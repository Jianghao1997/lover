package loversmission.hoodee.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import loversmission.hoodee.common.UserInfoUtil;
import loversmission.hoodee.dao.ProductInfoDao;
import loversmission.hoodee.dao.UserRelationDao;
import loversmission.hoodee.dao.UserStorageDao;
import loversmission.hoodee.dao.WxUserDao;
import loversmission.hoodee.entity.ProductInfo;
import loversmission.hoodee.entity.UserRelation;
import loversmission.hoodee.entity.WxUser;
import loversmission.hoodee.enums.ProductStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 商品相关
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月26日 9:39
 */
@Service
public class ProductInfoService {

    Logger logger = LoggerFactory.getLogger(ProductInfoService.class);

    @Resource
    private ProductInfoDao productInfoDao;

    @Resource
    private WxUserDao userDao;

    @Resource
    private UserRelationDao userRelationDao;

    @Resource
    private UserInfoUtil infoUtil;

    @Resource
    private UserStorageDao userStorageDao;

    @Resource
    private WxUserService wxUserService;

    /**
     * 上架商品或者修改商品
     * @param productInfo
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean saveOrUpdateInfo(ProductInfo productInfo) {
        boolean res;
        ProductInfo info = productInfoDao.getProductInfo(productInfo.getProductID());
        if (ObjectUtil.isNull(info)) {
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            long id = snowflake.nextId();
            productInfo.setProductID(Convert.toStr(id));
            productInfo.setCreateTime(new Date());
            res = productInfoDao.save(productInfo);
            if (!res) {
                logger.error("保存商品至数据库中失败！");
                throw new RuntimeException("商品上架失败啦！");
            }
            if (res) {
                wxUserService.sendMessage(productInfo.getCreateUserOpenID(), String.format("## 上架了新的商品: %s，快去购买吧！", productInfo.getProductName()));
            }
        } else {
            info.setProductDesc(productInfo.getProductDesc());
            info.setProductName(productInfo.getProductName());
            info.setProductClassify(productInfo.getProductClassify());
            info.setProductCredit(productInfo.getProductCredit());
            info.setUpdateTime(new Date());
            res = productInfoDao.updateById(info);
            if (!res) {
                logger.error("更新商品至数据库中失败！");
                throw new RuntimeException("商品修改失败啦！");
            }
        }
        return true;
    }

    /**
     * 购买商品
     * @param productId
     * @param openID
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean buyProduct(String productId, String openID) {
        ProductInfo info = productInfoDao.getProductInfo(productId);
        if (ObjectUtil.isNull(info)) {
            throw new RuntimeException("该商品不存在哦！");
        }
//        if (openID.equals(info.getCreateUserOpenID())) {
//            throw new RuntimeException("不能购买自己上架的商品哦！");
//        }
        if (info.getProductStatus().compareTo(ProductStatusEnum.UN_SALE.getCode()) == 0) {
            throw new RuntimeException("商品已经下架了哦！");
        }
        WxUser user = userDao.getUserInfo(openID);
        Integer credit = user.getWxUserCredit();
        if (credit.compareTo(info.getProductCredit()) < 0) {
            throw new RuntimeException("积分不够哦！");
        }
        credit = credit - info.getProductCredit();
        user.setWxUserCredit(credit);
        boolean res = userDao.updateById(user);
        if (!res) {
            throw new RuntimeException("oh! 扣除积分出错了啦！");
        } else {
            res = userStorageDao.saveUserStorage(productId, openID);
            if (!res) {
                throw new RuntimeException("oh! 不存此券出错啦！");
            }
        }
        wxUserService.sendMessage(openID, String.format("## 购买了：%s，有空记得提醒Ta使用哦！", info.getProductName()));
        return true;
    }

    /**
     * 获取两个人之间的商品 （可根据状态）
     * @param openID
     * @param status
     * @param searchValue
     * @return
     */
    public List<ProductInfo> getProductInfoList(String openID, Integer status, String searchValue) {
        List<String> openIds = new ArrayList<>(2);

        UserRelation relation = userRelationDao.getRelations(openID);
        if (ObjectUtil.isNotNull(relation)) {
            if (relation.getStatus().compareTo(1) != 0) {
                openIds.add(openID);
            } else {
                openIds.add(relation.getUserOpenId());
                openIds.add(relation.getUserCpOpenId());
            }
        } else {
            openIds.add(openID);
        }
        List<ProductInfo> productInfos = productInfoDao.getProductInfoListByOpenID(openIds, status, searchValue);
        Optional.ofNullable(productInfos).orElse(new ArrayList<>()).stream().forEach(p -> {
            fillUserName(p);
        });
        return productInfos;
    }


    /**
     * 获取具体商品商品详情
     * @param productId
     * @return
     */
    public ProductInfo getProductInfoById(String productId) {
        ProductInfo productInfo = productInfoDao.getProductInfo(productId);
        if (ObjectUtil.isNotNull(productInfo)) {
            fillUserName(productInfo);
            return productInfo;
        } else {
            throw new RuntimeException("oh! 没找到这个商品！");
        }
    }

    /**
     * 商品上架或下架
     * @param productId
     * @param openID
     * @param status
     * @return
     */
    public boolean updateProductStatus(String productId, String openID, Integer status) {
        ProductInfo productInfo = productInfoDao.getProductInfo(productId);
        if (ObjectUtil.isNotNull(productInfo)) {
            if (!openID.equals(productInfo.getCreateUserOpenID())) {
                throw new RuntimeException("不能修改对方上架的商品哦！");
            }
            if (status.compareTo(ProductStatusEnum.SALE.getCode()) == 0) {
                productInfo.setProductStatus(ProductStatusEnum.SALE.getCode());
            } else {
                productInfo.setProductStatus(ProductStatusEnum.UN_SALE.getCode());
            }
            boolean res = productInfoDao.updateById(productInfo);
            if (res) {
                wxUserService.sendMessage(productInfo.getCreateUserOpenID(), String.format("## %s了商品: %s，快去瞅瞅吧！", status.compareTo(ProductStatusEnum.SALE.getCode()) == 0 ? "重新上架" : "下架", productInfo.getProductName()));
            }
            return res;
        } else {
            throw new RuntimeException("oh! 没找到这个商品！");
        }
    }

    /**
     * 删除商品
     * @param productId
     * @param openID
     * @return
     */
    public boolean deleteProduct(String productId, String openID) {
        ProductInfo productInfo = productInfoDao.getProductInfo(productId);
        if (ObjectUtil.isNotNull(productInfo)) {
            if (!openID.equals(productInfo.getCreateUserOpenID())) {
                throw new RuntimeException("不能删除对方上架的商品哦！");
            }
            return productInfoDao.removeById(productInfo.getId());
        } else {
            throw new RuntimeException("oh! 没找到这个商品！");
        }
    }

    /**
     * 填充用户名称
     * @param productInfo
     */
    private void fillUserName(ProductInfo productInfo) {
        if (StringUtils.isNotBlank(productInfo.getCreateUserOpenID())) {
            String createUserName = infoUtil.getUserName(productInfo.getCreateUserOpenID());
            productInfo.setCreateUserName(createUserName);
        }
    }
}

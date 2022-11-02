package loversmission.hoodee.service;

import cn.hutool.core.util.ObjectUtil;
import loversmission.hoodee.common.UserInfoUtil;
import loversmission.hoodee.dao.ProductInfoDao;
import loversmission.hoodee.dao.UserStorageDao;
import loversmission.hoodee.entity.ProductInfo;
import loversmission.hoodee.entity.UserStorage;
import loversmission.hoodee.entity.WxUser;
import loversmission.hoodee.enums.StorageStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品相关
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月26日 9:39
 */
@Service
public class UserStorageService {

    @Resource
    private ProductInfoDao productInfoDao;

    @Resource
    private UserStorageDao userStorageDao;

    @Resource
    private UserInfoUtil infoUtil;

    @Resource
    private WxUserService wxUserService;

    /**
     * 使用券
     * @param storageID
     * @param openID
     * @return
     */
    public boolean userStorage(String storageID, String openID) {
        UserStorage info = userStorageDao.getUserStorage(storageID);
        if (ObjectUtil.isNull(info)) {
            throw new RuntimeException("没有找到所购买的商品哦！");
        }
        if (!openID.equals(info.getStorageUser())) {
            throw new RuntimeException("不能兑换对方的券哦！");
        }
        if (info.getStorageStatus().compareTo(StorageStatusEnum.USED.getCode()) == 0) {
            throw new RuntimeException("此券已经使用了哦！");
        }
        int newCount = info.getStorageCount() - 1;
        info.setStorageCount(newCount);
        if (newCount == 0 ) {
            info.setStorageStatus(StorageStatusEnum.USED.getCode());
        }
        boolean res = userStorageDao.updateById(info);
        if (!res) {
            throw new RuntimeException("oh! 券使用出错了啦！");
        }
        if (res) {
            ProductInfo productInfo = productInfoDao.getProductInfo(info.getProductID());
            wxUserService.sendMessage(openID, String.format("$$ 使用:%s，你得干活咯！", productInfo != null ? productInfo.getProductName() : "一张券"));
        }
        return true;
    }

    /**
     * 获取自己购买的券 （可根据状态）
     * @param openID
     * @param status
     * @return
     */
    public List<ProductInfo> getAllReadyBuyProductInfoList(String openID, Integer status) {
        List<UserStorage> storageList = userStorageDao.getUserStorageList(openID, status);
        if (storageList == null || storageList.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, UserStorage> map = Optional.ofNullable(storageList).orElse(new ArrayList<>()).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(UserStorage::getProductID, Function.identity(), (e1, e2) -> e2));
        List<String> productIds = Optional.ofNullable(storageList).orElse(new ArrayList<>()).stream().map(p -> p.getProductID()).collect(Collectors.toList());
        List<ProductInfo> productInfos = productInfoDao.getProductInfoListByProductIDs(productIds);
        productInfos.stream().forEach(p -> {
            p.setStorageID(map.getOrDefault(p.getProductID(), new UserStorage()).getStorageID());
            p.setCount(map.getOrDefault(p.getProductID(), new UserStorage()).getStorageCount());
            fillUserName(p);
        });
        return productInfos;
    }


    /**
     * 获取cp未使用的券
     * @param openID
     * @param status
     * @return
     */
    public List<ProductInfo> getCpAllReadyBuyProductInfoList(String openID, Integer status) {
        WxUser cpUserInfo = infoUtil.getCpUserInfo(openID, false);
        return getAllReadyBuyProductInfoList(cpUserInfo.getWxOpenId(), status);
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

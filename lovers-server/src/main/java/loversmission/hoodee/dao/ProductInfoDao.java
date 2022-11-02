package loversmission.hoodee.dao;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import loversmission.hoodee.dao.pre.ProductInfoMapper;
import loversmission.hoodee.entity.ProductInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月26日 9:37
 */
@Repository
@AllArgsConstructor
public class ProductInfoDao extends ServiceImpl<ProductInfoMapper, ProductInfo> {

    public ProductInfo getProductInfo(String productId) {
        return super.getOne(Wrappers.<ProductInfo>lambdaQuery().eq(ProductInfo::getProductID, productId));
    }

    public List<ProductInfo> getProductInfoListByOpenID(List<String> openIDs, Integer status, String searchValue) {
        return super.list(Wrappers.<ProductInfo>lambdaQuery()
                .in(ProductInfo::getCreateUserOpenID, openIDs)
                .eq(ObjectUtil.isNotNull(status), ProductInfo::getProductStatus, status)
                .like(StringUtils.isNotBlank(searchValue), ProductInfo::getProductName, searchValue)
                .orderByDesc(ProductInfo::getCreateTime));
    }

    public List<ProductInfo> getProductInfoListByProductIDs(List<String> productIds) {
        return super.list(Wrappers.<ProductInfo>lambdaQuery()
                .in(ProductInfo::getProductID, productIds)
                .orderByDesc(ProductInfo::getCreateTime));
    }
}

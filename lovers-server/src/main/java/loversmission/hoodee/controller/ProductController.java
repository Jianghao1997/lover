package loversmission.hoodee.controller;

import cn.hutool.core.util.ObjectUtil;
import loversmission.hoodee.common.Result;
import loversmission.hoodee.entity.ProductInfo;
import loversmission.hoodee.service.ProductInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月25日 11:26
 */
@RestController
@RequestMapping("product")
public class ProductController {

    @Resource
    private ProductInfoService productInfoService;

    @PostMapping("/saveInfo")
    public Result<Boolean> saveProductInfo(@Validated @RequestBody ProductInfo info) {
        if (!info.validatedCredit()) {
            return Result.error("必须要设点积分哦！");
        }
        Boolean res = productInfoService.saveOrUpdateInfo(info);
        return res ? Result.success(true) : Result.error();
    }

    @GetMapping("/get/{productId}")
    public Result<ProductInfo> getProductInfo(@PathVariable("productId") String productId) {
        ProductInfo info = productInfoService.getProductInfoById(productId);
        return ObjectUtil.isNotNull(info) ? Result.success(info) : Result.error();
    }

    @GetMapping("/get")
    public Result<List<ProductInfo>> getProductInfoList(String openID, Integer status, String searchValue) {
        List<ProductInfo> info = productInfoService.getProductInfoList(openID, status, searchValue);
        return Result.success(info);
    }

    @PostMapping("/buy")
    public Result<Boolean> buyProduct(String productId, String openID) {
        Boolean res = productInfoService.buyProduct(productId, openID);
        return res ? Result.success(true) : Result.error();
    }

    @PostMapping("/updateStatus")
    public Result<Boolean> updateStatus(String productId, String openID, Integer status) {
        Boolean res = productInfoService.updateProductStatus(productId, openID, status);
        return res ? Result.success(true) : Result.error();
    }

    @PostMapping("/delete")
    public Result<Boolean> delete(String missionID, String openID) {
        Boolean res = productInfoService.deleteProduct(missionID, openID);
        return res ? Result.success(true) : Result.error();
    }
}

package loversmission.hoodee.controller;

import loversmission.hoodee.common.Result;
import loversmission.hoodee.entity.ProductInfo;
import loversmission.hoodee.service.UserStorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月25日 11:26
 */
@RestController
@RequestMapping("storage")
public class UserStorageController {

    @Resource
    private UserStorageService userStorageService;

    @PostMapping("/use")
    public Result<Boolean> use(String storageID, String openID) {
        Boolean res = userStorageService.userStorage(storageID, openID);
        return res ? Result.success(true) : Result.error();
    }

    @GetMapping("/list")
    public Result<List<ProductInfo>> getProductList(String openID, Integer status) {
        List<ProductInfo> infoList = userStorageService.getAllReadyBuyProductInfoList(openID, status);
        return Result.success(infoList);
    }

    @GetMapping("cp/list")
    public Result<List<ProductInfo>> getCpProductList(String openID, Integer status) {
        List<ProductInfo> infoList = userStorageService.getCpAllReadyBuyProductInfoList(openID, status);
        return Result.success(infoList);
    }
}

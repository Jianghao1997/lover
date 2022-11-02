package loversmission.hoodee.dao;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import loversmission.hoodee.dao.pre.UserStorageMapper;
import loversmission.hoodee.entity.UserStorage;
import loversmission.hoodee.enums.StorageStatusEnum;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月05日 16:40
 */
@Repository
@AllArgsConstructor
public class UserStorageDao extends ServiceImpl<UserStorageMapper, UserStorage> {

    public UserStorage getUserStorage(String storageID) {
        return super.getOne(Wrappers.<UserStorage>lambdaQuery().eq(UserStorage::getStorageID, storageID));
    }

    public List<UserStorage> getUserStorageList(String openID, Integer status) {
        return super.list(Wrappers.<UserStorage>lambdaQuery().eq(UserStorage::getStorageUser, openID).eq(status != null, UserStorage::getStorageStatus, status));
    }

    public boolean saveUserStorage(String productId, String openID) {
        UserStorage storage = getOne(Wrappers.<UserStorage>lambdaQuery().eq(UserStorage::getProductID, productId));
        if (ObjectUtil.isNotNull(storage)) {
            int newCount = storage.getStorageCount() + 1;
            storage.setStorageCount(newCount);
            if (newCount > 0 ) {
                storage.setStorageStatus(StorageStatusEnum.UN_USE.getCode());
            }
            return super.updateById(storage);
        } else {
            Snowflake snowflake = IdUtil.getSnowflake(1, 1);
            long id = snowflake.nextId();
            UserStorage userStorage = UserStorage.builder()
                    .storageID(Convert.toStr(id))
                    .storageUser(openID)
                    .productID(productId)
                    .storageCount(1)
                    .createTime(new Date())
                    .storageStatus(StorageStatusEnum.UN_USE.getCode())
                    .build();
            return super.save(userStorage);
        }
    }
}

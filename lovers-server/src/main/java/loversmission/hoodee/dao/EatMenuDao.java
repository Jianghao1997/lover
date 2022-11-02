package loversmission.hoodee.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import loversmission.hoodee.dao.pre.EatMenuMapper;
import loversmission.hoodee.entity.EatMenu;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月25日 11:36
 */
@Repository
@AllArgsConstructor
public class EatMenuDao extends ServiceImpl<EatMenuMapper, EatMenu> {

    public Boolean dataExist(String text) {
        int count = super.count(Wrappers.<EatMenu>lambdaQuery().eq(EatMenu::getText, text));
        return count > 0;
    }
}

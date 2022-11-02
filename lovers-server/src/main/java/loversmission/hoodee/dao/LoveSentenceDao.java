package loversmission.hoodee.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import loversmission.hoodee.dao.pre.LoveSentenceMapper;
import loversmission.hoodee.entity.LoveSentence;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年07月25日 11:36
 */
@Repository
@AllArgsConstructor
public class LoveSentenceDao extends ServiceImpl<LoveSentenceMapper, LoveSentence> {

    public List<LoveSentence> getLoveSentence(String relationId) {
        return super.list(Wrappers.<LoveSentence>lambdaQuery()
                .eq(LoveSentence::getRelationId, relationId)
                .orderByDesc(LoveSentence::getCreateTime)
        );
    }
}

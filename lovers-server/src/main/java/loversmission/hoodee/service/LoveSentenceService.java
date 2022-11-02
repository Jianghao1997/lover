package loversmission.hoodee.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import loversmission.hoodee.dao.LoveSentenceDao;
import loversmission.hoodee.dao.UserRelationDao;
import loversmission.hoodee.entity.LoveSentence;
import loversmission.hoodee.entity.UserRelation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月13日 9:53
 */
@Service
public class LoveSentenceService {

    @Resource
    private LoveSentenceDao loveSentenceDao;

    @Resource
    private UserRelationDao relationDao;

    @Resource
    private WxUserService wxUserService;

    private String[] localInfo = {
            "你在哪里，家就在哪里",
            "牵了手就不能松开了，这是我们的约定",
            "我最喜欢的一个词，叫“我们”",
            "世界从来不会辜负可爱的人，而我们如此可爱",
            "我能做的不多，但你需要的时候，我总是在的",
            "自从我们相遇的那一刻，你是我白天黑夜不落的星",
            "愿未来所有好时光都有你相伴","只希望，我们都不要收敛对彼此的爱意"
    };
    /**
     * 保存情话
     * @param openId
     * @param info
     * @return
     */
    public boolean saveLoveSentence (String openId, String info) {
        UserRelation relations = relationDao.getRelations(openId);
        if (ObjectUtil.isNull(relations)) {
            throw new RuntimeException("未获取匹配信息哦！");
        }
        LoveSentence loveSentence = LoveSentence.builder()
                .loveInfo(info)
                .createUser(openId)
                .relationId(relations.getId())
                .createTime(new Date())
                .build();
        boolean res = loveSentenceDao.save(loveSentence);
        if (!res) {
            throw new RuntimeException("保存失败了哦！");
        }
        if (res) {
            wxUserService.sendMessage(openId, "## 给你写了一句小情话，快去瞅瞅吧！");
        }
        return true;
    }

    /**
     * 随机获取一个情话
     * @param openId
     * @return
     */
    public String getOneLoveSentence (String openId) {
        UserRelation relations = relationDao.getRelations(openId);
        if (ObjectUtil.isNull(relations)) {
            throw new RuntimeException("未获取匹配信息哦！");
        }
        List<LoveSentence> loveSentences = loveSentenceDao.getLoveSentence(relations.getId().toString());
        if (loveSentences.isEmpty()) {
            String msg = RandomUtil.randomEle(CollUtil.newArrayList(localInfo), CollUtil.newArrayList(localInfo).size());
            return msg;
        }
        return RandomUtil.randomEle(loveSentences.stream().map(LoveSentence::getLoveInfo).collect(Collectors.toList()), loveSentences.size());
    }

}

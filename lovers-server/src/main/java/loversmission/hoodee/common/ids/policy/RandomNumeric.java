package loversmission.hoodee.common.ids.policy;

import loversmission.hoodee.common.ids.IIdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * loversmission.hoodee.common.ids
 *
 * @Description：
 * @Author: 蒋豪
 * @Date: 2021.11.28 13:33
 * @Modified By:
 */
@Component
public class RandomNumeric implements IIdGenerator {
    /**
     * 获取ID，目前有两种实现方式
     * 1. 雪花算法，用于生成单号
     * 2. 日期算法，用于生成活动编号类，特性是生成数字串较短，但指定时间内不能生成太多
     * 3. 随机算法，用于生成策略ID
     *
     * @return ID
     */
    @Override
    public long nextId() {
        return Long.parseLong(RandomStringUtils.randomNumeric(11));
    }
}

package loversmission.hoodee.common.ids;

/**
 * Package: loversmission.hoodee.common.ids
 *
 * @Description：定义生成ID的策略接口
 * @Author: 蒋豪
 * @Date: 2021.11.28 13:33
 * @Modified By:
 */
public interface IIdGenerator {

    /**
     * 获取ID，目前有两种实现方式
     * 1. 雪花算法，用于生成单号
     * 2. 日期算法，用于生成活动编号类，特性是生成数字串较短，但指定时间内不能生成太多
     * 3. 随机算法，用于生成策略ID
     * @return ID
     */
    long nextId();
}

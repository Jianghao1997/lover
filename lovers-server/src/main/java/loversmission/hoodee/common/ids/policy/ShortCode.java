package loversmission.hoodee.common.ids.policy;

import loversmission.hoodee.common.ids.IIdGenerator;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Random;

/**
 * Package: cn.itedus.lottery.domain.support.ids.policy
 *
 * @Description：
 * @Author: 蒋豪
 * @Date: 2021.11.28 13:33
 * @Modified By:
 */
@Component
public class ShortCode implements IIdGenerator {
    /**
     * 获取ID，目前有两种实现方式
     * 1. 雪花算法，用于生成单号
     * 2. 日期算法，用于生成活动编号类，特性是生成数字串较短，但指定时间内不能生成太多
     * 3. 随机算法，用于生成策略ID
     *
     * @return ID
     */
    @Override
    public synchronized long nextId() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int weak = calendar.get(Calendar.WEEK_OF_YEAR);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // 打乱排序：2020年为准 + 小时 + 周期 + 日 + 三位随机数
        StringBuilder idStr = new StringBuilder();
        idStr.append(year - 2020);
        idStr.append(hour);
        idStr.append(String.format("%02d", weak));
        idStr.append(day);
        idStr.append(String.format("%03d", new Random().nextInt(1000)));
        return Long.parseLong(idStr.toString());
    }
}

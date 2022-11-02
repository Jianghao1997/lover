package loversmission.hoodee.common.ids;

import loversmission.hoodee.common.ids.policy.RandomNumeric;
import loversmission.hoodee.common.ids.policy.ShortCode;
import loversmission.hoodee.common.ids.policy.SnowFlake;
import loversmission.hoodee.enums.Ids;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Package: loversmission.hoodee.common.ids
 *
 * @Description： Id 策略模式上下文配置「在正式的完整的系统架构中，ID 的生成会有单独的服务来完成，其他服务来调用 ID 生成接口即可」
 * @Author: 蒋豪
 * @Date: 2021.11.28 13:32
 * @Modified By:
 */
@Configuration
public class IdContext {
    /**
     * 创建 ID 生成策略对象，属于策略设计模式的使用方式
     *
     * @param snowFlake 雪花算法，长码，大量
     * @param shortCode 日期算法，短码，少量，全局唯一需要自己保证
     * @param randomNumeric 随机算法，短码，大量，全局唯一需要自己保证
     * @return IIdGenerator 实现类
     */
    @Bean
    public Map<Ids, IIdGenerator> idGenerator(SnowFlake snowFlake, ShortCode shortCode, RandomNumeric randomNumeric) {
        Map<Ids, IIdGenerator> idGeneratorMap = new HashMap<>(8);
        idGeneratorMap.put(Ids.SnowFlake, snowFlake);
        idGeneratorMap.put(Ids.ShortCode, shortCode);
        idGeneratorMap.put(Ids.RandomNumeric, randomNumeric);
        return idGeneratorMap;
    }
}

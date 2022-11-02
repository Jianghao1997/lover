package loversmission.hoodee;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan("loversmission.hoodee.dao.pre")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class )
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = "loversmission.hoodee.*")
public class HoodeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HoodeeApplication.class, args);
    }

}

package sdk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class,DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass=true)
@EnableCaching
@EnableScheduling
public class SdkApplication /*implements CommandLineRunner */{
	public static void main(String[] args) {
        System.out.print("test");
		SpringApplication.run(SdkApplication.class, args);
	}



}

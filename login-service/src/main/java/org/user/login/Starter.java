// 启动类
package org.user.login;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("org.user.login.dao")
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

}

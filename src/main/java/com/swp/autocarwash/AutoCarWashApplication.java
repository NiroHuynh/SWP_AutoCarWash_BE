package com.swp.autocarwash;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AutoCarWashApplication {

    public static void main(String[] args) {

        SpringApplication.run(AutoCarWashApplication.class, args);
    }

}

package com.supermancell.okex.trans.trader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransTraderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransTraderApplication.class, args);
    }

}

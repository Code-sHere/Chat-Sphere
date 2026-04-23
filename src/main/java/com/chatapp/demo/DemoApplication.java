package com.chatapp.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.chatapp.demo.config.SpringConfigurator;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                DemoApplication.class,
                args
        );

    }

}
@Component
class SpringContextBridge
        implements ApplicationContextAware {

    @Override
    public void setApplicationContext(
            ApplicationContext applicationContext) {

        System.out.println(
                "ApplicationContext set in SpringConfigurator"
        );

        SpringConfigurator
                .setApplicationContext(
                        applicationContext
                );

    }

}
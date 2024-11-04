package com.sacidpak.order;

import com.sacidpak.common.repository.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@EnableFeignClients(basePackages = "com.sacidpak.clients")
@SpringBootApplication(
        scanBasePackages = {
                "com.sacidpak.order",
                "com.sacidpak.common.exception"
        }
)
@EnableJpaRepositories(basePackages = {"com.sacidpak.order.repository"}, repositoryBaseClass = BaseRepositoryImpl.class)
public class OrderServiceApplication {
        public static void main(String[] args) {
                SpringApplication.run(OrderServiceApplication.class,args);
        }
}

package com.sacidpak.product;

import com.sacidpak.common.repository.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;



@EnableTransactionManagement
@EnableAsync
@EnableFeignClients(basePackages = "com.sacidpak.clients")
@SpringBootApplication(
        scanBasePackages = {
                "com.sacidpak.product",
                "com.sacidpak.common.exception"
        }
)
@EnableJpaRepositories(basePackages = {"com.sacidpak.product.repository"}, repositoryBaseClass = BaseRepositoryImpl.class)
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class,args);
    }
}
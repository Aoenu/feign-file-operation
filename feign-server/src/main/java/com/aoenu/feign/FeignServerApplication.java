package com.aoenu.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aoenu
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FeignServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignServerApplication.class, args);
    }

}

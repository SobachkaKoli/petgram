package com.example.petgram;

import com.example.petgram.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class PetgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetgramApplication.class, args);
    }

//    @RequestMapping("/user")
//    public Principal user(Principal principal) {
//        return principal;
//    }


}

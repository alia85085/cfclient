package com.fss.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by asif on 10/18/17.
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
public class FirstClient {
    public static Logger logger = LoggerFactory.getLogger(FirstClient.class);

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    RestTemplate template;

    public static void main(String[] argv){
        logger.info(System.getenv("VCAP_SERVICES"));

        SpringApplication.run(FirstClient.class, argv);
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String index() {
        return "Hello Client";
    }

    @RequestMapping(value="/message", method = RequestMethod.GET)
    public String getMessage(@RequestParam("name") String name, @RequestParam("description") String description){
        logger.info("inside getMessage");
        URI uri = UriComponentsBuilder.fromUriString("//myfirstapp/message")
                .queryParam("name", name)
                .queryParam("description", description)
                .build()
                .toUri();
        String str = "init";
        try{
            str = template.getForObject(uri, String.class);
            logger.info("returned error {}", str);
        }
        catch (Exception e){
            logger.error("Error", e);
        }
        return str + "test1";

    }
}

package com.edr.technews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.edr.technews.repository")
public class MyAppTest {

  private static final Logger log = LoggerFactory.getLogger(MyAppTest.class);

  public static void main(String[] args) {
    SpringApplication.run(MyAppTest.class, args);
  }
}

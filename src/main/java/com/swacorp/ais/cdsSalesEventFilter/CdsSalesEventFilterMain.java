package com.swacorp.ais.cdsSalesEventFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
@ComponentScan({"com.swacorp.ais.cdsSalesEventFilter", "com.swacorp.ais.embassyFile"})
@Configuration
public class CdsSalesEventFilterMain {

   @Autowired
   private CdsSalesEventFilterController _auditLogScannerController;

   public static void main(String[] args) {
      SpringApplication.run(CdsSalesEventFilterMain.class, args);
   }

   @Bean
   public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
      return args -> {
         _auditLogScannerController.process(args);
      };
   }

}

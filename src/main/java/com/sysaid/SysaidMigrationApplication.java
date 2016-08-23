package com.sysaid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@SpringBootApplication
public class SysaidMigrationApplication {

	public static void main(String[] args) throws Exception{
		SpringApplication.run(Manager.class, args);
	}
}

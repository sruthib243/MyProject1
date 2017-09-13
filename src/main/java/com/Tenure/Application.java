package com.Tenure;

import java.sql.SQLException;
import java.text.ParseException;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@Configuration
@PropertySource("classpath:/application.properties")
public class Application {

	
	@Bean
	DataSource dataSource() throws SQLException {
		OracleDataSource dataSource = new OracleDataSource();
		dataSource.setUser("CWKDL");
		dataSource.setPassword("CwkD143$");
		dataSource.setURL("jdbc:oracle:thin:@//dbs-nprd2-vm-036.cisco.com:1550/CWKDEV.CISCO.COM");
		dataSource.setImplicitCachingEnabled(true);
		dataSource.setFastConnectionFailoverEnabled(true);
		return dataSource;
	}
	
	
	public static void main(String[] args) throws SQLException, ParseException {
		SpringApplication.run(Application.class, args);
	}

}

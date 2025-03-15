package com.book.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:config/secu.properties")
public class BookTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookTrackerApplication.class, args);
	}

}

package org.milosz.friendsreactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FriendsreactiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(FriendsreactiveApplication.class, args);
	}

}

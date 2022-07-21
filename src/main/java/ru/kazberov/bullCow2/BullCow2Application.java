package ru.kazberov.bullCow2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BullCow2Application {

	public static void main(String[] args) {
		SpringApplication.run(BullCow2Application.class, args);
	}

	public static void resetPassword(char[] password) {
		for (int i = 0; i < password.length; i++) {
			password[i] = '0';
		}
	}
}

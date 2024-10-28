package com.benjsoft.validationdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ValidationDemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ValidationDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		InputValidator validator = new InputValidator();

		// Example 1: Validate email
		ValidationResult emailResult = validator.validatePattern(
				"test@example.com", "email");
		System.out.println("Email validation: " + emailResult.getMessage());

		// Example 2: Validate user role
		ValidationResult roleResult = validator.validateAllowlist(
				"ADMIN", "userRole");
		System.out.println("Role validation: " + roleResult.getMessage());

		// Example 3: Validate complete user form
		UserForm form = new UserForm(
				"john_doe",
				"john@example.com",
				"USER",
				"1990-01-01",
				"<script>alert('xss')</script>Some notes"
		);

		UserFormValidation formValidation = validator.validateUserForm(form);

		if (formValidation.isValid()) {
			System.out.println("Form is valid!");
			System.out.println("Sanitized notes: " +
					formValidation.getSanitizedForm().getNotes());
		} else {
			System.out.println("Form validation errors:");
			formValidation.getErrors().forEach(System.out::println);
		}
	}
}

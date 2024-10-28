package com.benjsoft.validationdemo;

import org.apache.commons.lang3.StringEscapeUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class InputValidator {
    // Common regex patterns
    private static final Map<String, Pattern> REGEX_PATTERNS = new HashMap<>();

    static {
        // Username: 3-20 characters, alphanumeric and underscore only
        REGEX_PATTERNS.put("username",
                Pattern.compile("^[a-zA-Z0-9_]{3,20}$"));

        // Email: Basic email validation
        REGEX_PATTERNS.put("email",
                Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$"));

        // Phone: International format
        REGEX_PATTERNS.put("phone",
                Pattern.compile("^\\+?[1-9]\\d{1,14}$"));

        // Date: YYYY-MM-DD format
        REGEX_PATTERNS.put("date",
                Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$"));

        // Password: Minimum 8 characters, at least one letter and one number
        REGEX_PATTERNS.put("password",
                Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$"));

        // URL: Basic URL validation
        REGEX_PATTERNS.put("url",
                Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$"));

        // ZIP Code: US format
        REGEX_PATTERNS.put("zipcode",
                Pattern.compile("^\\d{5}(-\\d{4})?$"));
    }

    // Allowlist for specific fields
    private static final Map<String, List<String>> ALLOWLISTS = new HashMap<>();

    static {
        // Country codes
        ALLOWLISTS.put("countryCode", List.of("US", "CA", "GB", "FR", "DE", "IT", "ES", "AU", "JP"));

        // User roles
        ALLOWLISTS.put("userRole", List.of("USER", "ADMIN", "MANAGER", "GUEST"));

        // Document types
        ALLOWLISTS.put("documentType", List.of("PDF", "DOC", "DOCX", "XLS", "XLSX"));

        // Status codes
        ALLOWLISTS.put("status", List.of("ACTIVE", "INACTIVE", "PENDING", "SUSPENDED"));
    }

    /**
     * Validates input against a predefined regex pattern
     */
    public ValidationResult validatePattern(String input, String patternType) {
        if (input == null || input.trim().isEmpty()) {
            return new ValidationResult(false, "Input cannot be empty");
        }

        Pattern pattern = REGEX_PATTERNS.get(patternType);
        if (pattern == null) {
            throw new IllegalArgumentException("Unknown pattern type: " + patternType);
        }

        boolean matches = pattern.matcher(input).matches();
        return new ValidationResult(
                matches,
                matches ? "Valid input" : "Invalid format for " + patternType
        );
    }

    /**
     * Validates input against an allowlist
     */
    public ValidationResult validateAllowlist(String input, String allowlistType) {
        if (input == null || input.trim().isEmpty()) {
            return new ValidationResult(false, "Input cannot be empty");
        }

        List<String> allowlist = ALLOWLISTS.get(allowlistType);
        if (allowlist == null) {
            throw new IllegalArgumentException("Unknown allowlist type: " + allowlistType);
        }

        boolean isValid = allowlist.contains(input.trim().toUpperCase());
        return new ValidationResult(
                isValid,
                isValid ? "Valid input" : "Value not allowed for " + allowlistType
        );
    }

    /**
     * Validates and sanitizes a user input form
     */
    public UserFormValidation validateUserForm(UserForm form) {
        List<String> errors = new ArrayList<>();

        // Validate username
        ValidationResult usernameResult = validatePattern(form.getUsername(), "username");
        if (!usernameResult.isValid()) {
            errors.add("Username: " + usernameResult.getMessage());
        }

        // Validate email
        ValidationResult emailResult = validatePattern(form.getEmail(), "email");
        if (!emailResult.isValid()) {
            errors.add("Email: " + emailResult.getMessage());
        }

        // Validate role
        ValidationResult roleResult = validateAllowlist(form.getRole(), "userRole");
        if (!roleResult.isValid()) {
            errors.add("Role: " + roleResult.getMessage());
        }

        // Validate and parse date
        try {
            if (validatePattern(form.getBirthDate(), "date").isValid()) {
                LocalDate.parse(form.getBirthDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            } else {
                errors.add("Birth Date: Invalid date format");
            }
        } catch (DateTimeParseException e) {
            errors.add("Birth Date: " + e.getMessage());
        }

        // Sanitize text input
        String sanitizedNotes = sanitizeInput(form.getNotes());

        return new UserFormValidation(
                errors.isEmpty(),
                errors,
                new UserForm(
                        form.getUsername(),
                        form.getEmail(),
                        form.getRole(),
                        form.getBirthDate(),
                        sanitizedNotes
                )
        );
    }

    /**
     * Sanitizes text input to prevent XSS
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Use Apache Commons Text for HTML escaping
        return StringEscapeUtils.escapeHtml4(input.trim());
    }
}

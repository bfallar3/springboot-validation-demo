package com.benjsoft.validationdemo;

import java.util.List;

class UserFormValidation {
    private final boolean valid;
    private final List<String> errors;
    private final UserForm sanitizedForm;

    public UserFormValidation(boolean valid, List<String> errors, UserForm sanitizedForm) {
        this.valid = valid;
        this.errors = errors;
        this.sanitizedForm = sanitizedForm;
    }

    // Getters
    public boolean isValid() { return valid; }
    public List<String> getErrors() { return errors; }
    public UserForm getSanitizedForm() { return sanitizedForm; }
}

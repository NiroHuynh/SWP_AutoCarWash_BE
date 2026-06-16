package com.swp.autocarwash.common.validation;

public final class PhoneValidator {
    private PhoneValidator() {
    }

    public static boolean isValid(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        return phone.matches(
                "^(0|\\+84)[0-9]{9}$"
        );
    }
}

package com.swp.autocarwash.common.validation;

public final class LicensePlateValidator {

    private LicensePlateValidator() {
    }

    public static boolean isValid(
            String licensePlate
    ) {

        if (licensePlate == null
                || licensePlate.isBlank()) {

            return false;
        }
        return licensePlate.matches(
                "^[0-9]{2}[A-Z]-?[0-9]{4,5}$"
        );
    }
}
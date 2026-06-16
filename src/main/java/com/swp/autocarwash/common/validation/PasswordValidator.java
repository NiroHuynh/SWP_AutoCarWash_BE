package com.swp.autocarwash.common.validation;

public final class PasswordValidator {

    private PasswordValidator() {
    }


    public static boolean isStrong(
            String password
    ){

        if(password == null){
            return false;
        }


        return password.matches(
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$"
        );
    }
}
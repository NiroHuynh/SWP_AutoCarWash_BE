package com.swp.autocarwash.auth.exception;

public class AccountDisabledException extends RuntimeException{

    //constructor của class con
    public AccountDisabledException (String message){
        //Gọi constructor của cha và đưa lỗi cho cha gán hộ
        super(message);
    }


}

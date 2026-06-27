package com.swp.autocarwash.auth.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {


    /**
     * Message returned after register process
     */
    private String message;


}

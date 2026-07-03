package com.swp.autocarwash.auth.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String email;
    private String name;
    private Integer stationId;

}

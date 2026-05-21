package com.hotel.msauth.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String username;
    private String rol;
    private String mensaje;
}
package org.raf.usermanagement.dtos.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TokenResponseDto {
    public String token;

    public TokenResponseDto(String token) {
        this.token = token;
    }
}

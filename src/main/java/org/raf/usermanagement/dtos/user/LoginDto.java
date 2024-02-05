package org.raf.usermanagement.dtos.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    public String email;
    public String password;
}

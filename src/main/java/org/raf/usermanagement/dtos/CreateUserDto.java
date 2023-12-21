package org.raf.usermanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    public String name;
    public String surname;
    public String email;
    public String password;
    public List<String> permissions = new ArrayList<>();
}

package org.raf.usermanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    public Long id;
    public String name;
    public String surname;
    public String email;
    public List<String> permissions = new ArrayList<>();

}

package org.raf.usermanagement.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class ReadUsersDto {
    public List<UserDto> users = new ArrayList<>();
}

package org.raf.usermanagement.dtos.usisivac;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.raf.usermanagement.domain.UsisivacStatus;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
public class UsisivacDto {
    public Long id;
    public UsisivacStatus status;
    public String name;
    public Boolean isChanging;
    public LocalDate dateAdded;
    public Boolean active;

}

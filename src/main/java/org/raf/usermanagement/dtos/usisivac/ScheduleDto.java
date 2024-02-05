package org.raf.usermanagement.dtos.usisivac;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.raf.usermanagement.domain.ScheduledAction;
import org.raf.usermanagement.domain.UsisivacStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    public String jwt;
    public Long usisivacid;
    public UsisivacStatus status;
    public String time;
}

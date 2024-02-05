package org.raf.usermanagement.service;

import org.raf.usermanagement.domain.Usisivac;
import org.raf.usermanagement.dtos.usisivac.*;

import java.util.List;

public interface UsisivacService {

    ReadUsisivacDto search(String jwt, SearchUsisivacDto searchUsisivacDto);

    void add(String jwt, UsisivacDto usisivacDto);

    void start(String jwt, Long id) throws Exception;

    void stop(String jwt, Long id) throws Exception;

    void discharge(String jwt, Long id) throws Exception;

    void scheduled() throws InterruptedException;

    void remove(String jwt, Long id);

    void schedule(String jwt, ScheduleDto scheduleDto);

    ScheduleErrorsDto getScheduleErrors(String jwt);


}

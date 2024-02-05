package org.raf.usermanagement.service;

import org.raf.usermanagement.domain.Usisivac;

public interface StartStopAsyncService {
    void startAsync(Usisivac usisivac);

    void stopAsync(Usisivac usisivac);

    void dischargeAsync(Usisivac usisivac);
}

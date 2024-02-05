package org.raf.usermanagement.service;

import org.raf.usermanagement.controller.WebSocketController;
import org.raf.usermanagement.db.UsisivacRepository;
import org.raf.usermanagement.domain.Usisivac;
import org.raf.usermanagement.domain.UsisivacStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class StartStopAsyncServiceImpl implements StartStopAsyncService{

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private WebSocketController webSocketController;

    private UsisivacRepository usisivacRepository;

    public StartStopAsyncServiceImpl(UsisivacRepository usisivacRepository) {
        this.usisivacRepository = usisivacRepository;
    }

    @Override
    @Async
    public void startAsync(Usisivac usisivac) {
        Random random = new Random();
        double delay = random.nextDouble(5)*1000;
        try {
            Thread.sleep((long) delay + 15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        usisivac.setStatus(UsisivacStatus.ON);
        usisivac.setIsChanging(false);
        usisivacRepository.save(usisivac);
        simpMessagingTemplate.convertAndSend("/topic/vacuum", usisivac);
        //webSocketController.processMessageFromClient();
    }

    @Override
    @Async
    public void stopAsync(Usisivac usisivac) {
        Random random = new Random();
        double delay = random.nextDouble(5)*1000;
        try {
            Thread.sleep((long) delay + 15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        usisivac.setCycle(usisivac.getCycle() + 1);
        if(usisivac.getCycle() == 3){
            usisivacRepository.save(usisivac);
            dischargeAsync(usisivac);
            return;
        }
        usisivac.setStatus(UsisivacStatus.OFF);
        usisivac.setIsChanging(false);
        usisivacRepository.save(usisivac);
        simpMessagingTemplate.convertAndSend("/topic/vacuum", usisivac);
        //webSocketController.processMessageFromClient();
    }

    @Override
    @Async
    public void dischargeAsync(Usisivac usisivac){
        Random random = new Random();
        double delay = random.nextDouble(5)*1000 + 30000;
        usisivac = usisivacRepository.findById(usisivac.getId()).get();
        try {
            Thread.sleep((long)delay /2);
            usisivac.setStatus(UsisivacStatus.DISCHARGING);
            usisivacRepository.save(usisivac);
            Thread.sleep((long)delay /2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        usisivac = usisivacRepository.findById(usisivac.getId()).get();
        usisivac.setStatus(UsisivacStatus.OFF);
        usisivac.setIsChanging(false);
        usisivac.setCycle(0);
        usisivacRepository.save(usisivac);
        simpMessagingTemplate.convertAndSend("/topic/vacuum", usisivac);
        //webSocketController.processMessageFromClient();
    }
}

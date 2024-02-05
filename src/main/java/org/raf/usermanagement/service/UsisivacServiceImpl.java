package org.raf.usermanagement.service;

import org.raf.usermanagement.db.*;
import org.raf.usermanagement.domain.*;
import org.raf.usermanagement.dtos.usisivac.*;
import org.raf.usermanagement.exceptions.CustomException;
import org.raf.usermanagement.exceptions.ErrorCode;
import org.raf.usermanagement.security.MyTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UsisivacServiceImpl implements UsisivacService{

    private UsisivacRepository usisivacRepository;
    private ScheduledActionRepository scheduledActionRepository;
    private PermissionRepository permissionRepository;
    private UserPermissionRepository userPermissionRepository;
    private MyTokenService tokenService;
    @Autowired
    private StartStopAsyncService startStopAsyncService;

    @Autowired
    private ScheduleErrorRepository scheduleErrorRepository;

    public UsisivacServiceImpl(UsisivacRepository usisivacRepository, ScheduledActionRepository scheduledActionRepository, PermissionRepository permissionRepository, UserPermissionRepository userPermissionRepository, MyTokenService tokenService) {
        this.usisivacRepository = usisivacRepository;
        this.scheduledActionRepository = scheduledActionRepository;
        this.permissionRepository = permissionRepository;
        this.userPermissionRepository = userPermissionRepository;
        this.tokenService = tokenService;
    }

    @Override
    public ReadUsisivacDto search(String jwt, SearchUsisivacDto searchUsisivacDto) {
        User user = tokenService.getUserByJwt(jwt);
        Permission tmppermission = permissionRepository.findPermissionByName("can_search_vacuum").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, tmppermission).isEmpty()){
            throw new RuntimeException("No permission");
        }
        List<Usisivac> usisivaci = usisivacRepository.findAllByAddedBy(user);
        if(searchUsisivacDto.getName() != null && !searchUsisivacDto.getName().equals("")){
            usisivaci.removeIf(usisivac -> !usisivac.getName().toLowerCase().contains(searchUsisivacDto.getName().toLowerCase()));
        }
        if(searchUsisivacDto.getStatus() != null){
            usisivaci.removeIf(usisivac -> !searchUsisivacDto.getStatus().contains(usisivac.getStatus().toString()));
        }
        if(searchUsisivacDto.getDateFrom() != null && searchUsisivacDto.getDateTo() != null){
            usisivaci.removeIf(usisivac -> usisivac.getDateAdded().isBefore(searchUsisivacDto.getDateFrom()) || usisivac.getDateAdded().isAfter(searchUsisivacDto.getDateTo()));
        }
        ReadUsisivacDto response = new ReadUsisivacDto();
        for(Usisivac usisivac : usisivaci){
            UsisivacDto usisivacDto = new UsisivacDto();
            usisivacDto.id = usisivac.getId();
            usisivacDto.name = usisivac.getName();
            usisivacDto.status = usisivac.getStatus();
            usisivacDto.dateAdded = usisivac.getDateAdded();
            usisivacDto.active = usisivac.getActive();
            usisivacDto.isChanging = usisivac.getIsChanging();
            response.vacuums.add(usisivacDto);
        }
        return response;
    }

    @Override
    public void add(String jwt, UsisivacDto usisivacDto) {
        User user = tokenService.getUserByJwt(jwt);
        Permission tmppermission = permissionRepository.findPermissionByName("can_add_vacuum").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, tmppermission).isEmpty()){
            throw new RuntimeException("No permission");
        }
        Usisivac usisivac = new Usisivac();
        usisivac.setName(usisivacDto.name);
        usisivac.setActive(true);
        usisivac.setStatus(UsisivacStatus.OFF);
        usisivac.setDateAdded(LocalDateTime.now().toLocalDate());
        usisivac.setAddedBy(user);
        usisivacRepository.save(usisivac);
    }

    @Override
    public void start(String jwt, Long id) throws Exception {
        User user = tokenService.getUserByJwt(jwt);
        Permission tmppermission = permissionRepository.findPermissionByName("can_start_vacuum").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, tmppermission).isEmpty()){
            throw new RuntimeException("No permission");
        }
        try {
            Usisivac usisivac = usisivacRepository.findById(id).get();
            if(!(usisivac.getStatus() == UsisivacStatus.OFF) || usisivac.getIsChanging()){
                throw new RuntimeException("Usisivac is already on");
            }
            usisivac.setIsChanging(true);
            usisivacRepository.save(usisivac);
            //startasync(usisivac);
            startStopAsyncService.startAsync(usisivac);
        }
        catch (Exception e){
            e.printStackTrace(); //TODO: VRATI PORUKU!!!
            throw new Exception(e.getMessage());
        }
    }

    //@Async
    //public void startasync(Usisivac usisivac){
    //    Random random = new Random();
    //    double delay = random.nextDouble(5)*1000;
    //    try {
    //        Thread.sleep((long) delay + 15000);
    //    } catch (InterruptedException e) {
    //        e.printStackTrace();
    //    }
    //    usisivac.setStatus(UsisivacStatus.ON);
    //    usisivac.setIsChanging(false);
    //    usisivacRepository.save(usisivac);
    //}

    @Override
    public void stop(String jwt, Long id) throws Exception {
        User user = tokenService.getUserByJwt(jwt);
        Permission tmppermission = permissionRepository.findPermissionByName("can_stop_vacuum").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, tmppermission).isEmpty()){
            throw new RuntimeException("No permission");
        }
        try {
            Usisivac usisivac = usisivacRepository.findById(id).get();
            if(!(usisivac.getStatus() == UsisivacStatus.ON) || usisivac.getIsChanging()){
                throw new RuntimeException("Usisivac is already on");
            }
            usisivac.setIsChanging(true);
            usisivacRepository.save(usisivac);
            //stopasync(usisivac);
            startStopAsyncService.stopAsync(usisivac);
        }
        catch (Exception e){
            e.printStackTrace(); //TODO: VRATI PORUKU!!!
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void remove(String jwt, Long id) {
        User user = tokenService.getUserByJwt(jwt);
        Permission tmppermission = permissionRepository.findPermissionByName("can_remove_vacuum").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, tmppermission).isEmpty()){
            throw new RuntimeException("No permission");
        }
        Usisivac usisivac = usisivacRepository.findById(id).get();
        if(usisivac.getIsChanging() || usisivac.getStatus() != UsisivacStatus.OFF){
            throw new RuntimeException("Usisivac is not off");
        }
        usisivac.setActive(false);
        usisivacRepository.save(usisivac);
    }

    @Override
    public void schedule(String jwt, ScheduleDto scheduleDto) {
        ScheduledAction action = new ScheduledAction();
        action.setStatus(scheduleDto.status);
        Usisivac usisivac = usisivacRepository.findById(scheduleDto.usisivacid).orElseThrow(()->new CustomException("Usisivac not found", ErrorCode.NOT_AUTHORIZED, HttpStatus.BAD_REQUEST));

        action.setUsisivac(usisivac);
        action.setTime(LocalDateTime.parse(scheduleDto.time));
        action.setJwt(jwt);
        scheduledActionRepository.save(action);
    }

    @Override
    public ScheduleErrorsDto getScheduleErrors(String jwt) {
        User user = tokenService.getUserByJwt(jwt);

        List<ScheduleError> scheduleErrors = scheduleErrorRepository.findAllByUser(user).orElseThrow(()->new CustomException("Internal error", ErrorCode.NOT_AUTHORIZED, HttpStatus.BAD_REQUEST));

        ScheduleErrorsDto scheduleErrorsDto = new ScheduleErrorsDto();
        scheduleErrorsDto.errors = new ArrayList<>();

        for(ScheduleError scheduleError : scheduleErrors){
            ErrorDto scheduleErrorDto = new ErrorDto();
            scheduleErrorDto.date = scheduleError.getDate().toString();
            scheduleErrorDto.message = scheduleError.getMessage();
            scheduleErrorDto.operation = scheduleError.getOperation();
            scheduleErrorDto.usisivacName = scheduleError.getUsisivac().getName();
            scheduleErrorsDto.errors.add(scheduleErrorDto);

        }

        return scheduleErrorsDto;
    }

    //@Async
    //public void stopasync(Usisivac usisivac){
    //    Random random = new Random();
    //    double delay = random.nextDouble(5)*1000;
    //    try {
    //        Thread.sleep((long) delay + 15000);
    //    } catch (InterruptedException e) {
    //        e.printStackTrace();
    //    }
    //    usisivac.setCycle(usisivac.getCycle() + 1);
    //    if(usisivac.getCycle() == 3){
    //        usisivacRepository.save(usisivac);
    //        discharge(usisivac);
    //        return;
    //    }
    //    usisivac.setStatus(UsisivacStatus.OFF);
    //    usisivac.setIsChanging(false);
    //    usisivacRepository.save(usisivac);
    //}

    @Override
    public void discharge(String jwt, Long id) throws Exception {
        User user = tokenService.getUserByJwt(jwt);
        Permission tmppermission = permissionRepository.findPermissionByName("can_discharge_vacuum").get();
        if(userPermissionRepository.findUserPermissionByUserAndPermission(user, tmppermission).isEmpty()){
            throw new RuntimeException("No permission");
        }
        try {
            Usisivac usisivac = usisivacRepository.findById(id).get();
            if(!(usisivac.getStatus() == UsisivacStatus.OFF) || usisivac.getIsChanging()){
                throw new RuntimeException("Usisivac is already on");
            }
            usisivac.setIsChanging(true);
            usisivacRepository.save(usisivac);
            //discharge(usisivac);
            startStopAsyncService.dischargeAsync(usisivac);
        }
        catch (Exception e){
            e.printStackTrace(); //TODO: VRATI PORUKU!!!
            throw new Exception(e.getMessage());
        }
    }

    //@Async
    //public void discharge(Usisivac usisivac){
    //    Random random = new Random();
    //    double delay = random.nextDouble(5)*1000 + 30000;
    //    try {
    //        Thread.sleep((long)delay /2);
    //        usisivac.setStatus(UsisivacStatus.DISCHARGING);
    //        usisivacRepository.save(usisivac);
    //        Thread.sleep((long)delay /2);
    //    } catch (InterruptedException e) {
    //        e.printStackTrace();
    //    }
    //    usisivac.setStatus(UsisivacStatus.OFF);
    //    usisivac.setIsChanging(false);
    //    usisivac.setCycle(0);
    //    usisivacRepository.save(usisivac);
    //}

    @Scheduled(cron = "0 * * * * *", zone = "Europe/Belgrade")
    public void scheduled() throws InterruptedException{
        System.out.println("checking for scheduled actions");
        LocalDateTime time = LocalDateTime.now();
        List<ScheduledAction> actions = scheduledActionRepository.findAllByTime(time);
        for(ScheduledAction action : actions){
            scheduledActionRepository.delete(action);
            try {
                switch (action.getStatus()) {
                    case ON:
                        start(action.getJwt(), action.getUsisivac().getId());
                        break;
                    case OFF:
                        stop(action.getJwt(), action.getUsisivac().getId());
                        break;
                    case DISCHARGING:
                        discharge(action.getJwt(), action.getUsisivac().getId());
                        break;
                }
            }
            catch (Exception e){
                ScheduleError scheduleError = new ScheduleError();
                scheduleError.setDate(LocalDateTime.now());
                scheduleError.setUsisivac(action.getUsisivac());
                scheduleError.setUser(tokenService.getUserByJwt(action.getJwt()));
                scheduleError.setOperation(action.getStatus().toString());
                scheduleError.setMessage(e.getMessage());
                scheduleErrorRepository.save(scheduleError);
            }
        }
    }
}

package org.raf.usermanagement.controller;

import org.raf.usermanagement.domain.Usisivac;
import org.raf.usermanagement.dtos.usisivac.ReadUsisivacDto;
import org.raf.usermanagement.dtos.usisivac.ScheduleDto;
import org.raf.usermanagement.dtos.usisivac.SearchUsisivacDto;
import org.raf.usermanagement.dtos.usisivac.UsisivacDto;
import org.raf.usermanagement.service.UsisivacService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RestController
@RequestMapping("/vacuum")
public class VacuumController {

    private UsisivacService vacuumService;

    public VacuumController(UsisivacService vacuumService) {
        this.vacuumService = vacuumService;
    }

    @PostMapping("/search")
    public ResponseEntity<ReadUsisivacDto> search(@RequestHeader("Authorization") String jwt, @RequestBody SearchUsisivacDto searchUsisivacDto){
        return new ResponseEntity<>(vacuumService.search(jwt, searchUsisivacDto), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestHeader("Authorization") String jwt, @RequestBody UsisivacDto usisivac){
        vacuumService.add(jwt, usisivac);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/start/{id}")
    public ResponseEntity<?> start(@RequestHeader("Authorization") String jwt, @PathVariable Long id){
        try {
            vacuumService.start(jwt, id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/stop/{id}")
    public ResponseEntity<?> stop(@RequestHeader("Authorization") String jwt, @PathVariable Long id){
        try {
            vacuumService.stop(jwt, id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/discharge/{id}")
    public ResponseEntity<?> discharge(@RequestHeader("Authorization") String jwt, @PathVariable Long id){
        try {
            vacuumService.discharge(jwt, id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> remove(@RequestHeader("Authorization") String jwt, @PathVariable Long id){
        vacuumService.remove(jwt, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/schedule")
    public ResponseEntity<?> schedule(@RequestHeader("Authorization") String jwt,@RequestBody ScheduleDto scheduleDto){
        vacuumService.schedule(jwt, scheduleDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/scheduleErrors")
    public ResponseEntity<?> getScheduleErrors(@RequestHeader("Authorization") String jwt){
        return new ResponseEntity<>(vacuumService.getScheduleErrors(jwt), HttpStatus.OK);
    }
}

package org.raf.usermanagement.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ScheduledAction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UsisivacStatus status;

    private String jwt;
    @ManyToOne
    private Usisivac usisivac;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
}

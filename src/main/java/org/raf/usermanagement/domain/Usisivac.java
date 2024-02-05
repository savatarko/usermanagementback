package org.raf.usermanagement.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
public class Usisivac {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private UsisivacStatus status;

    @ManyToOne
    private User addedBy;

    private Boolean active;

    private String name;

    private LocalDate dateAdded;

    @Version
    private Integer version = 0;

    private Boolean isChanging = false;

    private Integer cycle = 0;
}

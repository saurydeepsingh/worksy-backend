package com.worksy.entity.enums;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    private int id;
    private String name;
    private String Password;
    private String address;
    private String details;
}

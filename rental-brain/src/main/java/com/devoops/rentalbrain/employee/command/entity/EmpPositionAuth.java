package com.devoops.rentalbrain.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="emp_position_auth")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EmpPositionAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String positionId;
    @Column
    private String authId;

    public EmpPositionAuth(String positionId, String authId) {
        this.positionId = positionId;
        this.authId = authId;
    }
}

package com.devoops.rentalbrain.employee.command.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="login_history")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String loginSuccessDate;
    @Column
    private Character loginIsSucceed;
    @Column
    private String loginIp;
    @Column
    private Long empId;
}

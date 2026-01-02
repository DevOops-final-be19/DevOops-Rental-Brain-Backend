package com.devoops.rentalbrain.employee.command.dto;

import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@ToString
public class UserImpl extends User {
    private Long id;
    private String employeeCode;
    private String empId;
    private String name;
    private String phone;
    private String email;
    private String addr;
    private String birthday;
    private Character gender;
    private Character status;
    private String dept;
    private String hireDate;
    private String resignDate;
    private Long positionId;

    public UserImpl(String username, @Nullable String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public void setUserInfo(UserDetailInfoDTO userDetailInfoDTO) {
        this.id = userDetailInfoDTO.getId();
        this.employeeCode = userDetailInfoDTO.getEmployeeCode();
        this.empId = userDetailInfoDTO.getEmpId();
        this.name = userDetailInfoDTO.getName();
        this.phone = userDetailInfoDTO.getPhone();
        this.email = userDetailInfoDTO.getEmail();
        this.addr = userDetailInfoDTO.getAddr();
        this.birthday = userDetailInfoDTO.getBirthday();
        this.gender = userDetailInfoDTO.getGender();
        this.status = userDetailInfoDTO.getStatus();
        this.dept = userDetailInfoDTO.getDept();
        this.hireDate = userDetailInfoDTO.getHireDate();
        this.resignDate = userDetailInfoDTO.getResignDate();
        this.positionId = userDetailInfoDTO.getPositionId();
    }
}
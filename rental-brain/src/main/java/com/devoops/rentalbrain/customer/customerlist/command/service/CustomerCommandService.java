package com.devoops.rentalbrain.customer.customerlist.command.service;

import com.devoops.rentalbrain.customer.customerlist.command.dto.CustomerCommandDto;
import com.devoops.rentalbrain.customer.customerlist.command.entity.Customer;
import com.devoops.rentalbrain.customer.customerlist.command.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 수정 필요!!!!!!!!!!!!!!!!!!!!!
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CustomerCommandService {

    private final CustomerRepository customerRepository;

    public Long registerCustomer(CustomerCommandDto dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setInCharge(dto.getInCharge());
        customer.setDept(dto.getDept());
        customer.setCallNum(dto.getCallNum());
        customer.setPhone(dto.getPhone());
        customer.setEmail(dto.getEmail());
        customer.setBusinessNum(dto.getBusinessNum());
        customer.setAddr(dto.getAddr());
        customer.setMemo(dto.getMemo());
        customer.setStar(dto.getStar());
        customer.setChannelId(dto.getChannelId());
        customer.setSegmentId(dto.getSegmentId());

        return customerRepository.save(customer).getId();
    }

    public void updateCustomer(Long id, CustomerCommandDto dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 고객이 존재하지 않습니다. ID=" + id));

        log.info("수정 전 Customer: {}", customer);

        if(dto.getName() != null) customer.setName(dto.getName());
        if(dto.getInCharge() != null) customer.setInCharge(dto.getInCharge());
        if(dto.getDept() != null) customer.setDept(dto.getDept());
        if(dto.getCallNum() != null) customer.setCallNum(dto.getCallNum());
        if(dto.getPhone() != null) customer.setPhone(dto.getPhone());
        if(dto.getEmail() != null) customer.setEmail(dto.getEmail());
        if(dto.getBusinessNum() != null) customer.setBusinessNum(dto.getBusinessNum());
        if(dto.getAddr() != null) customer.setAddr(dto.getAddr());
        if(dto.getMemo() != null) customer.setMemo(dto.getMemo());
        if(dto.getStar() != null) customer.setStar(dto.getStar());
        if(dto.getChannelId() != null) customer.setChannelId(dto.getChannelId());
        if(dto.getSegmentId() != null) customer.setSegmentId(dto.getSegmentId());

        // Transaction 종료 시 자동 Update
    }
}
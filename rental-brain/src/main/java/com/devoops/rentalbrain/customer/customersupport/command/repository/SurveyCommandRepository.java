package com.devoops.rentalbrain.customer.customersupport.command.repository;

import com.devoops.rentalbrain.customer.customersupport.command.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyCommandRepository extends JpaRepository<Survey,Long> {
}

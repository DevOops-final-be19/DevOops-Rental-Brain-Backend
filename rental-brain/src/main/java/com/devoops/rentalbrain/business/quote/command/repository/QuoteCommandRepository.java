package com.devoops.rentalbrain.business.quote.command.repository;

import com.devoops.rentalbrain.business.quote.command.entity.QuoteCommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteCommandRepository extends JpaRepository<QuoteCommandEntity, Long> {
}

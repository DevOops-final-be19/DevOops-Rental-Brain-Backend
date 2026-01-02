package com.devoops.rentalbrain.business.quote.command.service;

import com.devoops.rentalbrain.business.quote.command.dto.QuoteCommandCreateDTO;
import com.devoops.rentalbrain.business.quote.command.dto.QuoteCommandResponseDTO;
import jakarta.transaction.Transactional;

public interface QuoteCommandService {
    QuoteCommandCreateDTO insertQuote(QuoteCommandCreateDTO quoteCommandCreateDTO);

    QuoteCommandResponseDTO updateQuote(Long quoteId, QuoteCommandResponseDTO quoteCommandResponseDTO);

    void deleteQuote(Long quoteId);

}

package com.devoops.rentalbrain.business.quote.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuoteCommandService {

    private final QuoteCommandRepository quoteCommandRepository;

}

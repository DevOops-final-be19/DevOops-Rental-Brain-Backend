package com.devoops.rentalbrain.business.quote.command.controller;

import com.devoops.rentalbrain.business.quote.command.service.QuoteCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quote")
@Slf4j
public class QuoteCommandController {
    private final QuoteCommandService quoteCommandService;


    @Autowired
    public QuoteCommandController(QuoteCommandService quoteCommandService) {
        this.quoteCommandService = quoteCommandService;
    }

    @PostMapping("/insertQuoter")
}

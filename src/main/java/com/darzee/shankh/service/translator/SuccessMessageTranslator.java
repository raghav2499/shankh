package com.darzee.shankh.service.translator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.service.LocalisationService;

@Service
public class SuccessMessageTranslator {

    @Autowired
    private LocalisationService localisationService;

    public  String getTranslatedMessage(String message) {
        return localisationService.translate(message);
    }
    
}

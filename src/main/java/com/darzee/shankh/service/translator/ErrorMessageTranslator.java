package com.darzee.shankh.service.translator;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.darzee.shankh.service.LocalisationService;


@Service
public class ErrorMessageTranslator {

    @Autowired
    private LocalisationService localisationService;

    public String getTranslatedMessage(String message,Object... args) {
        String msg= localisationService.translate(message);
        return MessageFormat.format(msg,args);
    }
}

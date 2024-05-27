package com.darzee.shankh.service;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LocalisationService {
    private final MessageSource messageSource;

    public LocalisationService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String translate(String englishText) {
        String key = englishText.toLowerCase().replace(" ", ".");
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null,"No translation found for :"+englishText, locale);
    }
}

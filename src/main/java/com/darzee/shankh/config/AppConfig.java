package com.darzee.shankh.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    public enum Language {
        ENGLISH("en"),
        HINDI("hi"),
        PUNJABI("pa"),
        GUJARATI("gu"),
        MARATHI("mr"),
        TELUGU("te"),
        BENGALI("bn"),
        KANNADA("kn"),
        MALAYALAM("ml"),
        ORIYA("or"),
        ASSAMESE("as"),
        TAMIL("ta"),
        URDU("ur");

        private final String code;

        Language(String code) {
        this.code = code;
        }

        public String getCode() {
        return code;
        }
    }

    @Bean
    public LocaleResolver localeResolver() {
        
        List<Locale> supportedLocales = Arrays.stream(Language.values())
            .map(language -> new Locale(language.getCode()))
            .collect(Collectors.toList());
        Locale.setDefault(supportedLocales.get(0));
        return new AcceptHeaderLocaleResolver(){
             @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String headerLang = request.getHeader("Accept-Language");
            return headerLang == null || headerLang.isEmpty()
                    ? Locale.getDefault()
                    : Locale.lookup(Locale.LanguageRange.parse(headerLang), supportedLocales);
        }
        };
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
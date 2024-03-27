package com.darzee.shankh.controller;

import com.darzee.shankh.utils.kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public boolean testResponse() {
        return true;
    }

    @PostMapping(value = "/kafka", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean kafkaProducer(@RequestParam String message) {
        String testTopic = "whatsapp-messaging";
        kafkaProducer.sendMessage(testTopic, message);
        return true;
    }

}

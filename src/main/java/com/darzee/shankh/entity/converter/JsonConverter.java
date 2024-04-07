package com.darzee.shankh.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JsonConverter implements AttributeConverter<JsonNode, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // Handle or log the exception
            return null;
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readTree(dbData);
        } catch (JsonProcessingException e) {
            // Handle or log the exception
            return null;
        }
    }
}
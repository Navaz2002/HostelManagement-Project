package com.hostel.management.serialization;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocalDateSerializer implements JsonSerializer<LocalDate>{

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    /*
     * This method is used to serialize the LocalDate object into JSON
     */
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        try {
            return new JsonPrimitive(date.format(formatter));
        } catch (JsonParseException e) {
            throw e ;
        }
    }
    
}

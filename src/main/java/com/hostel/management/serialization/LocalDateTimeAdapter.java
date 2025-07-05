package com.hostel.management.serialization;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    
    @Override
    /*
     * This method is used to serialize the LocalDateTime object ino JSON
     */
    public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext context) {

        return new JsonPrimitive(dateTime.toString()); // ISO-8601
        
    }

    @Override
    /*
     * This method is used deserialize the LocalDateTime field present in JSON into
     * LocalDateTime object 
     */
    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context)
            throws JsonParseException {

        return LocalDateTime.parse(json.getAsString());

    }
}

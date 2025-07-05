package com.hostel.management.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hostel.management.serialization.LocalDateSerializer;
import com.hostel.management.serialization.LocalDateTimeAdapter;
/*
 * This class is used to provide GSON instances , custom registered GSON instances required for serializing
 * and deserializing.
 * 
 */
public class GsonFactory {

    private static final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateSerializer()).create();

    private static final Gson dateTimeGson = new GsonBuilder()
                                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                                            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer()).create();


    private static final Gson gson = new Gson();

    /*
     * returns the gson instance registered for serialization of LocalDate object.
     */
    public static Gson getGsonBuilder(){
        return GSON_INSTANCE ;
    }
    /*
     * returns the gson instance, can be used anywhere
     * without creating it every time.
     */
    public static Gson getGsonInstance(){
        return gson;
    }
    /*
     * this method returns the GSON
     * instance registered for LocalDateTime
     * for both serializing and deserializing
     */
    public static Gson getDateTimeGson(){
        return dateTimeGson;
    }
}

package com.hostel.management.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
/*
 * This class contains the method which parses the
 * provided String json to JsonElement
 * 
 */
public class JsonParseUtil {
    
    /*
     * this method parses the given json string into JsonElement object
     * 
     * @returns the JsonElement object containing the request json details.
     */
    public static JsonElement parseStringToJsonElement(String jsonString) throws JsonSyntaxException{

        try {
   
            return JsonParser.parseString(jsonString);

        } catch (JsonSyntaxException e) {
            throw e;
        }
    }
}

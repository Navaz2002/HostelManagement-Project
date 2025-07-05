package com.hostel.management.utility;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ExtractParameter {

    /*
     * this exctracts the key value pairs from the provided
     * multiple query 
     * 
     * here pairs[] contains the key values in not usable format.
     * 
     * @returns the map containing key values of query parameters
     */
    public static Map<String, String> extractQueryParam(String[] pairs){

        Map<String, String> map = new HashMap<>();

        for (String  pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                map.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                 URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
            }
        }

        return map;
    }
}

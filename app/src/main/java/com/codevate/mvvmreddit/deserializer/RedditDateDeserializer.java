package com.codevate.mvvmreddit.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Reddit uses timestamps for dates. This deserializer transforms them into Java dates;
 */
public class RedditDateDeserializer implements JsonDeserializer
{
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        return new Date(json.getAsJsonPrimitive().getAsLong() * 1000);
    }
}

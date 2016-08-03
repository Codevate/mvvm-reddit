package com.codevate.mvvmreddit.deserializer;

import android.util.Log;

import com.codevate.mvvmreddit.model.RedditComment;
import com.codevate.mvvmreddit.model.RedditLink;
import com.codevate.mvvmreddit.model.RedditListing;
import com.codevate.mvvmreddit.model.RedditObject;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Deserialize the Reddit object into a subclass based on its 'kind' field.
 */
public class RedditObjectJsonDeserializer implements JsonDeserializer
{
    private static String TAG = "RedditObjectJsonDeserializer";

    @Override
    public RedditObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        if (json == null || !json.isJsonObject())
        {
            return null;
        }

        try
        {
            JsonObject jsonObject = json.getAsJsonObject();
            String kind = jsonObject.get("kind").getAsString();

            return context.deserialize(jsonObject.get("data"), getClassForKind(kind));
        }
        catch (JsonParseException e)
        {
            Log.e(TAG, String.format("Could not deserialize Reddit element: %s", json.toString()));
            return null;
        }
    }

    private Class getClassForKind(String kind)
    {
        switch (kind)
        {
            case "Listing":
                return RedditListing.class;
            case "t1":
                return RedditComment.class;
            case "t3":
                return RedditLink.class;
            default:
                Log.e(TAG, String.format("Unsupported Reddit kind: %s", kind));
                return null;
        }
    }
}

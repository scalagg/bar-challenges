package com.solexgames.challenges.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.solexgames.challenges.tracker.DataTracker;

import java.lang.reflect.Type;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

public class DataTrackerAdapter implements JsonSerializer<DataTracker> {

    @Override
    public JsonElement serialize(DataTracker jsonElement, Type typeOfSrc, JsonSerializationContext context) {
        final JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("className", jsonElement.getClass().getName());
        jsonObject.add("classData", context.serialize(jsonElement));

        return jsonObject;
    }
}

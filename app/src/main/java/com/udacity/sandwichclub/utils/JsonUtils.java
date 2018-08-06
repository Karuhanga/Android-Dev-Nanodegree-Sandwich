package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.udacity.sandwichclub.utils.DebugUtils.TAG;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        JSONObject sandwichObject;
        String mainName, placeOfOrigin, description, image;
        List<String> alsoKnownAs = new ArrayList<>();
        List<String> ingredients = new ArrayList<>();

        try {
            sandwichObject = new JSONObject(json);
            JSONObject name = sandwichObject.getJSONObject("name");

            mainName = name.getString("mainName");

            JSONArray alsoKnownAsList = name.getJSONArray("alsoKnownAs");
            for (int i = 0; i < alsoKnownAsList.length(); i++) {
                alsoKnownAs.add(alsoKnownAsList.getString(i));
            }

            placeOfOrigin = sandwichObject.getString("placeOfOrigin");
            description = sandwichObject.getString("description");
            image = sandwichObject.getString("image");

            JSONArray ingredientsList = sandwichObject.getJSONArray("ingredients");
            for (int i = 0; i < ingredientsList.length(); i++) {
                ingredients.add(ingredientsList.getString(i));
            }

        } catch (JSONException e) {
            Log.e(TAG, "parseSandwichJson", e);
            return null;
        }


        Sandwich sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
        return sandwich;
    }
}

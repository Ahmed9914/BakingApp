package nanodegree.udacity.bakingapp.utils;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import nanodegree.udacity.bakingapp.model.Recipe;

public class NetworkUtils {
    final static String RECIPES_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    public static final Recipe[] getAllRecipes(){
        Recipe[] recipes;
        try {
            recipes = new Gson()
                    .fromJson(getResponseFromHttpUrl(new URL(RECIPES_URL)),
                            Recipe[].class);
            return recipes;
        } catch (Exception e) {
        e.printStackTrace();
        return null;
        }
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

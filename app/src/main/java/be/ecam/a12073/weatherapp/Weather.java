package be.ecam.a12073.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Weather {
    private String weather;
    private double min_temp;
    private double max_temp;

    private static ArrayList<Weather> weathers = new ArrayList<>();

    public Weather(double min_temp, double max_temp) {
        this.min_temp = min_temp;
        this.max_temp = max_temp;

    }

    public static void parse(String json) throws JSONException {
        JSONObject jsonParse = new JSONObject(json);
        JSONArray jsonList = jsonParse.getJSONArray("list");

        for (int i = 0, l = jsonList.length(); i < l; i++) {
            JSONObject jsonWeather = jsonList.getJSONObject(i);

            JSONObject temp = jsonWeather.getJSONObject("temp");
            int min_temp = (int) Math.round(temp.getDouble("min"));
            int max_temp = (int) Math.round(temp.getDouble("max"));
            weathers.add(new Weather(min_temp, max_temp));
        }
    }

    public static Weather find(int index) {
        return weathers.get(index);
    }

    public double getMaxTemp() {
        return max_temp;
    }

    public double getMinTemp() {
        return min_temp;
    }

    public static String[] getMaxTemps() {
        String[] temps = new String[weathers.size()];

        for (int i = 0, l = weathers.size(); i < l; i++) {
            temps[i] = String.valueOf(weathers.get(i).max_temp);
        }

        return temps;
    }

    public String[] getMinTemps() {
        String[] temps = new String[weathers.size()];

        for (int i = 0, l = weathers.size(); i < l; i++) {
            temps[i] = String.valueOf(weathers.get(i).min_temp);
        }

        return temps;
    }

    public static int count(){
        return weathers.size();
    }
}

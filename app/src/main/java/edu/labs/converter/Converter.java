package edu.labs.converter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Converter {
    public static JSONObject convert(char flag, double value, String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(jsonString);
        data = (JSONObject) data.get("Valute");
        JSONObject result = new JSONObject();
        double course, temp;

        switch (flag){
            case 'R':
                result.put("Roubles", value);
                course = Double.parseDouble(((JSONObject) data.get("USD")).get("Value").toString());
                result.put("Dollars", value / course);
                course = Double.parseDouble(((JSONObject) data.get("EUR")).get("Value").toString());
                result.put("Euros", value / course);
                break;
            case 'D':
                result.put("Dollars", value);
                course = Double.parseDouble(((JSONObject) data.get("USD")).get("Value").toString());
                temp = value * course;
                result.put("Roubles", temp);
                course = Double.parseDouble(((JSONObject) data.get("EUR")).get("Value").toString());
                result.put("Euros", temp / course);
                break;
            case 'E':
                result.put("Euros", value);
                course = Double.parseDouble(((JSONObject) data.get("EUR")).get("Value").toString());
                temp = value * course;
                result.put("Roubles", temp);
                course = Double.parseDouble(((JSONObject) data.get("USD")).get("Value").toString());
                result.put("Dollars", temp / course);
                break;
        }

        return result;
    }
}

package edu.labs.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    protected JSONObject convertedValues;
    protected Button button;
    protected Button clearButton;
    protected EditText roubles;
    protected EditText euros;
    protected EditText dollars;
    protected boolean isConverted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        clearButton = findViewById(R.id.clearButton);
        roubles = findViewById(R.id.roubles);
        euros = findViewById(R.id.euros);
        dollars = findViewById(R.id.dollars);
        isConverted = false;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isConverted && (roubles.length() != 0 || dollars.length() != 0 || euros.length() != 0)){
                    new JsonGetter().execute("https://www.cbr-xml-daily.ru/daily_json.js");
                    isConverted = true;
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roubles.setText("");
                dollars.setText("");
                euros.setText("");
                isConverted = false;
            }
        });
    }

    private class JsonGetter extends AsyncTask<String, String , String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder stringBuffer = new StringBuilder();
                String temp;

                while ((temp = reader.readLine()) != null){
                    stringBuffer.append(temp);
                }

                connection.disconnect();
                stream.close();
                reader.close();

                if(roubles.length() != 0)
                    convertedValues = Converter.convert('R', Double.parseDouble(String.valueOf(roubles.getText())), stringBuffer.toString());
                else if(dollars.length() != 0)
                    convertedValues = Converter.convert('D', Double.parseDouble(String.valueOf(dollars.getText())), stringBuffer.toString());
                else if(euros.length() != 0)
                    convertedValues = Converter.convert('E', Double.parseDouble(String.valueOf(euros.getText())), stringBuffer.toString());

                return null;

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            double finalValue = (double) convertedValues.get("Dollars");
            String temp = String.format("%.4f", finalValue);
            dollars.setText(temp);

            finalValue = (double) convertedValues.get("Euros");
            temp = String.format("%.4f", finalValue);
            euros.setText(temp);

            finalValue = (double) convertedValues.get("Roubles");
            temp = String.format("%.4f", finalValue);
            roubles.setText(temp);
        }
    }
}
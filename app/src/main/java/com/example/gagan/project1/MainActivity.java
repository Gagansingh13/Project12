package com.example.gagan.project1;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textViewweatherresult;
    EditText editTextcity;
    String ciyToFind;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextcity = (EditText) findViewById(R.id.editTextCityName);
        textViewweatherresult = (TextView) findViewById(R.id.textViewWeather);

       fab =  (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindWeather(v);
            }
        });


    }
    public void FindWeather(View v){
      ciyToFind =  editTextcity.getText().toString();

        try {

         ExecuteTask tasky = new ExecuteTask();
            tasky.execute("http://api.openweathermap.org/data/2.5/weather?q"+ciyToFind+ "&APPID=418fe6179614b175030baa84f08104ff");

        }
        catch (Exception e)
        {

            e.printStackTrace();
        }



    }

    public class ExecuteTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is  = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);

                int data = reader.read();
                while (data != -1)
                {
                    char current = (char) data ;
                    result += current;
                    data = reader.read();
                }

                return  result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e)
            {

            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(s);

                String infoWeatherToday = jsonObject.getString("weather");
                JSONArray array = new JSONArray(infoWeatherToday);

                for (int i =0 ; i <array.length() ; i++)
                {
                    JSONObject jsonSecoondary = array.getJSONObject(i);
                    String main = "";
                    String description = "";

                    main = jsonSecoondary.getString("main");
                    description = jsonSecoondary.getString("description");

                    if(main != "" && description !="") {
                        message += main + ": " + description + "\r\n";
                    }

                }
                if (message != "")
                {
                    textViewweatherresult.setText(message);

                }

                else {
                    Toast.makeText(MainActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
package com.example.michal.forecast;

import android.content.Context;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final BigDecimal K_TO_F = BigDecimal.valueOf(27315, 2);
    public static final String CITY = "Bratislava";

    JSONObject data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getJSON(CITY);
        TextView textViewCity = (TextView) findViewById(R.id.city);
        textViewCity.setText(CITY);
    }

    public void getJSON(final String city) {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&APPID=c8879f655fad6607a8df2b174b8383bd");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());

                    if(data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }


                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(data!=null){
                    Log.d("my weather received",data.toString());
                    try {
                        JSONArray jsonArray = data.getJSONArray("list");

                        JSONObject day = jsonArray.getJSONObject(0);

                        JSONArray weather = day.getJSONArray("weather");
                        String icon = weather.getJSONObject(0).getString("icon");
                        ImageView weatherImageView = (ImageView) findViewById(R.id.weatherIcon);
                        Context context = weatherImageView.getContext();
                        int id = context.getResources().getIdentifier("w"+icon, "drawable", context.getPackageName());
                        weatherImageView.setImageResource(id);

                        String date = day.getString("dt_txt");
                        TextView dateTextView = (TextView) findViewById(R.id.date);
                        date = dateConvert(date);
                        dateTextView.setText(date);

                        JSONObject main = day.getJSONObject("main");
                        BigDecimal temp = new BigDecimal(main.getString("temp"));
                        temp = temp.subtract(K_TO_F);
                        TextView tempTextView = (TextView) findViewById(R.id.temperature);
                        tempTextView.setText(temp.setScale(0, BigDecimal.ROUND_HALF_UP) + "°C");

                        String clouds = day.getJSONObject("clouds").getString("all");
                        TextView cloudsTextView = (TextView) findViewById(R.id.clouds);
                        cloudsTextView.setText(clouds + "%");

                        JSONObject wind = day.getJSONObject("wind");
                        String direction = degToDirection(wind.getString("deg"));
                        String speed = wind.getString("speed");
                        TextView windTextView = (TextView) findViewById(R.id.wind);
                        windTextView.setText(direction + ", " + speed + "m/s");

                        int row = 1;
                        int col = 1;
                        String oldDate = date;
                        String actualDate = "";
                        id = getResources().getIdentifier("textView" + (2*row-1), "id", getPackageName());
                        dateTextView = (TextView) findViewById(id);
                        dateTextView.setText("Today");
                        id = getResources().getIdentifier("textView" + (2*row), "id", getPackageName());
                        Log.w("id", id+"");
                        dateTextView = (TextView) findViewById(id);
                        dateTextView.setText(date);

                        col = 1;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            day = jsonArray.getJSONObject(i);
                            actualDate = day.getString("dt_txt");
                            Log.w("before date", actualDate);
                            String time = actualDate.substring(actualDate.indexOf(" ")+1);
                            time = time.substring(0, 5);
                            actualDate = dateConvert(actualDate);
                            Log.w("Actual date", actualDate);
                            Log.w("Old date", oldDate);
                            if (actualDate.compareTo(oldDate) != 0) {
                                row++;
                                if (row == 6) {
                                    break;
                                }
                                try {
                                    date = day.getString("dt_txt");
                                    date = date.substring(0, 10);
                                    Date realDate = new SimpleDateFormat( "yyyy-MM-dd" ).parse( date );
                                    DateFormat format2 = new SimpleDateFormat("EEEE");
                                    String finalDay = format2.format(realDate);
                                    id = getResources().getIdentifier("textView" + (2*row-1), "id", getPackageName());
                                    dateTextView = (TextView) findViewById(id);
                                    dateTextView.setText(finalDay);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                id = getResources().getIdentifier("textView" + (2*row), "id", getPackageName());
                                dateTextView = (TextView) findViewById(id);
                                dateTextView.setText(actualDate);
                                col = 1;

                            }
                            id = getResources().getIdentifier("time" + row + "" + col, "id", getPackageName());
                            TextView timeTextView = (TextView) findViewById(id);
                            timeTextView.setText(time);
                            oldDate = actualDate;

                            weather = day.getJSONArray("weather");
                            icon = weather.getJSONObject(0).getString("icon");
                            id = getResources().getIdentifier("timeIcon" + row + "" + col, "id", getPackageName());
                            weatherImageView = (ImageView) findViewById(id);
                            id = getResources().getIdentifier("w"+icon, "drawable", context.getPackageName());
                            weatherImageView.setImageResource(id);

                            main = day.getJSONObject("main");
                            temp = new BigDecimal(main.getString("temp"));
                            temp = temp.subtract(K_TO_F);
                            id = getResources().getIdentifier("timeTemp" + row + "" + col, "id", getPackageName());
                            tempTextView = (TextView) findViewById(id);
                            tempTextView.setText(temp.setScale(0, BigDecimal.ROUND_HALF_UP) + "°C");

                            col++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.execute();
    }

    public String degToDirection(String deg) {
        int degInt = Math.round(Float.parseFloat(deg) / 45);
        Log.w("wind", degInt+"");
        String result = "";
        switch (degInt) {
            case 0:
                result = "N";
                break;
            case 1:
                result = "NE";
                break;
            case 2:
                result = "E";
                break;
            case 3:
                result = "SE";
                break;
            case 4:
                result = "S";
                break;
            case 5:
                result = "SW";
                break;
            case 6:
                result = "W";
                break;
            case 7:
                result = "NW";
                break;
            case 8:
                result = "N";
                break;
        }
        return result;
    }

    public String dateConvert(String date) {
        String year = date.substring(0, 4);
        String month = date.substring(5, 7);
        String day = date.substring(8, 10);
        switch (month) {
            case "1":
                month = "Jan";
                break;
            case "2":
                month = "Feb";
                break;
            case "3":
                month = "Mar";
                break;
            case "4":
                month = "Apr";
                break;
            case "5":
                month = "May";
                break;
            case "6":
                month = "Jun";
                break;
            case "7":
                month = "Jul";
                break;
            case "8":
                month = "Aug";
                break;
            case "9":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;

        }
        return month + " " + day + ", " + year;
    }
}

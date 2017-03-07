package be.ecam.a12073.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class WeatherInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_info);

        TextView min_temp = (TextView) findViewById(R.id.min_temp);
        TextView max_temp = (TextView) findViewById(R.id.max_temp);

        Intent intent = getIntent();
        Weather weather = Weather.find(intent.getIntExtra(Intent.EXTRA_INDEX, 0));

        min_temp.setText(String.valueOf(weather.getMinTemp()));
        max_temp.setText(String.valueOf(weather.getMaxTemp()));
    }
}

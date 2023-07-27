package com.example.implemapi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.implemapi.io.Holiday;
import com.example.implemapi.io.HolidayApiService;
import com.example.implemapi.io.HolidayResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BASE_URL = "https://holidayapi.com/v1/";
    private static final String API_KEY = "c958d90f-1b03-43df-b1ea-3ecfa145b8b8";
    private HolidayApiService holidayApiService;
    private TextView textViewHolidays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        holidayApiService = retrofit.create(HolidayApiService.class);
        textViewHolidays = findViewById(R.id.text_view_holidays);

        Button buttonGetHolidays = findViewById(R.id.button_get_holidays);
        buttonGetHolidays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hacer solicitud API
                Call<HolidayResponse> call = holidayApiService.getHolidays("EC", 2022, API_KEY);
                call.enqueue(new Callback<HolidayResponse>() {
                    @Override
                    public void onResponse(Call<HolidayResponse> call, Response<HolidayResponse> response) {
                        if (response.isSuccessful()) {
                            HolidayResponse holidayResponse = response.body();
                            if (holidayResponse != null) {
                                List<Holiday> holidays = holidayResponse.getHolidays();

                                StringBuilder stringBuilder = new StringBuilder();
                                for (Holiday holiday : holidays) {
                                    stringBuilder.append("Nombre: ").append(holiday.getName()).append("\n");
                                    stringBuilder.append("Fecha: ").append(holiday.getDate()).append("\n\n\n");
                                }
                                textViewHolidays.setText(stringBuilder.toString());
                            }
                        } else {
                            Log.e(TAG, "Ocurrio un error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<HolidayResponse> call, Throwable t) {
                        Log.e(TAG, "Ocurrio un error: " + t.getMessage());
                        System.out.print("holaaaa ");
                    }
                });
            }
        });
    }
}

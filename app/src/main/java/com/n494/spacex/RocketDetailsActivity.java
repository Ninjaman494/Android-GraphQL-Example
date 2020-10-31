package com.n494.spacex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.apollographql.apollo.api.Response;
import com.n494.RocketQuery;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RocketDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_details);

        String id = getIntent().getExtras().getString(EXTRA_ID);

        TextView nameView = findViewById(R.id.rocketDetails_name);
        TextView flightView = findViewById(R.id.rocketDetails_flight);
        TextView costView = findViewById(R.id.rocketDetails_cost);
        TextView descView = findViewById(R.id.rocketDetails_desc);

        Server.fetchRocket(id).subscribeWith(new DisposableObserver<Response<RocketQuery.Data>>() {
            @Override
            public void onNext(@NonNull Response<RocketQuery.Data> dataResponse) {
                if(dataResponse.getData() != null) {
                    RocketQuery.Rocket rocket = dataResponse.getData().rocket();

                    nameView.setText(rocket.name());
                    flightView.setText(String.format("First flight was on %s", rocket.first_flight()));
                    costView.setText(String.format("Cost per flight: $%s", rocket.cost_per_launch()));
                    descView.setText(rocket.description());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
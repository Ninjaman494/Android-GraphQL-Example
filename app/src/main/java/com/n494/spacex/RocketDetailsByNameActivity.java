package com.n494.spacex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apollographql.apollo.api.Response;
import com.n494.RocketQuery;
import com.n494.RocketsQuery;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class RocketDetailsByNameActivity extends AppCompatActivity {

    private final String ROCKET_NAME = "Falcon 9";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_details_by_name);

        TextView nameView = findViewById(R.id.rocketDetails_name);
        TextView flightView = findViewById(R.id.rocketDetails_flight);
        TextView costView = findViewById(R.id.rocketDetails_cost);
        TextView descView = findViewById(R.id.rocketDetails_desc);

        // Show loading
        ProgressBar progressBar = findViewById(R.id.rocketDetails_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Fetch rockets
        Server.fetchRockets()
                // Use the response to fetch the rocket we want
                .concatMap(dataResponse -> {
                    assert dataResponse.getData() != null;

                    List<RocketsQuery.Rocket> rocketList = dataResponse.getData().rockets();
                    for (RocketsQuery.Rocket rocket : rocketList) {
                        if (rocket.name().equals(ROCKET_NAME)) {
                            return Server.fetchRocket(rocket.id());
                        }
                    }

                    return null;
                })
                // Once response is received, handle result
                .subscribeWith(new DisposableObserver<Response<RocketQuery.Data>>() {
                    @Override
                    public void onNext(@NonNull Response<RocketQuery.Data> dataResponse) {
                        assert dataResponse.getData() != null;

                        RocketQuery.Rocket rocket = dataResponse.getData().rocket();

                        nameView.setText(rocket.name());
                        flightView.setText(String.format("First flight was on %s", rocket.first_flight()));
                        costView.setText(String.format("Cost per launch: $%s", rocket.cost_per_launch()));
                        descView.setText(rocket.description());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // Requests have finished, so hide loading bar
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
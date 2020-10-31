package com.n494.spacex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.apollographql.apollo.api.Response;
import com.n494.LaunchPadsQuery;
import com.n494.RocketQuery;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.observers.DisposableObserver;

@SuppressWarnings("ConstantConditions")
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

        RecyclerView recyclerView = findViewById(R.id.rocketDetails_launchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Show loading
        ProgressBar progressBar = findViewById(R.id.rocketDetails_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Fetch Rockets
        ObservableSource<Response<RocketQuery.Data>> observable = Server.fetchRocket(id);

        // Fetch Launchpads, combine the two requests into one observable
        Server.fetchLaunchPads()
                .zipWith(observable, (BiFunction<Response<LaunchPadsQuery.Data>, Response<RocketQuery.Data>, Pair<RocketQuery.Rocket, List<String>>>)
                        (launchPadsResponse, rocketResponse) -> {
                            List<LaunchPadsQuery.Launchpad> launchpads = launchPadsResponse.getData().launchpads();
                            RocketQuery.Rocket rocket = rocketResponse.getData().rocket();

                            String rocketId = rocket.id();
                            ArrayList<String> filteredPads = new ArrayList<>();

                            for (LaunchPadsQuery.Launchpad launchpad : launchpads) {
                                for (LaunchPadsQuery.Vehicles_launched vehicle : launchpad.vehicles_launched()) {
                                    if (vehicle.id().equals(rocketId)) {
                                        filteredPads.add(launchpad.name());
                                    }
                                }
                            }

                            return new Pair<>(rocket, filteredPads);
                        })
                // Once both requests have completed, handle the combined result
                .subscribeWith(new DisposableObserver<Pair<RocketQuery.Rocket, List<String>>>() {
                    @Override
                    public void onNext(@NonNull Pair<RocketQuery.Rocket, List<String>> pair) {
                        RocketQuery.Rocket rocket = pair.first;
                        List<String> launchpads = pair.second;

                        nameView.setText(rocket.name());
                        flightView.setText(String.format("First flight was on %s", rocket.first_flight()));
                        costView.setText(String.format("Cost per launch: $%s", rocket.cost_per_launch()));
                        descView.setText(rocket.description());

                        LaunchPadsAdapter adapter = new LaunchPadsAdapter(launchpads);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        // Both requests have finished, so hide loading bar
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
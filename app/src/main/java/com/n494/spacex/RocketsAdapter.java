package com.n494.spacex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n494.RocketsQuery;

import java.util.List;

class RocketsAdapter extends RecyclerView.Adapter<RocketsAdapter.RocketHolder> {

    private List<RocketsQuery.Rocket> rockets;

    public RocketsAdapter(List<RocketsQuery.Rocket> rockets) {
        this.rockets = rockets;
    }

    @NonNull
    @Override
    public RocketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rocket, parent, false);

        return new RocketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RocketHolder holder, int position) {
        RocketsQuery.Rocket rocket = rockets.get(position);

        holder.rocketName.setText(rocket.name());
        holder.rocketDesc.setText(rocket.description());
    }

    @Override
    public int getItemCount() {
        return rockets.size();
    }

    class RocketHolder extends RecyclerView.ViewHolder {
        TextView rocketName;
        TextView rocketDesc;

        RocketHolder(@NonNull View itemView) {
            super(itemView);

            rocketName = itemView.findViewById(R.id.rocket_name);
            rocketDesc = itemView.findViewById(R.id.rocket_desc);
        }
    }
}

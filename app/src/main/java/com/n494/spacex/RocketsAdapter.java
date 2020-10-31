package com.n494.spacex;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n494.RocketsQuery;

import java.util.List;

class RocketsAdapter extends RecyclerView.Adapter<RocketsAdapter.RocketHolder> {

    private Context context;
    private List<RocketsQuery.Rocket> rockets;

    public RocketsAdapter(List<RocketsQuery.Rocket> rockets, Context context) {
        this.rockets = rockets;
        this.context = context;
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

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, RocketDetailsActivity.class);
            intent.putExtra(RocketDetailsActivity.EXTRA_ID, rocket.id());
            context.startActivity(intent);
        });
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

package com.n494.spacex;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class LaunchPadsAdapter extends RecyclerView.Adapter<LaunchPadsAdapter.LaunchPadHolder>{
    List<String> launchpads;

    LaunchPadsAdapter(List<String> launchpads) {
        this.launchpads = launchpads;
    }

    @NonNull
    @Override
    public LaunchPadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_launchpad, parent, false);

        return new LaunchPadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaunchPadHolder holder, int position) {
        holder.textView.setText(launchpads.get(position));
    }

    @Override
    public int getItemCount() {
        return launchpads.size();
    }

    class LaunchPadHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public LaunchPadHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.launchpad_name);
        }
    }
}

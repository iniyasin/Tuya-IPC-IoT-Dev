package com.kelompokberdua.iotipcdev.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.homemanagement.HomeDetailActivity;
import com.thingclips.smart.home.sdk.bean.HomeBean;

import java.util.ArrayList;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {
    public ArrayList<HomeBean> data = new ArrayList<>();

    @NonNull
    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false));

        holder.ivIcon.setImageResource(R.drawable.ic_next_18);
        holder.itemView.setOnClickListener(v -> {
            // Home Detail
            Intent intent = new Intent(v.getContext(), HomeDetailActivity.class);
            intent.putExtra("homeId", data.get(holder.getAdapterPosition()).getHomeId());
            v.getContext().startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeListAdapter.ViewHolder holder, int position) {
        HomeBean bean = data.get(position);
        holder.tvName.setText(bean.getName());
        holder.ivIcon.setImageResource(0);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            ivIcon = itemView.findViewById(R.id.ivIcon);
        }
    }
}

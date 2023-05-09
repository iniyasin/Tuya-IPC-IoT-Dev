package com.kelompokberdua.iotipcdev.adapter;

import static com.kelompokberdua.iotipcdev.util.Constant.INTENT_DEV_ID;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kelompokberdua.iotipcdev.R;
import com.kelompokberdua.iotipcdev.devicemanagement.DeviceDetail;
import com.thingclips.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {
    public ArrayList<DeviceBean> data = new ArrayList<>();

    @Override
    public DeviceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false));
        holder.itemView.setOnClickListener(v -> {
            // Home Detail
            Intent intent = new Intent(v.getContext(), DeviceDetail.class);
            intent.putExtra(INTENT_DEV_ID, data.get(holder.getAdapterPosition()).devId);
            v.getContext().startActivity(intent);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceBean deviceBean = data.get(position);
        holder.tvDeviceName.setText(deviceBean.name);
        holder.tvDeviceStatus.setText(deviceBean.getIsOnline().toString());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDeviceName;
        private final TextView tvDeviceStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDeviceName = itemView.findViewById(R.id.id_device_name);
            tvDeviceStatus = itemView.findViewById(R.id.id_device_status);
        }
    }
}

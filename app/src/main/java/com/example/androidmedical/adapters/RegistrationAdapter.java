package com.example.androidmedical.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidmedical.R;
import com.example.androidmedical.model.CampaignItem;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.ViewHolder> {

    private List<CampaignItem> campaignList;
    private final boolean isFuture;
    private final OnRegistrationClickListener listener;

    public interface OnRegistrationClickListener {
        void onActionClick(CampaignItem campaign);
        void onDirectionsClick(CampaignItem campaign);
    }

    public RegistrationAdapter(List<CampaignItem> campaignList, boolean isFuture, OnRegistrationClickListener listener) {
        this.campaignList = campaignList;
        this.isFuture = isFuture;
        this.listener = listener;
    }

    public void setCampaignList(List<CampaignItem> campaignList) {
        this.campaignList = campaignList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = isFuture ? R.layout.item_campaign_future : R.layout.item_campaign_history;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CampaignItem item = campaignList.get(position);
        holder.tvTitle.setText(item.getTitle() != null ? item.getTitle() : "Chưa có tiêu đề");
        holder.tvLocation.setText(item.getLocation() != null ? item.getLocation() : "Chưa có địa điểm");
        holder.tvDate.setText(item.getEventDate() != null ? item.getEventDate().replace("T", " ") : "Chưa rõ");

        if (holder.btnRegisterCampaign != null) {
            holder.btnRegisterCampaign.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onActionClick(item);
                }
            });
        }

        if (holder.btnDirections != null) {
            holder.btnDirections.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDirectionsClick(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return campaignList != null ? campaignList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvDate;
        MaterialButton btnRegisterCampaign, btnDirections;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCampaignTitle);
            tvLocation = itemView.findViewById(R.id.tvCampaignLocation);
            tvDate = itemView.findViewById(R.id.tvCampaignDate);
            btnRegisterCampaign = itemView.findViewById(R.id.btnRegisterCampaign);
            btnDirections = itemView.findViewById(R.id.btnDirections);
        }
    }
}

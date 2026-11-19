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

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignViewHolder> {

    private List<CampaignItem> campaignList;
    private final OnCampaignClickListener listener;

    public interface OnCampaignClickListener {
        void onRegisterClick(CampaignItem campaign);
        void onDirectionsClick(CampaignItem campaign);
    }

    public CampaignAdapter(List<CampaignItem> campaignList, OnCampaignClickListener listener) {
        this.campaignList = campaignList;
        this.listener = listener;
    }

    public void setCampaignList(List<CampaignItem> campaignList) {
        this.campaignList = campaignList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CampaignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_campaign, parent, false);
        return new CampaignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CampaignViewHolder holder, int position) {
        CampaignItem item = campaignList.get(position);
        holder.tvTitle.setText(item.getTitle() != null ? item.getTitle() : "Chưa có tiêu đề");
        holder.tvLocation.setText(item.getLocation() != null ? item.getLocation() : "Chưa có địa điểm");
        holder.tvDate.setText(item.getEventDate() != null ? item.getEventDate().replace("T", " ") : "Chưa rõ");

        holder.btnRegister.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRegisterClick(item);
            }
        });

        holder.btnDirections.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDirectionsClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return campaignList != null ? campaignList.size() : 0;
    }

    public static class CampaignViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLocation, tvDate;
        MaterialButton btnRegister, btnDirections;

        public CampaignViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCampaignTitle);
            tvLocation = itemView.findViewById(R.id.tvCampaignLocation);
            tvDate = itemView.findViewById(R.id.tvCampaignDate);
            btnRegister = itemView.findViewById(R.id.btnRegisterCampaign);
            btnDirections = itemView.findViewById(R.id.btnDirections);
        }
    }
}

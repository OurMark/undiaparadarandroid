package itba.undiaparadar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.model.Pledge;
import itba.undiaparadar.model.PledgeStatus;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder> implements View.OnClickListener {
    private final List<Pledge> pledgeList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView positiveActionTitle;
        private TextView dateTime;
        private ImageView hand;
        private ImageView pledgeButton;

        public ViewHolder(View v) {
            super(v);
            positiveActionTitle = (TextView) v.findViewById(R.id.positive_action_title);
            dateTime = (TextView) v.findViewById(R.id.date_time);
            hand = (ImageView) v.findViewById(R.id.pledge_hand);
            pledgeButton = (ImageView) v.findViewById(R.id.pledge_button);
        }
    }

    @Override
    public AchievementsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Pledge pledge = pledgeList.get(position);
        holder.positiveActionTitle.setText(pledge.getPositiveActionTitle());
        if (android.text.format.DateFormat.is24HourFormat(UnDiaParaDarApplication.getAppContext())) {
            holder.dateTime.setText(pledge.getTargetDate());
        } else {
            holder.dateTime.setText(pledge.getTargetDate());
        }
        if (pledge.getDone() == PledgeStatus.NEUTRAL.ordinal()) {
            holder.hand.setImageResource(R.drawable.pledge_hand);
            holder.pledgeButton.setVisibility(View.VISIBLE);
        } else if (pledge.getDone() == PledgeStatus.DONE.ordinal()) {
            holder.hand.setImageResource(R.drawable.pledge_ok);
            holder.pledgeButton.setVisibility(View.GONE);
        } else {
            holder.hand.setImageResource(R.drawable.pledge_fail);
            holder.pledgeButton.setVisibility(View.GONE);
        }
        holder.pledgeButton.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return pledgeList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public void setPledgeList(final List<Pledge> pledgeList) {
        this.pledgeList.clear();
        this.pledgeList.addAll(pledgeList);
        notifyDataSetChanged();
    }
}
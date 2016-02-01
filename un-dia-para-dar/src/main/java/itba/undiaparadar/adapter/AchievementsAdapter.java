package itba.undiaparadar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import itba.undiaparadar.R;
import itba.undiaparadar.UnDiaParaDarApplication;
import itba.undiaparadar.activities.PledgeVerificationActivity;
import itba.undiaparadar.model.Pledge;
import itba.undiaparadar.model.PledgeStatus;
import itba.undiaparadar.services.PledgeService;
import itba.undiaparadar.utils.DateUtils;

public class AchievementsAdapter extends RecyclerView.Adapter<AchievementsAdapter.ViewHolder> {
    private final java.text.DateFormat date12Format = new SimpleDateFormat( "dd/MM/yyy hh:mm:ss aa");
    private final java.text.DateFormat date24Format = new SimpleDateFormat( "dd/MM/yyyy hh:mm:ss");
    @Inject
    private PledgeService pledgeService;
    private final List<Pledge> pledgeList = new ArrayList<>();
    private final Context context;

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

    public AchievementsAdapter(final Context context) {
        UnDiaParaDarApplication.injectMembers(this);
        this.context = context;
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
        final Date endDate = new Date();

        try {
            final Date startDate;
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                startDate = date24Format.parse(pledge.getTargetDate());
                holder.dateTime.setText(pledge.getTargetDate());
            } else {
                startDate = date12Format.parse(pledge.getTargetDate());
                holder.dateTime.setText(pledge.getTargetDate());
            }
            if (DateUtils.daysBetween(endDate, startDate) > 7) {
                pledge.setDone(PledgeStatus.FAILED.ordinal());
                pledgeService.savePledge(pledge);
            }
        } catch (ParseException e) {

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
        holder.pledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(PledgeVerificationActivity.getIntent(context, pledge.getObjectId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return pledgeList.size();
    }

    public void setPledgeList(final List<Pledge> pledgeList) {
        this.pledgeList.clear();
        this.pledgeList.addAll(pledgeList);
        notifyDataSetChanged();
    }

    public void clear() {
        this.pledgeList.clear();
        notifyDataSetChanged();
    }
}
package itba.undiaparadar.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import itba.undiaparadar.R;
import itba.undiaparadar.model.UnDiaParaDarMarker;


public class LastPledgeItemAdapter extends BaseAdapter {

    private final Context context;
    private List<UnDiaParaDarMarker> items;

    public LastPledgeItemAdapter(final Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size() ;
    }

    @Override
    public UnDiaParaDarMarker getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.last_pledge_item, null, false);
            viewHolder = new ViewHolder();

            viewHolder.positiveActionTitle = (TextView) convertView.findViewById(R.id.positive_action_title);
            viewHolder.topicImg = (ImageView) convertView.findViewById(R.id.topic_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initializeViews(getItem(position), viewHolder);
        return convertView;
    }

    private void initializeViews(final UnDiaParaDarMarker unDiaParaDarMarker, final ViewHolder holder) {
        holder.positiveActionTitle.setText(unDiaParaDarMarker.getTopic().getName());
        holder.topicImg.setImageResource(unDiaParaDarMarker.getTopic().getEnableImageResId());
    }

    protected class ViewHolder {
        TextView positiveActionTitle;
        ImageView topicImg;
    }

    public void setItems(final List<UnDiaParaDarMarker> items) {
        this.items = new ArrayList<>();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}

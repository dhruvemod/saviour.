package decodertech.com.saviour;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Dhruve on 9/18/2017.
 */

public class CustomAdapter extends ArrayAdapter<contact> {
    public CustomAdapter(Context context, List<contact> contacts) {
        super(context, 0, contacts);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.emergenvy_contact_adapter, parent, false);
        }
        contact c = getItem(position);
        TextView websiteTextView = (TextView) listItemView.findViewById(R.id.Name);
        websiteTextView.setText(c.getPerson_name());
        TextView adType = (TextView) listItemView.findViewById(R.id.number);
        adType.setText(c.getNumber());
        TextView startTime = (TextView) listItemView.findViewById(R.id.location);
        startTime.setText(c.getLoc());
        return listItemView;
    }
}

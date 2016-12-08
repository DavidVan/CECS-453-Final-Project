package edu.csulb.suitup;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nhut on 12/4/2016.
 */

public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.imageTitle);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        new AsyncTask<ViewHolder, Void, ImageItem>()
        {
            private ViewHolder v;

            @Override
            protected ImageItem doInBackground(ViewHolder... viewHolders) {
                v = viewHolders[0];
                ImageItem item = (ImageItem) data.get(position);
                return item;
            }

            @Override
            protected void onPostExecute(ImageItem result)
            {
                v.image.setImageBitmap(result.getImage());
                v.imageTitle.setText( result.getCategory() + " " + Integer.toString(position));
                System.out.println(result.getId());
            }
        }.execute(holder);



        //holder.imageTitle.setText(item.getTitle());
        //holder.image.setImageBitmap(item.getImage());
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}

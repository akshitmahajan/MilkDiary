package com.manaksh.milkdiary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import manaksh.com.milkdiary.R;

/**
 * Created by akshmaha on 1/5/2016.
 */
public class ReportsAdapter extends BaseAdapter{

    public Integer[] mThumbIds = {
            0, R.drawable.orange, R.drawable.blue, R.drawable.yellow, R.drawable.milkman,
            R.drawable.check, R.string.zero, R.string.zero, R.string.zero, R.string.zero,
            R.drawable.cross, 0, 0, 0, 1
    };

    // Map each state to its graphics.
    //private Map<Integer, Integer> mStateResources = new HashMap<Integer, Integer>();
    private Context mContext;

    public ReportsAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(160, 160));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(10, 10, 10, 10);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
}

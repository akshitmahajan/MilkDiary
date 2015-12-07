package com.manaksh.milkdiary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import manaksh.com.milkdiary.R;

/**
 * Created by akshmaha on 12/6/2015.
 */
public class ImageAdapter extends BaseAdapter {
    // references to our images
    public Integer[] mThumbIds = {
            R.drawable.orange, R.drawable.blue,
            R.drawable.yellow, R.drawable.milkman,
            R.drawable.one_0, R.drawable.one_0,
            R.drawable.one_0, R.drawable.one_0,
            R.drawable.onefive_0, R.drawable.onefive_0,
            R.drawable.onefive_0, R.drawable.onefive_0,
            R.drawable.two_0, R.drawable.two_0,
            R.drawable.two_0, R.drawable.two_0,
            R.drawable.twofive_0, R.drawable.twofive_0,
            R.drawable.twofive_0, R.drawable.twofive_0,
            R.drawable.three_0, R.drawable.three_0,
            R.drawable.three_0, R.drawable.three_0
    };

    // Map each state to its graphics.
    //private Map<Integer, Integer> mStateResources = new HashMap<Integer, Integer>();
    private Context mContext;

    public ImageAdapter(Context c) {
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

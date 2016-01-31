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
            R.mipmap.orange, R.mipmap.blue,
            R.mipmap.yellow, R.mipmap.milkman,
            R.mipmap._1_0_steady, R.mipmap._1_0_steady,
            R.mipmap._1_0_steady, R.mipmap._1_0_steady,
            R.mipmap._1_5_steady, R.mipmap._1_5_steady,
            R.mipmap._1_5_steady, R.mipmap._1_5_steady,
            R.mipmap._2_0_steady, R.mipmap._2_0_steady,
            R.mipmap._2_0_steady, R.mipmap._2_0_steady,
            R.mipmap._2_5_steady, R.mipmap._2_5_steady,
            R.mipmap._2_5_steady, R.mipmap._2_5_steady,
            R.mipmap._3_0_steady, R.mipmap._3_0_steady,
            R.mipmap._3_0_steady, R.mipmap._3_0_steady
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
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5,5,5,5);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }
}

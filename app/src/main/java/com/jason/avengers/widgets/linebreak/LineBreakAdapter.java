package com.jason.avengers.widgets.linebreak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jason.avengers.R;

import java.util.List;
import java.util.Observable;

/**
 * Created by jason on 2018/4/4.
 */

public class LineBreakAdapter extends Observable {

    private final LayoutInflater mInflater;
    private List<String> mDatas;
    private LineBreakListener mListener;

    public LineBreakAdapter(Context context, List<String> datas, LineBreakListener listener) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mListener = listener;
    }

    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public View getView(final int position) {
        if (mDatas != null) {
            final String data = mDatas.get(position);
            TextView tv = (TextView) mInflater.inflate(R.layout.layout_item_line_break, null);
            tv.setText(data);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onClick(view, position, data);
                }
            });
            return tv;
        } else {
            return null;
        }
    }

    public void notifyObserversByData(List<String> datas) {
        mDatas = datas;
        setChanged();
        notifyObservers();
    }
}

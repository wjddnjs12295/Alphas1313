package com.example.tmap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class clickcustomView extends View {
    View v;

    public clickcustomView(Context context, String s) {
        super(context);
        initView(s);
    }

    private void initView(String s) {
        Log.d("HI", "HI");
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        v = layoutInflater.inflate(R.layout.bubble_popup2, null, false);
        Log.d("###", "" + v);
        TextView t = (TextView) v.findViewById(R.id.bubble_title);
        t.setText(s);
        Log.d("!!!", "" + t.getText());
    }

    protected View getView() {
        return v;
    }

}

package com.example.root.cryptofx.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.cryptofx.R;

public class CountryAdapter extends BaseAdapter{

    Context context;
    private String[] countryNames;
    private int flags[];
    private LayoutInflater inflater;

    public CountryAdapter(Context applicationContext, int[] flags, String[] countryNames) {
        this.context = applicationContext;
        this.flags = flags;
        this.countryNames = countryNames;
        inflater = (LayoutInflater.from(applicationContext));

    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.row_fiat_currency, viewGroup, false);
        ImageView flag_icon = (ImageView) view.findViewById(R.id.imageView);
        TextView country_name = (TextView) view.findViewById(R.id.textView);
        flag_icon.setImageResource(flags[i]);
        country_name.setText(countryNames[i]);

        return view;
    }
}

package com.example.root.cryptofx.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.root.cryptofx.R;

public class CryptoAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private String[] cryptoNames;
    private int cryptoLogo[];

    public CryptoAdapter(Context cryptoContext, int[] cryptoLogos, String[] cryptoNames) {
        this.context = cryptoContext;
        this.cryptoLogo = cryptoLogos;
        this.cryptoNames = cryptoNames;
        inflater = (LayoutInflater.from(cryptoContext));
    }

    @Override
    public int getCount() {
        return cryptoLogo.length;
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

        view = inflater.inflate(R.layout.row_crypto_currency, viewGroup, false);
        ImageView cryptoIcon = (ImageView) view.findViewById(R.id.cryptoLogo_row);
        TextView cryptoName = (TextView) view.findViewById(R.id.cryptoName_row);

        cryptoIcon.setImageResource(cryptoLogo[i]);
        cryptoName.setText(cryptoNames[i]);

        return view;
    }
}

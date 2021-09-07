package com.testing.testung.adapter;

import android.content.Context;
import android.database.Cursor;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testing.testung.R;

public class SearchAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SearchView searchView;


    public SearchAdapter(Context context, Cursor cursor, SearchView sv) {
        super(context, cursor, false);
        mContext = context;
        searchView = sv;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.item_search_custom, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        String deal = cursor.getString(cursor.getColumnIndexOrThrow("search"));
        String cashback = cursor.getString(cursor.getColumnIndexOrThrow("price"));

        TextView dealsTv = (TextView) view.findViewById(R.id.tv_deal);
        dealsTv.setText(deal);

        TextView cashbackTv = (TextView) view.findViewById(R.id.tv_cashback);
        if(cashback.equals("null")) {
            cashbackTv.setText("FREE");
        }else {
            cashbackTv.setText("PREMIUM");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //take next action based user selected item
                TextView dealText = (TextView) view.findViewById(R.id.tv_deal);
                searchView.setIconified(true);
                searchView.setQuery(dealText.getText(),true);
            }
        });

    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }
}

package com.testing.testung.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.testing.testung.R;

public class MakeStickerSelf extends Fragment {
    public static final String ARGKODE="dsad";
    static BillingProcessor bp;

    public static MakeStickerSelf newInisiate(int section,BillingProcessor billingProcessor) {
        bp=billingProcessor;
        MakeStickerSelf makeStickerSelf = new MakeStickerSelf();
        Bundle arg = new Bundle();
        arg.putInt(ARGKODE,section);
        return makeStickerSelf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.make_sticker_fragment,container,false);
        init(rootview);
        return rootview;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_create_app_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    Button donate;
    void init(View view){
        donate=view.findViewById(R.id.donate_me);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               bp.purchase(getActivity(),"donate");
            }
        });
    }
}

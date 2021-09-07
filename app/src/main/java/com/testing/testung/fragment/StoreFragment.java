package com.testing.testung.fragment;

import android.app.Activity;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.orhanobut.hawk.Hawk;
import com.testing.testung.MainActivity;
import com.testing.testung.R;
import com.testing.testung.model.Sticker;
import com.testing.testung.model.StickerPack;
import com.testing.testung.adapter.SearchAdapter;
import com.testing.testung.adapter.StickerAdapter;
import com.testing.testung.model.StickerModel;
import com.testing.testung.task.GetStickers;
import com.testing.testung.task.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoreFragment extends Fragment implements GetStickers.Callbacks,SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener{
    static final String ARGKODE="dsad";

    SearchAdapter mAdapter;

    List<Sticker> mStickers;
    List<String> mEmojis,mDownloadFiles;
    static ArrayList<StickerPack> stickerPacks = new ArrayList<>();
    static StickerAdapter adapter;
    static RecyclerView recyclerView;
    static SwipeRefreshLayout swipeRefreshLayout;
    String android_play_store_link;
    ArrayList<StickerModel> stickerModels = new ArrayList<>();

    SearchView searchView;

    @Override
    public void onListLoaded(String jsonResult, boolean jsonSwitch) {
        try {
            if (jsonResult != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonResult);
                    android_play_store_link = jsonResponse.getString("android_play_store_link");
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("sticker_packs");
                    Log.d("DSUI", "onListLoaded: " + jsonMainNode.length());
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        String leo = "";
                        try {
                            leo=jsonChildNode.getString("sku");
                        }catch (JSONException t){
                            leo="";
                        }

                        Log.d("H+DSUI", "onListLoaded: " + jsonChildNode.getString("name"));
                        stickerPacks.add(new StickerPack(
                                jsonChildNode.getString("identifier"),
                                jsonChildNode.getString("name"),
                                jsonChildNode.getString("publisher"),
                                MainActivity.getLastBitFromUrl(jsonChildNode.getString("tray_image_file")).replace(" ","_"),
                                jsonChildNode.getString("tray_image_file").replace(" ","_"),
                                jsonChildNode.getString("publisher_email"),
                                jsonChildNode.getString("publisher_website"),
                                jsonChildNode.getString("privacy_policy_website"),
                                jsonChildNode.getString("license_agreement_website"),
                                leo

                        ));
                        JSONArray stickers = jsonChildNode.getJSONArray("stickers");
                        Log.d("DSUI", "onListLoaded: " + stickers.length());
                        for (int j = 0; j < stickers.length(); j++) {
                            JSONObject jsonStickersChildNode = stickers.getJSONObject(j);
                            mStickers.add(new Sticker(
                                    MainActivity.getLastBitFromUrl(jsonStickersChildNode.getString("image_file")).replace(".png",".webp"),
                                    jsonStickersChildNode.getString("image_file").replace(".png",".webp"),
                                    mEmojis
                            ));
                            mDownloadFiles.add(jsonStickersChildNode.getString("image_file"));
                        }
                        //Log.d(TAG, "onListLoaded: " + mStickers.size());
                        Hawk.put(jsonChildNode.getString("identifier"), mStickers);
                        stickerPacks.get(i).setStickers(Hawk.get(jsonChildNode.getString("identifier"),new ArrayList<Sticker>()));
                        /*stickerModels.add(new StickerModel(
                                jsonChildNode.getString("name"),
                                mStickers.get(0).imageFileName,
                                mStickers.get(1).imageFileName,
                                mStickers.get(2).imageFileName,
                                mStickers.get(2).imageFileName,
                                mDownloadFiles
                        ));*/
                        mStickers.clear();
                    }
                    Hawk.put("sticker_packs",stickerPacks);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Collections.sort(stickerPacks, new Comparator<StickerPack>() {
                    @Override
                    public int compare(StickerPack o1, StickerPack o2) {
                        return o1.name.compareToIgnoreCase(o2.name);
                    }
                });

                NganuDataSugesti(stickerPacks);

                Update(stickerPacks);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static BillingProcessor inventory;
    public static void RefreshMe(Activity activity){
        Update(stickerPacks,activity);
    }
    public static StoreFragment newInisiate(int section, BillingProcessor in){
        StoreFragment storeFragment = new StoreFragment();
        Bundle arg = new Bundle();
        inventory =in;
        arg.putInt(ARGKODE,section);
        return storeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.store_fragment,container,false);
        init(rootview);
        Log.d("DSIU", "getItem: ");
        return rootview;
    }

    void init(View view){
        stickerPacks = new ArrayList<>();
        mStickers = new ArrayList<>();
        stickerModels = new ArrayList<>();
        mEmojis = new ArrayList<>();
        mDownloadFiles = new ArrayList<>();
        mEmojis.add("");

        new GetStickers(getActivity(), this, "https://dl.dropboxusercontent.com/s/2aoswz50unox4zl/wastickercoupleloveuniquedevasset.json").execute();
        recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout=view.findViewById(R.id.swipe_home_store);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        final String[] search= new String[] {"search"};
        final int[] to = new int[] {android.R.id.text1};


        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
        mAdapter = new SearchAdapter(getActivity(),c,searchView);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setIconifiedByDefault(false);


        int gyui = Utility.calculateNoOfColumns(getActivity(),100);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), gyui);
        recyclerView.setLayoutManager(gridLayoutManager);

    }


    void Update(ArrayList<StickerPack> stickers){

        adapter = new StickerAdapter(getActivity(), stickers,inventory);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    static void Update(ArrayList<StickerPack> stickers, Activity activity){
        try {
            adapter = new StickerAdapter(activity, stickers,inventory);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }catch (NullPointerException ignore){

        }

    }

    @Override
    public void onRefresh() {
        Update(stickerPacks);
    }



    @Override
    public boolean onQueryTextSubmit(String s) {
        ArrayList<StickerPack> stickerModelsClone = new ArrayList<>();
        for (StickerPack stickerPack : stickerPacks){
            if(stickerPack.identifier.contains(s.toLowerCase())){
                stickerModelsClone.add(stickerPack);
            }else if(stickerPack.name.contains(s)){
                stickerModelsClone.add(stickerPack);
            }else if(stickerPack.publisher.contains(s)){
                stickerModelsClone.add(stickerPack);
            }
        }
        Update(stickerModelsClone);
        return false;
    }

    private String[] SUGGESTIONS ={"null"};
    private String[] SUGGESTIONSPRICE ={"k"};

    void NganuDataSugesti(ArrayList<StickerPack> stickerPacksd){
        String nuhun="";
        String nuhun2="";
        String code = "%39323%";
        for (StickerPack stickerPack : stickerPacksd){
            nuhun +=stickerPack.name+code;
            if(stickerPack.sku.equals("")) {
                nuhun2 += "null" + code;
            }else {
                nuhun2 += stickerPack.sku + code;
            }
        }
        SUGGESTIONS=nuhun.split(code);
        SUGGESTIONSPRICE = nuhun2.split(code);
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<StickerPack> stickerModelsClone = new ArrayList<>();
        for (StickerPack stickerPack : stickerPacks){
            if(stickerPack.identifier.contains(s.toLowerCase())){
                stickerModelsClone.add(stickerPack);
            }else if(stickerPack.name.contains(s)){
                stickerModelsClone.add(stickerPack);
            }else if(stickerPack.publisher.contains(s)){
                stickerModelsClone.add(stickerPack);
            }
        }
        Update(stickerModelsClone);
        populateAdapter(s);
        return false;
    }
    MatrixCursor c;
    private void populateAdapter(String query) {
        c = new MatrixCursor(new String[]{ BaseColumns._ID, "search","price" });
        for (int i=0; i<SUGGESTIONS.length; i++) {
            if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
                c.addRow(new Object[] {i, SUGGESTIONS[i],SUGGESTIONSPRICE[i]});
        }
        mAdapter.changeCursor(c);
    }
}

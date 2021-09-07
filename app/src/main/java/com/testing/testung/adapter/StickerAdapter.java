package com.testing.testung.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.bumptech.glide.Glide;
import com.testing.testung.model.StickerPack;
import com.testing.testung.activity.StickerDetailsActivity;
import com.testing.testung.MainActivity;
import com.testing.testung.R;
import com.testing.testung.model.Sticker;
import com.testing.testung.model.Shop;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    Context context;
    ArrayList<com.testing.testung.model.StickerPack> StickerPack;
    BillingProcessor inventory;

    public StickerAdapter(Context context, ArrayList<StickerPack> StickerPack,BillingProcessor inventory) {
        this.context = context;
        this.StickerPack = StickerPack;
        this.inventory = inventory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sticker, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final List<Sticker> models = StickerPack.get(i).getStickers();

        viewHolder.name.setText(StickerPack.get(i).name);
        viewHolder.publiser.setText(StickerPack.get(i).publisher);
        Log.d("TATAS", "onBindViewHolder: "+StickerPack.get(i).trayImageFileLink.replace(".webp",".png"));
        Glide.with(context)
                .load(StickerPack.get(i).trayImageFileLink.replace(".webp",".png"))
                .into(viewHolder.imone);

        if(StickerPack.get(i).sku==null|| StickerPack.get(i).sku.equals("")||ImFreeAcces(StickerPack.get(i))){
            viewHolder.price.setText("FREE");
        }else {
            viewHolder.price.setText("0");
            try {
                String price = inventory.getPurchaseListingDetails(StickerPack.get(i).sku).priceText;
                if (price != null) {
                    viewHolder.price.setText(price);
                }
            }catch (NullPointerException n){

            }
        }


        //final File file = new File(MainActivity.path + "/" + StickerPack.get(i).identifier + "/" + models.get(0).imageFileName);
        viewHolder.SetListenerClick(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProgressDialog progressDialog=new ProgressDialog(context);
                progressDialog.setMessage("please wait..");
                progressDialog.show();
                if(StickerPack.get(i).sku==null|| StickerPack.get(i).sku.equals("")||ImFreeAcces(StickerPack.get(i))) {
                    final InterstitialAd mInterstitialAd;
                    mInterstitialAd = new InterstitialAd(context);
                    mInterstitialAd.setAdUnitId(MainActivity.INTERST);
                    mInterstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdFailedToLoad(int g) {
                            progressDialog.dismiss();
                            StickerDetailsActivity.StartActivity(v, context, StickerPack.get(i));
                            super.onAdFailedToLoad(g);
                        }
                        @Override
                        public void onAdLoaded() {
                            progressDialog.dismiss();
                            mInterstitialAd.show();

                            super.onAdLoaded();
                        }
                        @Override
                        public void onAdClosed() {
                            // Code to be executed when when the interstitial ad is closed.
                            StickerDetailsActivity.StartActivity(v, context, StickerPack.get(i));
                            super.onAdClosed();
                        }
                    });
                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("782F9439603AB89983CAFABD9C59C411").build());

                }else {

                    String hul = inventory.getPurchaseListingDetails(StickerPack.get(i).sku).priceText;
                    StickerDetailsActivity.StartActivity(v, context, StickerPack.get(i), hul);
                }
            }
        });

    }

    boolean ImFreeAcces(StickerPack stickerPack){
        if(MainActivity.getUserShop()==null)return false;
        for (Shop shop : MainActivity.getUserShop()){
            Log.d("FAInn", "ImFreeAcces: "+shop.sku+" katon "+stickerPack.sku);
            if(shop.sku!=null) {
                if (shop.sku.equals(stickerPack.sku)) {

                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return StickerPack.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,publiser,price;
        ImageView imone;
        CardView cardView;
        View.OnClickListener view;

        public void SetListenerClick(View.OnClickListener v){
            this.view=v;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            publiser = itemView.findViewById(R.id.rv_sticker_publisher);
            name = itemView.findViewById(R.id.rv_sticker_name);
            imone = itemView.findViewById(R.id.sticker_one);
            cardView = itemView.findViewById(R.id.card_view);
            price = itemView.findViewById(R.id.rv_sticker_price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.onClick(v);
                }
            });
        }
    }
}

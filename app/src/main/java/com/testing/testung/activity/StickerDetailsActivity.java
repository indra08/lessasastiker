package com.testing.testung.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.testing.testung.model.StickerPack;
import com.testing.testung.BuildConfig;
import com.testing.testung.MainActivity;
import com.testing.testung.R;
import com.testing.testung.model.Sticker;
import com.testing.testung.adapter.StickerDetailsAdapter;
import com.testing.testung.model.Shop;
import com.testing.testung.task.Utility;
import com.testing.testung.task.WhitelistCheck;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.testing.testung.MainActivity.EXTRA_STICKERPACK;
import static com.testing.testung.MainActivity.EXTRA_STICKERPACK_RAW;
import static com.testing.testung.MainActivity.EXTRA_STICKER_PACK_AUTHORITY;
import static com.testing.testung.MainActivity.EXTRA_STICKER_PACK_ID;
import static com.testing.testung.MainActivity.EXTRA_STICKER_PACK_NAME;

public class StickerDetailsActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    static BillingProcessor bp;
    void Pembelian() {
        String judsa = "";
        bp = new BillingProcessor(this, judsa, this);
        bp.initialize();
    }

    private final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    final int WRITEEXTCODE =1;
    private static final int ADD_PACK = 200;
    private static final String TAG = "TATAS";
    StickerPack stickerPack,rawSticker;
    StickerDetailsAdapter adapter;
    Toolbar toolbar;
    RecyclerView recyclerView;
    List<Sticker> ourwastickerxo;
    ArrayList<String> strings;
    public static String path;
    Button addtowhatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Pembelian();
        setContentView(R.layout.activity_sticker_details);

        MobileAds.initialize(this, MainActivity.APPID);
        RelativeLayout sww = findViewById(R.id.adView_2);
        final AdView mAdView = new AdView(this);
        mAdView.setAdUnitId(MainActivity.BANNER);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        sww.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("782F9439603AB89983CAFABD9C59C411").build();
        mAdView.loadAd(adRequest);


        stickerPack = getIntent().getParcelableExtra(MainActivity.EXTRA_STICKERPACK);
        rawSticker = getIntent().getParcelableExtra(MainActivity.EXTRA_STICKERPACK_RAW);
        toolbar = findViewById(R.id.toolbar);
        addtowhatsapp = findViewById(R.id.add_to_whatsapp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(stickerPack.name);
        getSupportActionBar().setSubtitle(stickerPack.sku);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.recyclerView);
        ourwastickerxo = stickerPack.getStickers();
        strings = new ArrayList<>();
        path = getFilesDir() + "/" + "stickers_asset" + "/" + stickerPack.identifier + "/";
        File file = new File(path + ourwastickerxo.get(0).imageFileNameLink);

        Log.d(TAG, "onCreate: MASUKDD"+stickerPack.sku+"\n"+ourwastickerxo.get(0).imageFileName+"\n"+ourwastickerxo.get(0).imageFileNameLink);
        if(file.exists()) {
            Log.d(TAG, "onCreate: " + path + ourwastickerxo.get(0).imageFileNameLink);
            for (Sticker s : ourwastickerxo) {
                if (!file.exists()) {
                    strings.add(s.imageFileNameLink);
                } else {
                    strings.add(path + s.imageFileNameLink);
                }
            }

            addtowhatsapp.setText("Add To Whatsapp");
            addtowhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddToWa();
                }
            });
        }
        else if(masalah!=null){
            //pembelian appa
            ourwastickerxo = rawSticker.getStickers();
            for (Sticker s : ourwastickerxo) {
                String av = s.imageFileNameLink.replace(".webp",".png");
                strings.add(av);
            }
            final Shop shop = new Shop(stickerPack.name,stickerPack.sku,stickerPack.identifier);
            addtowhatsapp.setText(masalahHarga);
            addtowhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        if(BPREADY) {
                            addtowhatsapp.setEnabled(false);
                            bp.purchase(StickerDetailsActivity.this, masalah);
                            //Barawea
                        }
                }
            });
        }
        else {
            ourwastickerxo = rawSticker.getStickers();
            for (Sticker s : ourwastickerxo) {
                // ini perlu ganti mas tempat stiker
                String av = s.imageFileNameLink.replace(".webp",".png");
                strings.add(av);
            }
            addtowhatsapp.setText("Download Pack");
            addtowhatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addtowhatsapp.setEnabled(false);
                    ProgressDialog progressDialog = new ProgressDialog(StickerDetailsActivity.this);
                    progressDialog.setMessage("download "+rawSticker.name);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    getPermissione(progressDialog);
                }
            });
        }

        adapter = new StickerDetailsAdapter(strings, this);
        int gyui = Utility.calculateNoOfColumns(this,80);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, gyui);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

    }


    Boolean BPREADY = false;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: "+requestCode+
                "\n" + permissions+
                "\n"+grantResults);
        if(requestCode==WRITEEXTCODE){



        }
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Shop shop = new Shop(stickerPack.name,productId,stickerPack.identifier,details.purchaseInfo.purchaseData.purchaseTime.toString());
        UpdateShop(shop);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        BPREADY=true;
    }

    class Download extends AsyncTask<Boolean,Void,Boolean> {
        StickerPack url;
        int a=0;
        ProgressDialog progressDialog;
        public Download(StickerPack stickers, ProgressDialog progressDialog){
            this.url=stickers;
            this.progressDialog=progressDialog;

        }

        @Override
        protected Boolean doInBackground(Boolean... voids) {

            for (final Sticker s : url.getStickers()) {
                String av = s.imageFileNameLink.replace(".webp",".png");
                Log.d("TATAS", "onResourceReady Image File: "+s.imageFileNameLink.replace(".webp",".png"));
                Glide.with(getBaseContext())
                        .asBitmap()
                        .apply(new RequestOptions().override(512, 512))
                        .load(Uri.parse(av))
                        .addListener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                Bitmap bitmap1 = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
                                Matrix matrix = new Matrix();
                                Canvas canvas = new Canvas(bitmap1);
                                canvas.drawColor(Color.TRANSPARENT);
                                matrix.postTranslate(
                                        canvas.getWidth() / 2 - resource.getWidth() / 2,
                                        canvas.getHeight() / 2 - resource.getHeight() / 2
                                );
                                canvas.drawBitmap(resource, matrix, null);
                                MainActivity.SaveImage(bitmap1, s.imageFileName, url.identifier);

                                a+=1;
                                float yuy = a/url.getStickers().size();
                                int persenan = (int) (yuy*100);
                                progressDialog.setMessage("download "+url.name+" "+persenan+"%");
                               // addtowhatsapp.setText("Download "+persenan+"%");
                                if(url.getStickers().size()==a){
                                    Log.d("TATAS", "onResourceReady: "+url.trayImageFile);
                                    Glide.with(getBaseContext())
                                            .asBitmap()
                                     //Lucinta Monyet
                                            .load(url.trayImageFileLink)
                                            .addListener(new RequestListener<Bitmap>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                                    Bitmap bitmap1 = getResizedBitmap(resource,96,96) ;
                                             //       Matrix matrix = new Matrix();
                                               //     Canvas canvas = new Canvas(bitmap1);
                                                 //   canvas.drawColor(Color.TRANSPARENT);
                                             //       matrix.postTranslate(
                                               //             canvas.getWidth() / 2 - resource.getWidth() / 2,
                                                   //         canvas.getHeight() / 2 - resource.getHeight() / 2
                                                 //   );
                                                   // canvas.drawBitmap(resource, matrix, null);
                                                    MainActivity.SaveTryImage(bitmap1,url.trayImageFile,url.identifier);
                                                    //complite
                                                    progressDialog.dismiss();
                                                    addtowhatsapp.setText("Add To Whatsapp");
                                                    addtowhatsapp.setEnabled(true);
                                                    stickerPack = rawSticker;
                                                    addtowhatsapp.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            AddToWa();
                                                        }
                                                    });
                                                    final ProgressDialog progressDialog=new ProgressDialog(StickerDetailsActivity.this);
                                                    progressDialog.setMessage("please wait..");
                                                    progressDialog.show();
                                                    final InterstitialAd mInterstitialAd;
                                                    mInterstitialAd = new InterstitialAd(StickerDetailsActivity.this);
                                                    mInterstitialAd.setAdUnitId(MainActivity.INTERST);
                                                    mInterstitialAd.setAdListener(new AdListener(){
                                                        @Override
                                                        public void onAdFailedToLoad(int i) {
                                                            progressDialog.dismiss();
                                                            super.onAdFailedToLoad(i);
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
                                                            super.onAdClosed();
                                                        }
                                                    });
                                                    mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("782F9439603AB89983CAFABD9C59C411").build());

                                                    return true;
                                                }
                                            })
                                            .submit();

                                }
                                Log.d("MARJAN", "onResourceReady: "+a);
                                return true;
                            }
                        }).submit();
            }
            return true;
        }
    }
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private InterstitialAd mInterstitialAd;

    void AddToWa(){
//        Shop shop =new Shop(stickerPack.name,stickerPack.sku,stickerPack.identifier);
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user!=null) {
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("shops").child(stickerPack.identifier);
//            reference.setValue(shop);
//        }
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("serve intent..");
        progressDialog.show();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(MainActivity.INTERST);
        final Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra(EXTRA_STICKER_PACK_ID, stickerPack.identifier);
        intent.putExtra(EXTRA_STICKER_PACK_AUTHORITY, BuildConfig.CONTENT_PROVIDER_AUTHORITY);
        intent.putExtra(EXTRA_STICKER_PACK_NAME, stickerPack.name);
        Log.d("TATAS", "AddToWa: "+stickerPack.name);
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                progressDialog.dismiss();
                try {
                    startActivityForResult(intent, ADD_PACK);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(StickerDetailsActivity.this, "error please update whatsapp current version", Toast.LENGTH_LONG).show();
                }
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                progressDialog.dismiss();
                try {
                    startActivityForResult(intent, ADD_PACK);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(StickerDetailsActivity.this, "error please update whatsapp current version", Toast.LENGTH_LONG).show();
                }
                super.onAdLoaded();
            }
        });
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("782F9439603AB89983CAFABD9C59C411").build());

    }

    void getPermissione(ProgressDialog progressDialog){
        int perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perm != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    WRITEEXTCODE
            );
        }else {

            new Download(rawSticker,progressDialog).execute();
        }
    }

    static class WhiteListCheckAsyncTask extends AsyncTask<StickerPack, Void, Boolean> {
        private final WeakReference<StickerDetailsActivity> stickerPackDetailsActivityWeakReference;

        WhiteListCheckAsyncTask(StickerDetailsActivity stickerPackListActivity) {
            this.stickerPackDetailsActivityWeakReference = new WeakReference<>(stickerPackListActivity);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected final Boolean doInBackground(StickerPack... stickerPacks) {
            StickerPack stickerPack = stickerPacks[0];
            final StickerDetailsActivity stickerPackDetailsActivity = stickerPackDetailsActivityWeakReference.get();
            //noinspection SimplifiableIfStatement
            if (stickerPackDetailsActivity == null) {
                return false;
            }
            return WhitelistCheck.isWhitelisted(stickerPackDetailsActivity, stickerPack.identifier);
        }

        @Override
        protected void onPostExecute(Boolean isWhitelisted) {
            final StickerDetailsActivity stickerPackDetailsActivity = stickerPackDetailsActivityWeakReference.get();
            if (stickerPackDetailsActivity != null) {
                stickerPackDetailsActivity.Nganu();
            }
        }
    }

    void Nganu(){
        addtowhatsapp.setText("already added");
        addtowhatsapp.setEnabled(false);
    }
    static String masalah;

    static String masalahHarga;
    public static void StartActivity(View v,Context context,StickerPack stickerPack,String harga){
        Log.d(TAG, "StartActivity: "+stickerPack.sku);
        masalah=stickerPack.sku;
        masalahHarga = harga;
        context.startActivity(new Intent(context, StickerDetailsActivity.class)
                        .putExtra(EXTRA_STICKERPACK, stickerPack)
                        .putExtra(EXTRA_STICKERPACK_RAW , stickerPack),
                ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(),
                        v.getHeight()).toBundle());
    }

    public static void StartActivity(View v,Context context,StickerPack stickerPack){
        Log.d(TAG, "StartActivity: "+stickerPack.sku);
        masalah=null;
        masalahHarga=null;
        context.startActivity(new Intent(context, StickerDetailsActivity.class)
                        .putExtra(EXTRA_STICKERPACK, stickerPack)
                        .putExtra(EXTRA_STICKERPACK_RAW , stickerPack),
                ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(),
                        v.getHeight()).toBundle());
    }

    void UpdateShop(Shop shop){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
           if(requestCode==ADD_PACK){
                mInterstitialAd.show();
            }
        }else {
            addtowhatsapp.setEnabled(true);
        }

    }


    //private WhiteListCheckAsyncTask whiteListCheckAsyncTask;

//    @Override
//    protected void onResume() {
//        super.onResume();
//        whiteListCheckAsyncTask = new WhiteListCheckAsyncTask(this);
//        whiteListCheckAsyncTask.execute(stickerPack);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (whiteListCheckAsyncTask != null && !whiteListCheckAsyncTask.isCancelled()) {
//            whiteListCheckAsyncTask.cancel(true);
//        }
//    }
}

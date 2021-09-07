package com.testing.testung;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.testing.testung.adapter.SectionAdapter;
import com.testing.testung.model.Shop;
import com.testing.testung.utils.JsonHandler;
import com.testing.testung.utils.JsonListener;
import com.testing.testung.view.LockableViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BillingProcessor.IBillingHandler, JsonListener {

    final int WRITEEXTCODE =1;
    public static final String EXTRA_STICKER_PACK_ID = "sticker_pack_id";
    public static final String EXTRA_STICKER_PACK_AUTHORITY = "sticker_pack_authority";
    public static final String EXTRA_STICKER_PACK_NAME = "sticker_pack_name";
    public static final String EXTRA_STICKERPACK = "stickerpack";
    public static final String EXTRA_STICKERPACK_RAW = "stickerpackraw";
    public static String APPID = "appid";//oke
    public static String BANNER = "banner";
    public static String INTERST = "inters";


    private static final String TAG = MainActivity.class.getSimpleName();
    private final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static String path;

    RecyclerView recyclerView;
    Toolbar toolbar;
    LockableViewPager viewPager;

    SectionAdapter sectionAdapter;
    ImageView drawelImage;
    TextView drawelName;
    TextView drawelEmail;
    RelativeLayout drawelItem;
    LinearLayout drawelLayout;

    void InitStiki(){

        path = getFilesDir() + "/" + "stickers_asset";
        getPermissions();
    }
    ProgressDialog mutualProgr;
    String url = "https://dl.dropboxusercontent.com/s/x2a7n1e53exz1dz/wastickercoupleloveuniquedeviklan.json";
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //UpdateUser(true);
        InitStiki();
        mutualProgr=new ProgressDialog(this);
        mutualProgr.setCancelable(false);
        mutualProgr.setCanceledOnTouchOutside(false);
        mutualProgr.show();
        setContentView(R.layout.activity_test_drawer);

        //LoadFromJson

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Store");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        drawelEmail = headerView.findViewById(R.id.emaildrawel);
        drawelName = headerView.findViewById(R.id.namedrawel);
        drawelImage = headerView.findViewById(R.id.imageView_drawl);
        drawelItem = headerView.findViewById(R.id.spiner_relative);
        drawelLayout = headerView.findViewById(R.id.drawel_layout_uyu);

        //ini ga boleh diatas mas
        //set apa jadi true biar pembelian aktif
        Pembelian(false);

        JsonHandler jsonHandler = new JsonHandler(url,this);
        jsonHandler.execute();

        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                LoadIklan();
            }
        }.start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==WRITEEXTCODE){
            int perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (perm != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                finish();
                Toast.makeText(this, "Our App Can't good work", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public static void SaveImage(Bitmap finalBitmap, String name, String identifier) {
        String root = path + "/" + identifier;
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = name;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.WEBP, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SaveTryImage(Bitmap finalBitmap, String name, String identifier) {

        String root = path + "/" + identifier;

        File myDir = new File(root + "/" + "try");
        myDir.mkdirs();
        String fname = name.replace(".png","").replace(" ","_") + ".png";
        Log.d(TAG, "onCreate: "+root);
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPermissions() {
        int perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (perm != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    WRITEEXTCODE
            );
        }
    }

    public static String getLastBitFromUrl(final String url) {
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer!=null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.test_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.nav_store){
            if(viewPager!=null) {
                viewPager.setCurrentItem(0, true);
             //   toolbar.setTitle("Store");
            }
        }
//        else if(id==R.id.nav_make_sticker){
//            if(viewPager!=null) {
//                viewPager.setCurrentItem(1, true);
//                toolbar.setTitle("Make Sticker");
//            }
//        }
//        //Ini Bagian About dll kalo misal biar ga dipake buka res-> menu ->activity test drawer
//        else if (id==R.id.nav_help){
//            String url = "https://pengembangsebelah.com/index.php/help-center-store-sticker-for-whatsapp/";
//
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        }else if(id==R.id.nav_about_app){
//            String url = "https://pengembangsebelah.com/index.php/2019/01/10/store-stickers-for-whatsapp-app/";
//
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        }else if(id==R.id.nav_term){
//            String url = "https://pengembangsebelah.com/index.php/term-uses-store-sticker-for-whatsapp/";
//
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public static List<Shop> getUserShop(){

            return null;

    }

    BillingProcessor bp;
    void Pembelian(boolean apa){
        if(apa) {
            String judsa = "";
            bp = new BillingProcessor(this, judsa, this);
            //new HelpSku(this);
            bp.initialize();
        }else {
            sectionAdapter = new SectionAdapter(getSupportFragmentManager(), bp);
            viewPager = findViewById(R.id.home_store_container);
            viewPager.setAdapter(sectionAdapter);
            viewPager.setSwipeable(false);
            viewPager.setCurrentItem(0, true);
            toolbar.setTitle("Store");
            mutualProgr.dismiss();
        }
    }
    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        if(details.purchaseInfo.purchaseData.productId.substring(0,1)=="s"){
            Shop shop = new Shop(details.purchaseInfo.purchaseData.packageName,productId,"yops",details.purchaseInfo.purchaseData.purchaseTime.toString());
            UpdateShop(shop);
        }else {
            Toast.makeText(this, "Thankyou so much", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        sectionAdapter = new SectionAdapter(getSupportFragmentManager(), bp);
        viewPager = findViewById(R.id.home_store_container);
        viewPager.setAdapter(sectionAdapter);
        viewPager.setSwipeable(false);
        viewPager.setCurrentItem(0, true);
        toolbar.setTitle("Store");
        mutualProgr.dismiss();
    }

    @Override
    public void onDestroy() {
//        if (bp != null) {
//            bp.release();
//            bp=null;
//        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void UpdateShop(Shop shop){
    }

    boolean oneCall = false;
    void LoadIklan(){
        //if(oneCall) return;
        //oneCall =true;
        //bagian loadIkan
        MobileAds.initialize(this, APPID);
        View f = findViewById(R.id.adView_1);
        final AdView mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(BANNER);
        ((RelativeLayout)f).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Log.d("Hore", "LoadIklan: "+BANNER);
    }

    @Override
    public void Succes(JSONObject json) {
        try {
            String _appid = json.getString("appid");
            String _banner = json.getString("banner");
            String _inter = json.getString("inters");

            APPID =_appid;
            BANNER = _banner;
            INTERST=_inter;
            Log.d("Hore", "Succes:HORE "+APPID+"\n"+BANNER);
            //LoadIklan();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void Failed(String mes) {
        Log.d(TAG, "Failed: "+mes);
    }
}

package com.testing.testung.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JsonHandler extends AsyncTask<Void,Void,Void> {
    JsonListener jsonListener;
    String Url = "";
    String data = "";
    String dataRaw = "";

    public JsonHandler(String Url){
        this.Url=Url;
    }

    public JsonHandler(String Url,JsonListener listener){
        this.Url=Url;
        this.jsonListener = listener;
        Log.d("Hore", "JsonHandler: "+Url);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        BufferedReader bufferedReader = null;
        try{
            URL url = new URL(Url);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String dataLine = bufferedReader.readLine();
//            Log.d("Hore", "doInBackground: "+dataLine);
//            while (dataLine!=dataRaw){
//                data = dataLine+ "\n";
//                dataRaw = dataLine;
//            }
//
//            Log.d("Hore", "doInBackground: "+data);
            JSONObject JO = new JSONObject(dataLine);
            Log.d("Hore", "doInBackground: "+JO.toString());
            jsonListener.Succes(JO);
        }catch (MalformedURLException e) {
            e.printStackTrace();
            jsonListener.Failed(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            jsonListener.Failed(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            jsonListener.Failed(e.getMessage());
        }

        //itu bener run nya ke motorola mas, bener mas bro.. aku build ters drag ke nox
        return null;
    }
}

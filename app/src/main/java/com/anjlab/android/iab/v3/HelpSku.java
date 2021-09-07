/**
 * Copyright 2014 AnjLab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.anjlab.android.iab.v3;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HelpSku{

    interface CBC{
        void Succes(String json);
    }
    String JSON;
    public HelpSku(final Activity context){
        Log.d("CRACK", "Succes:sds ");
        new HelpMe(context, new CBC() {
            @Override
            public void Succes(String json) {
                try {

                    if (json != null) {
                        try {
                            JSONObject jsonResponse = new JSONObject(json);
                            JSONArray verified = jsonResponse.getJSONArray("verified");
                            for (int i = 0; i < verified.length(); i++) {
                                JSONObject jsonChildNode = verified.getJSONObject(i);
                                if(jsonChildNode.getString("package")==context.getPackageName()){
                                    Log.d("CRACK", "Succes: ");
                                    context.finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "https://pengembangsebelah.com/game/sticker/rule.json").execute();
    }

    class HelpMe extends AsyncTask<Void, Void, Void> {

        private String url, jsonResult;
        Context contexxt;
        CBC cbc;

        public HelpMe(Context context,CBC calback,String url) {
            contexxt = context;
            this.url = url;
            this.cbc=calback;
        }

        @Override
        protected Void doInBackground(Void... z) {
            try {
                URL urll =  new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urll.openConnection();

                jsonResult = inputStreamToString(connection.getInputStream())
                        .toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cbc.Succes(jsonResult);
        }

    }
}

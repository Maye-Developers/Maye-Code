package com.l4d.maye;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macintosh on 9.05.16.
 */
public class JsonParcala {

    private Context context;
    private List<String> liste = new ArrayList<>();
    private List<String> gelenElemanlar = new ArrayList<>();
    private int id;
    public JsonParcala(Context context, List<String> gelenElemanlar, int id){
        this.context=context;
        this.id = id;
        this.gelenElemanlar=gelenElemanlar;
    }

    public void guncelle(){
        AsyncDataClass asyncRequestObject = new AsyncDataClass();
        asyncRequestObject.execute(gelenElemanlar);
    }


    private class AsyncDataClass extends AsyncTask<List<String>, Void, String> {

        ProgressDialog progDailog;

        @Override
        protected String doInBackground(List<String>... params) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0].get(0));

            String jsonResult = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                if(id == 0){
                    nameValuePairs.add(new BasicNameValuePair("mail", params[0].get(1)));
                    nameValuePairs.add(new BasicNameValuePair("kullanici", params[0].get(2)));
                    nameValuePairs.add(new BasicNameValuePair("sifre", params[0].get(3)));
                    nameValuePairs.add(new BasicNameValuePair("id", params[0].get(4)));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();
                System.out.println("Returned Json object " + jsonResult);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(context);
            progDailog.setMessage("Yükleniyor...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progDailog.dismiss();
            System.out.println("Resulted Value: " + result);
            if (result.equals("") || result == null) {
                Toast.makeText(context, "Lütfen internet bağlantınızı kontrol ediniz", Toast.LENGTH_LONG).show();
                return;
            }
            String jsonResult = returnParsedJsonObject(result);
            if (jsonResult.equals("0")) {
                //Toast.makeText(context, "yanlış id", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                return;
            }
        }
    }

    private String returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        String returnedResult = "0";
        try {
            resultObject = new JSONObject(result);
            if(id == 1){
                returnedResult = resultObject.getString("sonuc");
                liste.add(resultObject.getString("baslik"));
                liste.add(resultObject.getString("icerik"));
                liste.add(resultObject.getString("toplam"));

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = pref.edit();

                editor.putInt("id", Integer.parseInt(resultObject.getString("id")));
                editor.apply();

                JSONArray parca = resultObject.getJSONArray("parca");
                System.out.println("ertugrul1 " + parca.toString());
                for(int i = 0; i<parca.length(); i++){
                    JSONObject obj = parca.getJSONObject(i);
                    // parametrelere karşılık gelen degerler alınır.
                    String p = obj.getString("adi");
                    liste.add(p);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = br.readLine()) != null) {
                answer.append(rLine);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return answer;
    }
}

package com.l4d.maye;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnGiris;
    EditText EKullaniciAdiorMail, ESifre;
    String KullaniciAdiorMail, Sifre, soru,id;
    private final String serverUrl = "http://dijitaloyunzirvesi.com/android/giris.php"; // dijitaloyun giris.php seklinde degistir

    public static String username, password, mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGiris = (Button) findViewById(R.id.btnGiris);
        EKullaniciAdiorMail = (EditText) findViewById(R.id.txtGirisUser);
        ESifre = (EditText) findViewById(R.id.txtGirisSifre);

        btnGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KullaniciAdiorMail = EKullaniciAdiorMail.getText().toString();
                Sifre = ESifre.getText().toString();

                if (KullaniciAdiorMail.equals("") || Sifre.equals("")) {
                    Toast.makeText(MainActivity.this, "Lütfen kulanıcı adı ya da mail adresiniz yada şifrenizi boş bırakmayın", Toast.LENGTH_LONG).show();
                    return;
                } else if (Sifre.length() <= 1) {
                    Toast.makeText(MainActivity.this, "Şifreniz bir harften fazla olmalıdır", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    AsyncDataClass asyncRequestObject = new AsyncDataClass();

                    asyncRequestObject.execute(serverUrl, KullaniciAdiorMail, Sifre);
                }

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,KayitActivity.class);
                startActivity(i);
            }
        });

    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);

            String jsonResult = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("user", params[1]));
                nameValuePairs.add(new BasicNameValuePair("sifre", params[2]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if (result.equals("") || result == null) {
                Toast.makeText(MainActivity.this, "Lütfen internet bağlantınızı kontrol ediniz", Toast.LENGTH_LONG).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if (jsonResult == 0) {
                Toast.makeText(MainActivity.this, "Mail adresi yada şifreniz yanlış", Toast.LENGTH_LONG).show();
                return;
            }
            if (jsonResult == 1) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();

                editor.putInt("session", Integer.parseInt(id));
                editor.apply();

                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                int myId = pref.getInt("id", 0);
                if (myId == 0) {
                    SharedPreferences.Editor edit = pref.edit();

                    edit.putInt("id", Integer.parseInt(soru));
                    edit.apply();
                }
                Intent intent = new Intent(MainActivity.this, IndexActivity.class); // MainActivity duzeltilecek
                intent.putExtra("USERNAME", KullaniciAdiorMail);
                intent.putExtra("MESSAGE", "You have been successfully login");
                startActivity(intent);
            }
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

        private int returnParsedJsonObject(String result) {

            JSONObject resultObject = null;
            int returnedResult = 0;
            try {
                resultObject = new JSONObject(result);
                returnedResult = resultObject.getInt("sonuc");
                soru = resultObject.getString("soru");
                id = resultObject.getString("id");

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();

                username = resultObject.getString("kullaniciadi");
                password = resultObject.getString("sifre");
                mail = resultObject.getString("mail");

                editor.putString("kullanici", username);
                editor.putString("i", id);
                editor.putString("sifre", password);
                editor.putString("mail", mail);
                editor.apply();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnedResult;
        }
    }
}
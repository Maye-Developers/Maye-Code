package com.l4d.maye;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class KayitActivity extends AppCompatActivity {

    Button btn;
    EditText EkullaniciAdi, Email,Esifre, EsifreTekrar;
    String kullaniciAdi, pass, mail, passAgain;
    private final String serverUrl = "http://dijitaloyunzirvesi.com/android/kayit.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        btn = (Button) findViewById(R.id.btnKayit);
        EkullaniciAdi = (EditText) findViewById(R.id.txtKullanici);
        Esifre = (EditText) findViewById(R.id.txtSifre);
        Email = (EditText) findViewById(R.id.txtMail);
        EsifreTekrar = (EditText) findViewById(R.id.txtSifreTekrar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passAgain = EsifreTekrar.getText().toString();
                kullaniciAdi = EkullaniciAdi.getText().toString();
                pass = Esifre.getText().toString();
                mail = Email.getText().toString();

                if (kullaniciAdi.equals("") || pass.equals("")) {
                    Toast.makeText(KayitActivity.this, "Lütfen mail adresiniz yada şifrenizi boş bırakmayın", Toast.LENGTH_LONG).show();
                    return;
                } else if (!isValidEmail(mail)) {
                    Toast.makeText(KayitActivity.this, "Lütfen mail adresinizi doğru formatta giriniz.", Toast.LENGTH_LONG).show();
                    return;
                } else if (pass.length() <= 1 || passAgain.length()<=1 ) { // 3
                    Toast.makeText(KayitActivity.this, "Şifreniz bir harften fazla olmalıdır", Toast.LENGTH_LONG).show();
                    return;
                }else  if(!pass.equals(passAgain)){
                    Toast.makeText(KayitActivity.this, "Şifre kısımları aynı olmalıdır", Toast.LENGTH_SHORT).show();
                }
                else {
                    AsyncDataClass asyncRequestObject = new AsyncDataClass();
                    asyncRequestObject.execute(serverUrl, kullaniciAdi, mail, pass);
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(KayitActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
                nameValuePairs.add(new BasicNameValuePair("kadi", params[1]));
                nameValuePairs.add(new BasicNameValuePair("mail", params[2]));
                nameValuePairs.add(new BasicNameValuePair("sifre", params[3]));
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);
            if (result.equals("") || result == null) {
                Toast.makeText(KayitActivity.this, "Lütfen internet bağlantınızı kontrol ediniz", Toast.LENGTH_LONG).show();
                return;
            }
            int jsonResult = returnParsedJsonObject(result);
            if (jsonResult == 0) {
                Toast.makeText(KayitActivity.this, "Bu mail adresi ile daha önce kayıt olunmuş!", Toast.LENGTH_LONG).show();
                return;
            }
            if (jsonResult == 1) {
                Toast.makeText(KayitActivity.this, "Başarıyla kayıt oldunuz. Lütfen giriş yapınız", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(KayitActivity.this, MainActivity.class);
                intent.putExtra("USERNAME", kullaniciAdi);
                intent.putExtra("MESSAGE", "You have been successfully Registered");
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnedResult;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}

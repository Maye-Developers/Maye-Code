package com.l4d.maye;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView numara, indexBaslik, indexIcerik;
    LinearLayout drop, processBox, tags, tags2;
    List<Parca> parcalar = new ArrayList<>();
    kontrol k;
    String toplam,progSayac, yeniSayfa,baslik,icerik;
    int id, sayac = 1;
    private final String serverUrl = "http://www.dijitaloyunzirvesi.com/android/adimGonder.php";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = preferences.getInt("id", 0);
        int session = preferences.getInt("session", 0);

        System.out.println("myintdeger " + id);
        processBox = (LinearLayout) findViewById(R.id.processBox);

        indexBaslik = (TextView) findViewById(R.id.indexBaslik);
        indexIcerik = (TextView) findViewById(R.id.indexIcerik);
        numara = (TextView) findViewById(R.id.numara);

        AsyncDataClass asyncRequestObject = new AsyncDataClass();
        String newId = "";

        if (KontrolActivity.kontrolet) {
            newId = String.valueOf(id);
            KontrolActivity.kontrolet = false;
            yeniSayfa = "t";
        } else {
            newId = String.valueOf(id - 1);
            yeniSayfa = "f";
        }

        asyncRequestObject.execute(serverUrl, newId, String.valueOf(session),yeniSayfa);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drop = (LinearLayout) findViewById(R.id.drop);
        tags2 = (LinearLayout) findViewById(R.id.tags2);
        tags = (LinearLayout) findViewById(R.id.tags);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.quiz) {
            Intent i = new Intent(IndexActivity.this, QuizActivity.class);
            startActivity(i);
        } else if (id == R.id.map) {
            Intent i = new Intent(IndexActivity.this, yolharitamActivity.class);
            startActivity(i);
        } else if (id == R.id.settings) {
            Intent i = new Intent(IndexActivity.this, ayarlarActivity.class);
            startActivity(i);
        } else if (id == R.id.share) {
            Intent i = new Intent(IndexActivity.this, SocialActivity.class);
            startActivity(i);
        } else if (id == R.id.exit) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor edit = pref.edit();
            edit.putInt("session", 0);


            edit.remove("session");
            edit.remove("id");
            edit.remove("kullanici");
            edit.remove("i");
            edit.remove("sifre");
            edit.remove("mail");
            edit.apply();

            Intent i = new Intent(IndexActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void autoSmoothScroll(final int deger) {

        final HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.process);
        hsv.postDelayed(new Runnable() {
            @Override
            public void run() {
                //hsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                hsv.smoothScrollBy(deger, 0);
            }
        },100);
    }

    public void progressUret() {
        int top = Integer.parseInt(toplam);
        int deger=0;
        for (int i = 0; i < top; i++) {
            ImageView img = new ImageView(IndexActivity.this);
            if(i==0){
                img.setBackgroundResource(R.drawable.aktif);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(80
                        , 50);
                lp.setMargins(0, 0, 0, 0);
                img.setLayoutParams(lp);
            }
            else if (i < Integer.parseInt(progSayac)) {
                img.setBackgroundResource(R.drawable.aktif);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(80
                        , 50);
                lp.setMargins(-5, 0, 0, 0);
                img.setLayoutParams(lp);
                deger+=80;
            } else {
                img.setBackgroundResource(R.drawable.pasif);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(80
                        , 50);
                lp.setMargins(-5, 0, 0, 0);
                img.setLayoutParams(lp);
            }

            processBox.addView(img);
        }

        autoSmoothScroll(deger-80);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Index Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.l4d.maye/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Index Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.l4d.maye/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public class tikla implements View.OnTouchListener {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            TextView t = (TextView) v;
            boolean b = k.esles(t.getText().toString());
            if (b) {
                sayac++;
                ((LinearLayout) v.getParent()).removeView(v);
                if (sayac > parcalar.size()) {
                    KontrolActivity.kontrolet = true;
                    Toast.makeText(IndexActivity.this, "Doğru Cevap", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(IndexActivity.this, IndexActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 1500);

                }
            }
            return false;
        }
    }

    public class kontrol {
        private String dizi[];
        private int i = 0, sayac = 0;

        public kontrol(int s, List<Parca> p) {
            dizi = new String[s];
            for (Parca parca : p) {
                dizi[sayac] = parca.ad;
                sayac++;
            }
        }

        public boolean esles(String s) {
            if (s.equals(dizi[i])) {
                ekle(s);
                i++;
                return true;
            } else {
                Toast.makeText(IndexActivity.this, "Yanlış Cevap!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        private void ekle(String s) {
            TextView t = new TextView(IndexActivity.this);
            t.setBackground(null);
            int paddingPixel = 5;
            float density = IndexActivity.this.getResources().getDisplayMetrics().density;
            int paddingDp = (int) (paddingPixel * density);
            t.setPadding(paddingDp, paddingDp, 0, paddingDp);
            t.setText(s);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 2, 0);
            t.setLayoutParams(lp);
            t.setTextColor(Color.WHITE);
            drop.addView(t);
        }

    }

    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        ProgressDialog progDailog;

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
                nameValuePairs.add(new BasicNameValuePair("id", params[1]));
                nameValuePairs.add(new BasicNameValuePair("session", params[2]));
                nameValuePairs.add(new BasicNameValuePair("yenisayfa", params[3]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));

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
            progDailog = new ProgressDialog(IndexActivity.this);
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
                Toast.makeText(IndexActivity.this, "Lütfen internet bağlantınızı kontrol ediniz", Toast.LENGTH_LONG).show();
                return;
            }
            String jsonResult = returnParsedJsonObject(result);
            if (jsonResult.equals("0")) {
                Toast.makeText(IndexActivity.this, "yanlış id", Toast.LENGTH_LONG).show();
                return;
            } else {
                String newIcerik="", karakter;
                char[] karakterler;
                karakterler = icerik.toCharArray();
                for(int i=0;i<karakterler.length;i++){
                    if(karakterler[i] == '/')
                        karakter = "ç";
                    else if(karakterler[i] == '-')
                        karakter = "ğ";
                    else if(karakterler[i] == '*')
                        karakter = "ı";
                    else if(karakterler[i] == '%')
                        karakter = "ö";
                    else if(karakterler[i] == '&')
                        karakter = "ş";
                    else if(karakterler[i] == '^')
                        karakter = "ü";
                    else
                        karakter = String.valueOf(karakterler[i]);

                    newIcerik += karakter;
                }

                indexBaslik.setText(baslik);
                indexIcerik.setText(newIcerik);
                numara.setText(progSayac);
            }
        }
    }

    private StringBuilder inputStreamToString(InputStream is) throws UnsupportedEncodingException {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
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

    public void addTags() {
        String s[] = new String[parcalar.size()];
        int i = 0;
        for (Parca p : parcalar) {
            s[i] = p.ad;
            i++;
        }
        i = 0;
        while (true) {
            int in = 0 + (int) (Math.random() * parcalar.size());
            System.out.println("rastgele " + in);
            if (i != parcalar.size()) {
                if (s[in] != null) {
                    TextView t = new TextView(IndexActivity.this);
                    t.setBackgroundResource(R.drawable.karepembe);
                    int paddingPixel = 5;
                    float density = IndexActivity.this.getResources().getDisplayMetrics().density;
                    int paddingDp = (int) (paddingPixel * density);
                    t.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
                    t.setText(s[in]);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 15, 0);
                    t.setLayoutParams(lp);
                    t.setTextColor(Color.WHITE);
                    t.setOnTouchListener(new tikla());
                    if (i < 3)
                        tags.addView(t);
                    else
                        tags2.addView(t);
                    s[in] = null;
                    i++;
                }
            } else break;
        }
    }

    private String returnParsedJsonObject(String result) {

        JSONObject resultObject = null;
        String returnedResult = "0";
        try {
            resultObject = new JSONObject(result);
            returnedResult = resultObject.getString("sonuc");
            baslik = resultObject.getString("baslik");
            icerik = resultObject.getString("icerik");
            toplam = resultObject.getString("toplam");
            progSayac = resultObject.getString("sayac");

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = pref.edit();

            editor.putInt("id", Integer.parseInt(resultObject.getString("id")));
            editor.apply();

            JSONArray parca = resultObject.getJSONArray("parca");
            System.out.println("ertugrul1 " + parca.toString());
            for (int i = 0; i < parca.length(); i++) {
                JSONObject obj = parca.getJSONObject(i);
                // parametrelere karşılık gelen degerler alınır.
                String p = obj.getString("adi");
                Parca par = new Parca();

                par.ad = p;
                parcalar.add(par);
            }
            addTags();
            progressUret();
            k = new kontrol(parcalar.size(), parcalar);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedResult;
    }

    public class Parca {
        public String ad;
    }
}

package com.l4d.maye;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //get rating bar object
        //get text view
        TextView t=(TextView)findViewById(R.id.textResult);
        //get score
        Bundle b = getIntent().getExtras();
        int score= b.getInt("Puan");
        TextView t2=(TextView)findViewById(R.id.skor);
        t2.setText("Puanın: "+String.valueOf(score));
        switch (score)
        {
            case 1:
            case 2: t.setText("Biraz daha çalışman lazım");
                break;
            case 3:
            case 4:
            case 5:t.setText("Bizim testlerimizi bir daha çözmeni tavsiye ederim");
                break;
            case 6:
            case 7:
            case 9:t.setText("Süpersin! Bunu yapan ilk kişi olabilirsin :)");
                break;}
    }
}

package com.l4d.maye;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "triviaQuiz";
    // tasks table name
    private static final String TABLE_QUEST = "quest";
    // tasks Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_QUES = "question";
    private static final String KEY_ANSWER = "answer"; //correct option
    private static final String KEY_OPTA= "opta"; //option a
    private static final String KEY_OPTB= "optb"; //option b
    private static final String KEY_OPTC= "optc"; //option c

    private SQLiteDatabase dbase;
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        dbase=db;
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_QUEST + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_QUES
                + " TEXT, " + KEY_ANSWER+ " TEXT, "+KEY_OPTA +" TEXT, "
                +KEY_OPTB +" TEXT, "+KEY_OPTC+" TEXT)";

        db.execSQL(sql);
        addQuestions();
        //db.close();
    }
    private void addQuestions()
    {
        Question q1=new Question("Web tarayıcılarının derlediği kodlar?","HTML", "JAVA", "C#", "HTML");
        this.addQuestion(q1);
        Question q2=new Question("Baslık etiketi hangisidir?", "<br>", "<h>", "<div>","<h>");
        this.addQuestion(q2);
        Question q3=new Question("Aşağıda verilen link komutlarından hangisi doğrudur?","<a img=”http://www.meb.gov.tr”>Tıklayınız<img/a>", " <href=”http://www.meb.gov.tr”>", "<a href=”http://www.meb.gov.tr”>Tıklayınız</a>", "<a href=”http://www.meb.gov.tr”>Tıklayınız</a>");
        this.addQuestion(q3);
        Question q4=new Question("Internet’ teki dosyaların genel uzantısı nedir?",".html", "www", ".txt", ".html");
        this.addQuestion(q4);
        Question q5=new Question("HTML etiketleri hangi karakterlerle belirtilir?", "<>", "()", "#","<>");
        this.addQuestion(q5);
        Question q6 = new Question("<u></u> etiketi ne için kullanılır?", "Altını çizmek için", "Üstünü çizmek için", "üst bilgi","Altını çizmek için");
        this.addQuestion(q6);
        Question q7 = new Question("HTML elemanlarının görüntülenmesinde bir satır aşağıya inmek için hangi etiket kullanılır?", "<NEWLINE>", "<BR>", "<BREAKNEW>","<BR>");
        this.addQuestion(q7);
        Question q8=new Question("<input></input> etiketi ne için kullanılır?", "bir hesaplama sonucu tanımlar.", "kullanıcının veri girişi yapabileceği giriş alanı tanımlar.", "tablo başlığı tanımlar.","etiketi kullanıcının veri girişi yapabileceği giriş alanı tanımlar.");
        this.addQuestion(q8);
        Question q9=new Question("Hangisi arkaplan rengi için kullanılır?", "<body bgcolor=”yellow”>", "<background>yellow</background>", "<body color=”yellow”>","<body bgcolor=”yellow”>");
        this.addQuestion(q9);
        Question q10=new Question("<em></em> etiketi ne için kullanılır?", "Bir parça bilgisayar kodu tanımlar.", "Güçlü metin tanımlar.", "Vurgulanmış metin tanımlar.","Vurgulanmış metin tanımlar.");
        this.addQuestion(q10);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        // Create tables again
        onCreate(db);
    }
    // Adding new question
    public void addQuestion(Question quest) {
        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_QUES, quest.getQUESTION());
        values.put(KEY_ANSWER, quest.getANSWER());
        values.put(KEY_OPTA, quest.getOPTA());
        values.put(KEY_OPTB, quest.getOPTB());
        values.put(KEY_OPTC, quest.getOPTC());

        // Inserting Row
        dbase.insert(TABLE_QUEST, null, values);
    }
    public List<Question> getAllQuestions() {
        List<Question> quesList = new ArrayList<Question>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        dbase=this.getReadableDatabase();
        Cursor cursor = dbase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) do {
            Question quest = new Question();
            quest.setID(cursor.getInt(0));
            quest.setQUESTION(cursor.getString(1));
            quest.setANSWER(cursor.getString(2));
            quest.setOPTA(cursor.getString(3));
            quest.setOPTB(cursor.getString(4));
            quest.setOPTC(cursor.getString(5));

            quesList.add(quest);
        } while (cursor.moveToNext());
        // return quest list
        return quesList;
    }
    public int rowcount()
    {
        int row=0;
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        row=cursor.getCount();
        return row;
    }
}

package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FontTexts();
        startService(new Intent(this, Music.class));

    }

    //Стиль текста
    private void FontTexts(){
        final TextView start = (TextView)findViewById(R.id.start);
        start.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView quick = (TextView)findViewById(R.id.quick);
        quick.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView settings = (TextView)findViewById(R.id.settings);
        settings.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView about = (TextView)findViewById(R.id.rules);
        about.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView exit = (TextView)findViewById(R.id.exit);
        exit.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
        final TextView NetGame = (TextView)findViewById(R.id.Netgame);
        NetGame.setTypeface(Typeface.createFromAsset(
                getAssets(), "fonts/JurassicPark-BL48.ttf"));
    }
    //Переход в Правила
    public void goToRules(View view) {
        Intent i = new Intent(StartActivity.this, RulesActivity.class);
        startActivity(i);
        this.finish();
    }
    //Переход в параметры
    public void goToOptions(View view) {
        Intent i = new Intent(StartActivity.this, OptionActivity.class);
        startActivity(i);
        this.finish();
    }
    public void goToCustom(View view){
        Intent i = new Intent(StartActivity.this, Customization.class);
        startActivity(i);
        this.finish();
    }
    //Кнопка "выход"
    public void onBackPressed(View view) {
        moveTaskToBack(true); android.os.Process.killProcess(android.os.Process.myPid()); System.exit(1);
        stopService(new Intent(this, Music.class));
    }
    //Кнопка "играть"
    public void goTogames(View view){
        String mIcon;
        String mDesk;
        String mnick;
        Intent intent = getIntent();
        mIcon= intent.getStringExtra("I1");
        mDesk = intent.getStringExtra("D1");
        mnick= intent.getStringExtra("mnick");
        if(mnick==null){
            mnick="Default Player";
        }
        if (mIcon == null){
            mIcon=Integer.toString(R.drawable.icon_enchanter); // заменить на дефолтную иконку
        }
        if (mDesk == null){
            mDesk=Integer.toString(R.drawable.field_enchanter); // заменить на дефолтное поле
        }
        Intent i = new Intent(StartActivity.this, Deckofcards.class);
        i.putExtra("mnick",mnick);
        i.putExtra("I2", mIcon);
        i.putExtra("D2", mDesk);
        startActivity(i);

    }
    public void ToNet(View view){
        Intent i = new Intent(StartActivity.this, LogInActivity.class);
        startActivity(i);
        this.finish();
    }

}

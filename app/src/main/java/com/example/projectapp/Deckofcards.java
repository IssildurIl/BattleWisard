package com.example.projectapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Deckofcards extends AppCompatActivity {
    Button Click;
    Button rollDicesButton;
    TextView nick;
    ImageView iv_deck, iv_card1, iv_card2, iv_card3, iv_card4, iv_card5, iv_card6;
    public int[] card_table = { 0, 0, 0};
    //public int[] card_hand = { 0, 0, 0, 0, 0, 0};
    public int[] card_opp = { 0, 0, 0, 0, 0, 0};
    ImageView fstcard,seccard,thirdcard;
    int card_num=0, leave_card=6, leave_opp=6;
    ImageView PlayerIcon, mLeftImageView,mRightImageView;
    int mIcon, mDesk;
    //
    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "Nickname";
    //
    ArrayList<Integer> main_cards;
    ArrayList<Integer> cards_i;
    ArrayList<Integer> cards_k;
    ArrayList<Integer> cards_d;
    // поле
    ImageView tab_im1;
    //
    FrameLayout spells, hand;
    int value1, value2;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deckofcards);

        Click = findViewById(R.id.end);
        Click.setEnabled(false);
        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click.setEnabled(false);
                rollDicesButton.setEnabled(true);
                tableAction(leave_card);
                takeCardOpp(leave_opp);
                opp_turn();
                tableActionOpp(leave_opp);
            }
        });
        main_cards = new ArrayList<>();
        createArrayListOfCards(main_cards);
        cards_i = new ArrayList<>();
        createArrayListOfI(cards_i);
        cards_k = new ArrayList<>();
        createArrayListOfK(cards_k);
        cards_d = new ArrayList<>();
        createArrayListOfD(cards_d);
        //shuffle the cards
        Collections.shuffle(main_cards);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        PlayerIcon = findViewById(R.id.playerIcon);
        nick = findViewById(R.id.playerNick);
        spells = (FrameLayout) findViewById(R.id.spells);
        hand = (FrameLayout) findViewById(R.id.hand);
        //
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        getText();
        //
        iv_deck = findViewById(R.id.iv_deck);
        iv_card1 = findViewById(R.id.iv_card1);
        iv_card2 = findViewById(R.id.iv_card2);
        iv_card3 = findViewById(R.id.iv_card3);
        iv_card4 = findViewById(R.id.iv_card4);
        iv_card5 = findViewById(R.id.iv_card5);
        iv_card6 = findViewById(R.id.iv_card6);

        fstcard = (ImageView) findViewById(R.id.fstcard);
        seccard = (ImageView) findViewById(R.id.seccard);
        thirdcard = (ImageView) findViewById(R.id.thirdcard);

        fstcard.setOnDragListener(dragListener);
        seccard.setOnDragListener(dragListener);
        thirdcard.setOnDragListener(dragListener);

        fstcard.setImageResource(R.drawable.i_card);
        seccard.setImageResource(R.drawable.k_card);
        thirdcard.setImageResource(R.drawable.d_card);

        iv_card1.setOnLongClickListener(longClickListener);
        iv_card2.setOnLongClickListener(longClickListener);
        iv_card3.setOnLongClickListener(longClickListener);
        iv_card4.setOnLongClickListener(longClickListener);
        iv_card5.setOnLongClickListener(longClickListener);
        iv_card6.setOnLongClickListener(longClickListener);
//
        Intent intent = getIntent();
        mIcon = Integer.parseInt(intent.getStringExtra("I2"));
        mDesk = Integer.parseInt(intent.getStringExtra("D2"));
        String NickName = getIntent().getStringExtra("mnick");
        PlayerIcon.setImageResource(mIcon);
        // set background
        //getWindow().setBackgroundDrawableResource(mDesk);
        //nick.setText(NickName);

        iv_deck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeCard(leave_card);
            }
        });

//кубики
        rollDicesButton = (Button) findViewById(R.id.Roll);
        mLeftImageView = (ImageView) findViewById(R.id.imageview_left);
        mRightImageView = (ImageView) findViewById(R.id.imageview_right);
        rollDicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click.setEnabled(true);
                rollDicesButton.setEnabled(false);
                fooRollDice();
            }
        });
//
    }
    public void getText(){
        nick =(TextView) findViewById(R.id.playerNick);
        String savedText = mSettings.getString(APP_PREFERENCES_NAME, "");
        nick.setText(savedText);
    }
    private int randomDiceValue() {
        Random random = new Random();
        return random.nextInt(6) + 1;
    }
    //
    public void onImClick(View v) {
        Drawable drawable;
        switch (v.getId()) {
            case R.id.iv_card1:
                drawable = iv_card1.getDrawable();
                break;
            case R.id.iv_card2:
                drawable = iv_card2.getDrawable();
                break;
            case R.id.iv_card3:
                drawable = iv_card3.getDrawable();
                break;
            case R.id.iv_card4:
                drawable = iv_card4.getDrawable();
                break;
            case R.id.iv_card5:
                drawable = iv_card5.getDrawable();
                break;
            case R.id.iv_card6:
                drawable = iv_card6.getDrawable();
                break;
            case R.id.fstcard:
                drawable = fstcard.getDrawable();
                break;
            case R.id.seccard:
                drawable = seccard.getDrawable();
                break;
            case R.id.thirdcard:
                drawable = thirdcard.getDrawable();
                break;
            default:
                drawable = iv_deck.getDrawable();
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Deckofcards.this, R.style.CustomDialog);
        final ImageView image = new ImageView(this);
        image.setImageDrawable(drawable);
        alertDialog.setView(image);
        alertDialog.show();
    }

    //10.05.2020 3:23
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder myShadowbuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, myShadowbuilder, v, 0);
            return true;
        }
    };
    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            Drawable get_im=null;
            boolean flag;
            final View view = (View) event.getLocalState();
            switch (dragEvent) {
                case DragEvent.ACTION_DROP:
                    //11.05.2020 16:59
                    switch (view.getId()) {
                        case R.id.iv_card1:
                            //11.05.2020 16:38
                            get_im=iv_card1.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card2:
                            get_im=iv_card2.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card3:
                            get_im=iv_card3.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card4:
                            get_im=iv_card4.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card5:
                            get_im=iv_card5.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        case R.id.iv_card6:
                            get_im=iv_card6.getDrawable();
                            flag=changeCard(v.getId(), get_im);
                            break;
                        default:
                            flag=false;
                    }
                    // 12.05.2020 13:35 смещение карт в руке
                    if (flag){
                        //cardAction(detectCard(get_im));
                        card_table[leave_card-1]=detectCard(get_im);
                        switch (view.getId()) {
                            case R.id.iv_card1:
                                get_im=iv_card2.getDrawable();
                                iv_card1.setImageDrawable(get_im);
                            case R.id.iv_card2:
                                get_im=iv_card3.getDrawable();
                                iv_card2.setImageDrawable(get_im);
                            case R.id.iv_card3:
                                get_im=iv_card4.getDrawable();
                                iv_card3.setImageDrawable(get_im);
                            case R.id.iv_card4:
                                get_im=iv_card5.getDrawable();
                                iv_card4.setImageDrawable(get_im);
                            case R.id.iv_card5:
                                get_im=iv_card6.getDrawable();
                                iv_card5.setImageDrawable(get_im);
                            case R.id.iv_card6:
                                iv_card6.setImageDrawable(null);
                        }
                    }
            }
            return true;
        }
    };

    //11.05.2020 16:48 проверка свободно ли место на столе
    public boolean changeCard(int Rid, Drawable picture){
        switch (Rid) {
            case R.id.fstcard:
                if (detectCard(fstcard.getDrawable())==0) {
                    if (cards_i.indexOf(detectCard(picture)) > -1) {
                        fstcard.setImageDrawable(picture);
                        leave_card++;
                        return true;
                    }
                }
                return false;
            case R.id.seccard:
                if (detectCard(seccard.getDrawable())==0){
                    if (cards_k.indexOf(detectCard(picture)) > -1) {
                        seccard.setImageDrawable(picture);
                        leave_card++;
                        return true;
                    }
                }
                return false;
            case R.id.thirdcard:
                if (detectCard(thirdcard.getDrawable())==0){
                    if (cards_d.indexOf(detectCard(picture)) > -1) {
                        thirdcard.setImageDrawable(picture);
                        leave_card++;
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    public int detectCard(Drawable picture){
        Drawable.ConstantState imageViewDrawableState;
        for (int i=0; i<main_cards.size(); i++){
            imageViewDrawableState=getResources().getDrawable(main_cards.get(i)).getConstantState();
            if (imageViewDrawableState.equals(picture.getConstantState())){
                //Toast.makeText(PlayActivity.this,""+main_cards.get(i),Toast.LENGTH_SHORT).show();
                return main_cards.get(i);
            }
        }
        return 0;
    }

    public void gotoMenu(View view) {
        Intent i = new Intent(Deckofcards.this, StartActivity.class);
        startActivity(i);
        this.finish();
    }

    public void fooRollDice(){
        // Получим случайные числа для двух костей
        value1 = randomDiceValue();
        value2 = randomDiceValue();

        // Находим ресурс с этими числами
        int res1 = getResources().getIdentifier("dice" + value1,
                "drawable", "com.example.projectapp");
        int res2 = getResources().getIdentifier("dice" + value2,
                "drawable", "com.example.projectapp");

        mLeftImageView.setImageResource(res1);
        mRightImageView.setImageResource(res2);
    }

    public void takeCard(int qnt){
        cleanTable();
        if (card_num>=main_cards.size()){
            return;
        }
        switch (qnt){
            case 6:
                iv_card1.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 5:
                iv_card2.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 4:
                iv_card3.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 3:
                iv_card4.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 2:
                iv_card5.setImageResource(main_cards.get(card_num));
                card_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 1:
                iv_card6.setImageResource(main_cards.get(card_num));
                card_num++;
        }
    }

    public void takeCardOpp(int qnt){
        int guest_num=6-qnt;
        if (card_num>=main_cards.size()){
            return;
        }
        switch (qnt){
            case 6:
                card_opp[guest_num]=main_cards.get(card_num);
                card_num++;
                ((ImageView)findViewById(R.id.iv_card12)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 5:
                card_opp[guest_num]=main_cards.get(card_num);
                card_num++;
                ((ImageView)findViewById(R.id.iv_card22)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 4:
                card_opp[guest_num]=main_cards.get(card_num);
                card_num++;
                ((ImageView)findViewById(R.id.iv_card32)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 3:
                card_opp[guest_num]=main_cards.get(card_num);
                card_num++;
                ((ImageView)findViewById(R.id.iv_card42)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 2:
                card_opp[guest_num]=main_cards.get(card_num);
                card_num++;
                ((ImageView)findViewById(R.id.iv_card52)).setImageResource(card_opp[guest_num]);
                guest_num++;
                if (card_num>=main_cards.size()){
                    return;
                }
            case 1:
                card_opp[guest_num]=main_cards.get(card_num);
                card_num++;
                ((ImageView)findViewById(R.id.iv_card62)).setImageResource(card_opp[guest_num]);
                guest_num++;
            default:
                break;
        }
    }

    public void opp_turn(){
        leave_opp=0;
        int num;
        for (num=0; num<main_cards.size(); num++){
            if (cards_i.indexOf(card_opp[num])>-1){
                ((ImageView)findViewById(R.id.fstcard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp]=card_opp[num];
                leave_opp++;
                break;
            }
        }
        for (num=0; num<main_cards.size(); num++){
            if (cards_k.indexOf(card_opp[num])>-1){
                ((ImageView)findViewById(R.id.seccard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp]=card_opp[num];
                leave_opp++;
                break;
            }
        }
        for (num=0; num<main_cards.size(); num++){
            if (cards_d.indexOf(card_opp[num])>-1){
                ((ImageView)findViewById(R.id.thirdcard2)).setImageResource(card_opp[num]);
                remove_card_opp(num);
                card_table[leave_opp]=card_opp[num];
                leave_opp++;
                break;
            }
        }

    }

    public void remove_card_opp(int iter){
        switch (iter) {
            case 5:
                card_opp[0]=card_opp[1];
            case 4:
                card_opp[1]=card_opp[2];
            case 3:
                card_opp[2]=card_opp[3];
            case 2:
                card_opp[3]=card_opp[4];
            case 1:
                card_opp[4]=card_opp[5];
            case 0:
                card_opp[5]=0;
        }
    }

    public int[] cardAction(int card){
        switch(card){
            case R.drawable.d_darkness_3:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-2, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-4, 0};
                }
                break;
            case R.drawable.d_darkness_4:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-2, -1};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, -1};
                    case 10: case 11: case 12:
                        return new int[] {-5, -1};
                }
                break;
            case R.drawable.d_element_1:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-2, 0};
                    case 10: case 11: case 12:
                        return new int[] {-4, 0};
                }
                break;
            case R.drawable.d_element_2:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-5, 0};
                }
                break;
            case R.drawable.d_illusion_4:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-4, 0};
                }
                break;
            case R.drawable.d_nature_1:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-2, 0};
                    case 10: case 11: case 12:
                        return new int[] {-4, 0};
                }
                break;
            case R.drawable.d_nature_2:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {0, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {0, 2};
                    case 10: case 11: case 12:
                        return new int[] {0, 4};
                }
                break;
            case R.drawable.d_nature_3:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-2, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-6, 0};
                }
                break;
            case R.drawable.i_element_2:
            case R.drawable.i_element_3:
                return new int[] {-3, 0};
            case R.drawable.i_nature_4:
                return new int[] {0, 2};
            case R.drawable.i_nature_5://
                return new int[] {-3, 0};
            case R.drawable.i_secret_4:
                return new int[] {-3, 0};
            case R.drawable.k_darkness_1:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-2, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-4, -1};
                    case 10: case 11: case 12:
                        return new int[] {-5, -2};
                }
                break;
            case R.drawable.k_darkness_2:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {0, -3};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-3, 0};
                    case 10: case 11: case 12:
                        return new int[] {-5, 0};
                }
                break;
            case R.drawable.k_nature_1:
                switch(value1+value2){
                    case 2: case 3: case 4:
                        return new int[] {-1, 0};
                    case 5: case 6: case 7: case 8: case 9:
                        return new int[] {-1, 1};
                    case 10: case 11: case 12:
                        return new int[] {-3, 3};
                }
                break;
            case R.drawable.k_nature_4:
                return new int[] {-1, 0};
            case R.drawable.k_secret_1:
                return new int[] {-2, 0};
        }
        return new int[] {0, 0};
    }

    public void tableAction(int qnt){
        int[] hp={0, 0};
        for (int i=0; i<qnt; i++){
            hp=cardAction(card_table[i]);
            setHP(getHP()+hp[0]);
            setSelfHP(getSelfHP()+hp[1]);
        }
    }

    public void tableActionOpp(int qnt){
        fooRollDice();
        int[] hp={0, 0};
        for (int i=0; i<qnt; i++){
            hp=cardAction(card_table[i]);
            setHP(getHP()+hp[1]);
            setSelfHP(getSelfHP()+hp[0]);
        }
    }

    public void tableActionGuest(int qnt){
        int[] hp={0, 0};
        for (int i=0; i<qnt; i++){
            hp=cardAction(card_table[i]);
            setHP(getHP()+hp[0]);
            setSelfHP(getSelfHP()+hp[1]);
        }
    }
    public void opp_table(int qnt){
        for (int i=0; i<qnt; i++){
            if (cards_i.indexOf(card_table[i])>-1){
                ((ImageView)findViewById(R.id.fstcard2)).setImageResource(card_table[i]);
            }
        }
        for (int i=0; i<qnt; i++){
            if (cards_k.indexOf(card_table[i])>-1){
                ((ImageView)findViewById(R.id.seccard2)).setImageResource(card_table[i]);
            }
        }
        for (int i=0; i<qnt; i++){
            if (cards_d.indexOf(card_table[i])>-1){
                ((ImageView)findViewById(R.id.thirdcard2)).setImageResource(card_table[i]);
            }
        }
    }

    public int getHP(){
        return Integer.parseInt(((TextView)findViewById(R.id.hp2)).getText().toString());
    }

    public void setHP(int hp){
        int dead;
        if (hp<=0){
            dead=Integer.parseInt(((TextView)findViewById(R.id.dead2)).getText().toString());
            if(dead<2){
                ((TextView)findViewById(R.id.dead2)).setText(Integer.toString(dead+1));
                ((TextView)findViewById(R.id.hp2)).setText(Integer.toString(5));
            }else{
                ((TextView)findViewById(R.id.dead2)).setText("3");
                ((TextView)findViewById(R.id.hp2)).setText("0");
                AlertDialog alertDialog = new AlertDialog.Builder(Deckofcards.this).create();
                alertDialog.setTitle("Игра окончена");
                alertDialog.setMessage("Вы победили!");
                alertDialog.setIcon(R.drawable.end_win);
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Deckofcards.this, StartActivity.class);
                        startActivity(i);
                    }
                });
                alertDialog.show();
            }
        }else{
            ((TextView)findViewById(R.id.hp2)).setText(Integer.toString(hp));
        }
    }
    public int getSelfHP(){
        return Integer.parseInt(((TextView)findViewById(R.id.hp)).getText().toString());
    }

    public void setSelfHP(int hp){
        int dead;
        if (hp<=0){
            dead=Integer.parseInt(((TextView)findViewById(R.id.dead)).getText().toString());
            if(dead<2){
                ((TextView)findViewById(R.id.dead)).setText(Integer.toString(dead+1));
                ((TextView)findViewById(R.id.hp)).setText(Integer.toString(5));
            }else{
                ((TextView)findViewById(R.id.dead)).setText("3");
                ((TextView)findViewById(R.id.hp)).setText("0");
                AlertDialog alertDialog = new AlertDialog.Builder(Deckofcards.this).create();
                alertDialog.setTitle("Игра окончена");
                alertDialog.setMessage("Вы проиграли! Не отчаивайтесь, это не конец света, есть же некромантия!");
                alertDialog.setIcon(R.drawable.end_dead);
                alertDialog.setButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Deckofcards.this, StartActivity.class);
                        startActivity(i);
                    }
                });
                alertDialog.show();
            }
        }else{
            ((TextView)findViewById(R.id.hp)).setText(Integer.toString(hp));
        }
    }

    public void createArrayListOfCards(ArrayList main_cards){
        main_cards.add(R.drawable.d_darkness_3);
        main_cards.add(R.drawable.d_darkness_4);

        main_cards.add(R.drawable.d_element_1);
        main_cards.add(R.drawable.d_element_2);

        main_cards.add(R.drawable.d_illusion_4);

        main_cards.add(R.drawable.d_nature_1);
        main_cards.add(R.drawable.d_nature_2);
        main_cards.add(R.drawable.d_nature_3);

        main_cards.add(R.drawable.i_element_2);
        main_cards.add(R.drawable.i_element_3);

        main_cards.add(R.drawable.i_nature_4);
        main_cards.add(R.drawable.i_nature_5);

        main_cards.add(R.drawable.i_secret_4);

        main_cards.add(R.drawable.k_darkness_1);
        main_cards.add(R.drawable.k_darkness_2);

        main_cards.add(R.drawable.k_nature_1);
        main_cards.add(R.drawable.k_nature_4);

        main_cards.add(R.drawable.k_secret_1);
    }
    public void createArrayListOfI(ArrayList main_cards){
        main_cards.add(R.drawable.i_element_2);
        main_cards.add(R.drawable.i_element_3);
        main_cards.add(R.drawable.i_nature_4);
        main_cards.add(R.drawable.i_nature_5);
        main_cards.add(R.drawable.i_secret_4);
    }
    public void createArrayListOfK(ArrayList main_cards){
        main_cards.add(R.drawable.k_darkness_1);
        main_cards.add(R.drawable.k_darkness_2);
        main_cards.add(R.drawable.k_nature_1);
        main_cards.add(R.drawable.k_nature_4);
        main_cards.add(R.drawable.k_secret_1);
    }
    public void createArrayListOfD(ArrayList main_cards){
        main_cards.add(R.drawable.d_darkness_3);
        main_cards.add(R.drawable.d_darkness_4);
        main_cards.add(R.drawable.d_element_1);
        main_cards.add(R.drawable.d_element_2);
        main_cards.add(R.drawable.d_illusion_4);
        main_cards.add(R.drawable.d_nature_1);
        main_cards.add(R.drawable.d_nature_2);
        main_cards.add(R.drawable.d_nature_3);
    }

    public void cleanTable(){
        fstcard.setImageResource(R.drawable.i_card);
        seccard.setImageResource(R.drawable.k_card);
        thirdcard.setImageResource(R.drawable.d_card);
        ((ImageView)findViewById(R.id.fstcard2)).setImageDrawable(null);
        ((ImageView)findViewById(R.id.seccard2)).setImageDrawable(null);
        ((ImageView)findViewById(R.id.thirdcard2)).setImageDrawable(null);
        leave_card=0;
    }
}
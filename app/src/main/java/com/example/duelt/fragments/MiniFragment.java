package com.example.duelt.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.duelt.Calculation;
import com.example.duelt.Minigame;
import com.example.duelt.R;
import com.example.duelt.StatesView;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.PetModel;
import com.example.duelt.popWindows.ShopPopWindow;

public class MiniFragment extends Fragment {
    private StatesView hungrinessState;
    private StatesView moodState;
    private StatesView expState;
    private TextView petLvNum;
    private TextView petName;
    private TextView currency;
    private DatabaseHelper petDatabaseHelper;
    private PetModel petmodel;
    final Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;

    final Handler handler2 = new Handler(Looper.getMainLooper());

    AnimationDrawable catAnimation;
    Animation catAnimation2;


    ImageButton imageButton;
    static boolean needtochangAnimation = false;
    static boolean ChangedAnimation = false;
    static boolean isHappy = true;
    public MiniFragment(){
        //Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petDatabaseHelper = new DatabaseHelper(getActivity());
        petmodel = petDatabaseHelper.getCurrentStat();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.activity_mini,container,false);

        imageButton = rootView.findViewById(R.id.ani_cat);
        imageButton.setBackgroundResource(R.drawable.cat_animation_1);
        catAnimation = (AnimationDrawable) imageButton.getBackground();
        catAnimation.start();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AnimationDrawable)(imageButton.getBackground())).stop();
                imageButton.setEnabled(false);
                imageButton.setBackgroundDrawable(null);
                imageButton.setBackgroundResource(R.drawable.cat_animation_2);
                catAnimation = (AnimationDrawable) imageButton.getBackground();
                catAnimation.start();


                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((AnimationDrawable)(imageButton.getBackground())).stop();
                        imageButton.setBackgroundDrawable(null);
                        imageButton.setBackgroundResource(R.drawable.cat_animation_1);
                        catAnimation = (AnimationDrawable) imageButton.getBackground();
                        catAnimation.start();
                        imageButton.setEnabled(true);
                    }
                },3300);
            }
        });


        initState(rootView);
        //Buttons in Fragments should be written here
        // ↓↓↓↓↓↓↓↓

        //dataTesting button function
        Button btn_dataTesting = (Button) rootView.findViewById(R.id.data_testing);
        btn_dataTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Minigame.class);
                startActivity(i);
            }
        });

        Button btn_shoppage = rootView.findViewById(R.id.mini_shop_button);
        btn_shoppage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ShopPopWindow.class);
                startActivity(i);
            }
        });


        Button btn_feed = rootView.findViewById(R.id.btn_mini_feed);
        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feed()) {
                    ((AnimationDrawable) (imageButton.getBackground())).stop();
                    imageButton.setEnabled(false);
                    imageButton.setBackgroundDrawable(null);
                    imageButton.setBackgroundResource(R.drawable.cat_animation_3);
                    catAnimation = (AnimationDrawable) imageButton.getBackground();
                    catAnimation.start();


                    handler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((AnimationDrawable) (imageButton.getBackground())).stop();
                            imageButton.setBackgroundDrawable(null);
                            imageButton.setBackgroundResource(R.drawable.cat_animation_1);
                            catAnimation = (AnimationDrawable) imageButton.getBackground();
                            catAnimation.start();
                            imageButton.setEnabled(true);
                        }
                    }, 4300);
                }
            }
        });

        Button btn_play = rootView.findViewById(R.id.btn_mini_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play()){
                ((AnimationDrawable) (imageButton.getBackground())).stop();
                imageButton.setEnabled(false);
                imageButton.setBackgroundDrawable(null);
                imageButton.setBackgroundResource(R.drawable.cat_animation_4);
                catAnimation = (AnimationDrawable) imageButton.getBackground();
                catAnimation.start();


                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((AnimationDrawable) (imageButton.getBackground())).stop();
                        imageButton.setBackgroundDrawable(null);
                        imageButton.setBackgroundResource(R.drawable.cat_animation_1);
                        catAnimation = (AnimationDrawable) imageButton.getBackground();
                        catAnimation.start();
                        imageButton.setEnabled(true);
                    }
                }, 4100);
            }
        }
    });







        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        runnable = new Runnable(){
            @Override
            public void run(){
                if(isHappy && updateState()) {
                    isHappy = false;
                    ((AnimationDrawable) (imageButton.getBackground())).stop();
                    imageButton.setEnabled(false);
                    imageButton.setBackgroundDrawable(null);
                    imageButton.setBackgroundResource(R.drawable.cat_animation_5);
                    catAnimation = (AnimationDrawable) imageButton.getBackground();
                    catAnimation.start();
                } else if (!isHappy && !updateState()) {
                    isHappy = true;
                    ((AnimationDrawable) (imageButton.getBackground())).stop();
                    imageButton.setEnabled(false);
                    imageButton.setBackgroundDrawable(null);
                    imageButton.setBackgroundResource(R.drawable.cat_animation_1);
                    catAnimation = (AnimationDrawable) imageButton.getBackground();
                    catAnimation.start();
                }
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private boolean updateState() {
        Calculation.calculateHungerAndMood(getActivity());
        petmodel = petDatabaseHelper.getCurrentStat();

        //update hungriness state
        int currentHungry = petmodel.getHungriness();
        hungrinessState.setCurrentCount(currentHungry);

        //update mood state
        int currentMood = petmodel.getMood();
        moodState.setCurrentCount(currentMood);

        //update level text
        int level_text = petmodel.getLv();
        petLvNum.setText("LEVEL:  "+level_text);
        //update exp state
        int currentExp = petmodel.getExp();
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(level_text));
        expState.setCurrentCount(currentExp);
        //update name
        petName.setText(petmodel.getName());
        //update currency
        currency.setText("Currency: " + petDatabaseHelper.getCurrency());

        if(currentMood < 30){ return true; }
            else {return false;}
        }


    private void initState(View view) {

        petDatabaseHelper = new DatabaseHelper(view.getContext());
        petmodel = petDatabaseHelper.getCurrentStat();

        //init  HungrinessState bar
        hungrinessState = view.findViewById(R.id.hungrinessState);
        hungrinessState.setMaxCount(100);
        hungrinessState.setColor(Color.RED);
        int currentHungry = petmodel.getHungriness();
        hungrinessState.setCurrentCount(currentHungry);

        //init MoodState bar
        moodState = view.findViewById(R.id.moodState);
        moodState.setMaxCount(100);
        moodState.setColor(Color.BLUE);
        int currentMood = petmodel.getMood();
        moodState.setCurrentCount(currentMood);

        petLvNum = view.findViewById(R.id.mini_level_text);
        int level_text = petmodel.getLv();
        petLvNum.setText("LEVEL:  "+level_text);

        //init MoodState bar
        expState = view.findViewById(R.id.expState);
        expState.setMaxCount(petDatabaseHelper.getExpForLevelUp(level_text));
        expState.setColor(Color.GREEN);
        int currentExp = petmodel.getExp();
        expState.setCurrentCount(currentExp);

        petName = view.findViewById(R.id.mini_petname);
        petName.setText(petmodel.getName());

        currency = view.findViewById(R.id.mini_level_currency);
        currency.setText("Currency: " + petDatabaseHelper.getCurrency());
    }

    private boolean play(){
        int toyAmount= petDatabaseHelper.getToy();
        if (toyAmount>0) {
            petmodel = petDatabaseHelper.getCurrentStat() ;
            petmodel.play();
            moodState.setCurrentCount(petmodel.getMood());
            //update pet sqlite
            petDatabaseHelper.updateData(petmodel);
            toyAmount--;
            petDatabaseHelper.updateToy(toyAmount);
            return true;
        }
        else {
            Toast.makeText(getContext(), "You have no more toy for your pet!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean feed(){
        int foodAmount= petDatabaseHelper.getFood();
        if (foodAmount>0) {
            PetModel petmodel = new PetModel(getContext());
            petmodel.feed();
            hungrinessState.setCurrentCount(petmodel.getHungriness());
            //update pet sqlite
            petDatabaseHelper.updateData(petmodel);
            foodAmount--;
            petDatabaseHelper.updateFood(foodAmount);

            return true;
        }
        else {
            Toast.makeText(getContext(), "You have no more food to feed!", Toast.LENGTH_SHORT).show();

            return false;
        }

    }


}

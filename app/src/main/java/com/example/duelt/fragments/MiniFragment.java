package com.example.duelt.fragments;

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
import com.example.duelt.HintHelper;
import com.example.duelt.R;
import com.example.duelt.Setting;
import com.example.duelt.StatesView;
import com.example.duelt.db.DatabaseHelper;
import com.example.duelt.db.PetModel;
import com.example.duelt.popWindows.PopWindow;
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

    final private String FIRST_TIME_KEY = "MINI_FIRST_TIME_KEY";


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

        //Check for hint btn
        ImageButton btn_hint = (ImageButton) rootView.findViewById(R.id.btn_mini_hint1);
        ImageButton btn_hint2 = (ImageButton) rootView.findViewById(R.id.btn_mini_hint2);
        HintHelper hh = new HintHelper();
        hh.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint);
        HintHelper hh2 = new HintHelper();
        hh2.checkFirstTime(rootView.getContext(),FIRST_TIME_KEY,btn_hint2);

        initState(rootView);

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
        Button hungerTestBtn = getActivity().findViewById(R.id.mini_test_hunger);
        Button moodTestBtn = getActivity().findViewById(R.id.mini_test_mood);
        if (!Setting.testMode){
            hungerTestBtn.setVisibility(View.INVISIBLE);
            moodTestBtn.setVisibility(View.INVISIBLE);
        }
        else{
            hungerTestBtn.setVisibility(View.VISIBLE);
            moodTestBtn.setVisibility(View.VISIBLE);
            hungerTestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    petmodel.setHungriness(petmodel.getHungriness()-5);
                    petDatabaseHelper.updateData(petmodel);
                    updateState();
                }
            });
            moodTestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    petmodel.setMood(petmodel.getMood()-5);
                    petDatabaseHelper.updateData(petmodel);
                    updateState();
                }
            });
        }

        runnable = new Runnable(){
            @Override
            public void run(){
                if(isHappy && updateState()) {
                    isHappy = false;
                    ((AnimationDrawable) (imageButton.getBackground())).stop();
                    imageButton.setEnabled(true);
                    imageButton.setBackgroundDrawable(null);
                    imageButton.setBackgroundResource(R.drawable.cat_animation_5);
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
                                    imageButton.setBackgroundResource(R.drawable.cat_animation_5);
                                    catAnimation = (AnimationDrawable) imageButton.getBackground();
                                    catAnimation.start();
                                    imageButton.setEnabled(true);
                                }
                            },3300);
                        }
                    });
                } else if (!isHappy && !updateState()) {
                    isHappy = true;
                    ((AnimationDrawable) (imageButton.getBackground())).stop();
                    imageButton.setEnabled(true);
                    imageButton.setBackgroundDrawable(null);
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
                }


                if (isPetDead() == true){
                    Intent intent = new Intent(getActivity(), PopWindow.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("Table", "Mini");
                    startActivity(intent);
                }




                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 0);
    }


    public boolean isPetDead(){
        Calculation.calculateHungerAndMood(getActivity());
        petmodel = petDatabaseHelper.getCurrentStat();

        int currentHungry = petmodel.getHungriness();

        if(currentHungry<= 0){
            return true;
        } else {return false;}
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
        petLvNum.setText("Lv: "+level_text);
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
        petLvNum.setText("Lv: "+level_text);

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
        int mood = petDatabaseHelper.getCurrentStat().getMood();
        if (toyAmount>0) {
            if (mood==100){
                Toast.makeText(getContext(), "Your pet is happy enough!", Toast.LENGTH_SHORT).show();
                return false;
            }
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
        int hunger = petDatabaseHelper.getCurrentStat().getHungriness();
        if (foodAmount>0) {
            if (hunger==100){
                Toast.makeText(getContext(), "Your pet is full!", Toast.LENGTH_SHORT).show();
                return false;
            }
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

package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.duelt.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleTestActivity extends AppCompatActivity {

    com.google.android.material.button.MaterialButton btn_addEvent;
    List<RelativeLayout> layoutList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_schedule);

        //Initialize the relative layouts for updateEventCards usage
        layoutList = new ArrayList<RelativeLayout>(
                Arrays.asList(findViewById(R.id.sundayRelativeLayout),findViewById(R.id.mondayRelativeLayout),findViewById(R.id.tuesdayRelativeLayout),findViewById(R.id.wednesdayRelativeLayout)
                        ,findViewById(R.id.thursdayRelativeLayout),findViewById(R.id.fridayRelativeLayout),findViewById(R.id.saturdayRelativeLayout)));

        btn_addEvent = (com.google.android.material.button.MaterialButton)findViewById(R.id.btn_addEvent);
        btn_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ScheduleEventAddWindow.class);
                startActivity(i);
            }
        });

        //Button for testing purpose
        Button btn_add = (Button) findViewById(R.id.btn_addCard);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置layout出现位置.......
                RelativeLayout mondayLayout = findViewById(R.id.mondayRelativeLayout);
                CardView card = new CardView(ScheduleTestActivity.this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(175));
                params.leftMargin=dpToPx(2);
                params.topMargin=dpToPx(2);
                params.bottomMargin=dpToPx(2);
                params.rightMargin=dpToPx(2);
                card.setCardBackgroundColor(Color.parseColor("#0000FF"));
                mondayLayout.addView(card,params);

                CardView card2 = new CardView(ScheduleTestActivity.this);
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(175));
                params1.leftMargin=dpToPx(2);
                params1.topMargin=dpToPx(180);
                params1.bottomMargin=dpToPx(2);
                params1.rightMargin=dpToPx(2);
                mondayLayout.addView(card2,params1);

                TextView eventText = new TextView(ScheduleTestActivity.this);
                eventText.setText("Physics");
                eventText.setGravity(Gravity.CENTER);
                card.addView(eventText);
                //card2.addView(eventText);

            }
        });

        createAllEventCards();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEventCards();
    }

    //updateEventCards will remove all views and recreate them(to prevent overlapping and getting the new added in)
    private void updateEventCards(){
        for (RelativeLayout layout:layoutList
             ) {
            //Remove all views except the first one (the line divider)
            layout.removeViews(1,layout.getChildCount()-1);
        }
        createAllEventCards();
    }

    //Create cards for all event within the database
    private void createAllEventCards(){
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        List<WeeklyScheduleModel> eventList = databaseHelper.getWeeklySchedule();
        for(int i = 0; i < eventList.size(); i++){
            createOneEventCard(eventList.get(i));
        }
    }

    private void createOneEventCard(WeeklyScheduleModel model){
        //First locate the corresponding weekdayLayout
        RelativeLayout weekDayLayout = findViewById(getLayoutByWeekDay(model.getWeekDay()));
        //Create CardView for the event
        CardView card = new CardView(this);
        int startPosition = model.getStartPosition();
        int endPosition = model.getEndPosition();
        //blocks calculates how many hour blocks the event needs.
        int blocks = endPosition - startPosition;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,dpToPx(60 * blocks - 5));
        params.leftMargin=dpToPx(2);
        params.topMargin=dpToPx(2 + 60 * startPosition);        //topMargin have to calculate to prevent overlap. 60dp is the height for a hour block.
        params.bottomMargin=dpToPx(2);
        params.rightMargin=dpToPx(2);
        card.setCardBackgroundColor(getColorCodebyString(model.getColor()));
        weekDayLayout.addView(card,params);

        //Create content for card
        TextView eventText = new TextView(ScheduleTestActivity.this);
        eventText.setText(model.getEventName());
        eventText.setGravity(Gravity.CENTER);
        card.addView(eventText);
    }

    //Because LayoutParams works in px. In order to convert dp into px, we need the following conversion
    public int dpToPx(int px){
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (px * density + 0.5f);
    }

    //Converting weekDay String to layout ID
    private int getLayoutByWeekDay(String weekDay){
        switch (weekDay){
            default:
                return R.id.sundayRelativeLayout;
            case "Monday":
                return R.id.mondayRelativeLayout;
            case "Tuesday":
                return R.id.tuesdayRelativeLayout;
            case "Wednesday":
                return R.id.wednesdayRelativeLayout;
            case "Thursday":
                return R.id.thursdayRelativeLayout;
            case "Friday":
                return R.id.fridayRelativeLayout;
            case "Saturday":
                return R.id.saturdayRelativeLayout;
        }
    }

    //Converting color String to color code
    private int getColorCodebyString(String color){
        switch (color){
            default:
                return Color.parseColor("#FFFFFF");
            case "Blue":
                return Color.parseColor("#77FDE3");
            case "Pink":
                return Color.parseColor("#FF6FA8");
            case "Yellow":
                return Color.parseColor("#EFFE74");
            case "Green":
                return Color.parseColor("#B3FE74");
            case "Purple":
                return Color.parseColor("#EB6FFF");
        }
    }

}
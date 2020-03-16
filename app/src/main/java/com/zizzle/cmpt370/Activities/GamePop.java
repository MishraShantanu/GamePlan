package com.zizzle.cmpt370.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zizzle.cmpt370.Model.CurrentUserInfo;
import com.zizzle.cmpt370.Model.League;
import com.zizzle.cmpt370.Model.LeagueInfo;
import com.zizzle.cmpt370.Model.MemberInfo;
import com.zizzle.cmpt370.Model.Storage;
import com.zizzle.cmpt370.Model.Team;
import com.zizzle.cmpt370.Model.TeamInfo;
import com.zizzle.cmpt370.R;

import java.util.ArrayList;
import java.util.Calendar;

public class GamePop extends Activity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating the pop-up =====================================================================
        setContentView(R.layout.game_popup);


        // Spinner =================================================================================
        final TeamInfo currentTeam = (TeamInfo) getIntent().getSerializableExtra("TEAM_INFO");
        LeagueInfo leagueInfo = new LeagueInfo(currentTeam.getLeagueName());

        DatabaseReference leagueReference = FirebaseDatabase.getInstance().getReference().child("Leagues").child(leagueInfo.getDatabaseKey()).child("teamsInfoMap");
        leagueReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<TeamInfo> teamInfos = new ArrayList<>();
                ArrayList<String> teamNames = new ArrayList<>();

                // Gather all teams inside the same league, excluding user team.
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    TeamInfo currentTeamInfo = ds.getValue(TeamInfo.class);
                    teamInfos.add(currentTeamInfo);
                    teamNames.add(currentTeamInfo.getName());
                }
                teamInfos.remove(currentTeam);
                teamNames.remove(currentTeam.getName());

                // Create a Spinner
                Spinner againstTeam = findViewById(R.id.team_spinner);
                againstTeam.setOnItemSelectedListener(GamePop.this);

                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(GamePop.this, R.layout.game_spinner, teamNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                againstTeam.setAdapter(dataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_popup);

        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        txtDate = findViewById(R.id.in_date);
        txtTime = findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }


    // Date and Time Picker
    @Override
    public void onClick(View v) {
        // Date picker
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            txtDate.setTextColor(ContextCompat.getColor(GamePop.this, R.color.colorText));

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        // Time picker
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            // Add a zero to pad the time if less than ten minutes.
                            if (minute < 10) txtTime.setText(hourOfDay + ":0" + minute);
                            else txtTime.setText(hourOfDay + ":" + minute);

                            txtTime.setTextColor(ContextCompat.getColor(GamePop.this, R.color.colorText));
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }


    // Unused
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
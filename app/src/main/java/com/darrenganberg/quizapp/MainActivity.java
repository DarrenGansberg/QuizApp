package com.darrenganberg.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private int count = 0;
    private String msg = "Bundle was not passed";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView txt = findViewById(R.id.txtUsername);

        if (savedInstanceState != null)
        {
            msg = savedInstanceState.getString("message");

        }
    }

    public void onStartButtonClicked(View view)
    {
        TextView txt = findViewById(R.id.txtUsername);
        String userString = txt.getText().toString();
        if(userString.equals("") || userString.equals("Your name"))
        {
            String msg = "Please enter your name before starting the quiz";
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, AnswerQuestions.class);
        intent.putExtra("username", userString);
        startActivity(intent);
    }
}
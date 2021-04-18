package com.darrenganberg.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ViewQuizResult extends AppCompatActivity {

    private String username;
    private Integer correctlyAnswered;
    private Integer questionsAttempted;

    public void onBtnClick_NewQuiz(View view)
    {
        Intent intent = new Intent();
        intent.putExtra("username", username);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onBtnClick_Finish(View view)
    {
        this.finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_quiz_result);

        if (savedInstanceState != null)
        {
            restoreActivityState(savedInstanceState);
        }
        else
        {
            Intent intent = getIntent();
            init(intent);
        }
        displayUsername();
        displayQuizScore();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", username);
        outState.putInt("questionsAttempted", questionsAttempted);
        outState.putInt("correctlyAnswered", correctlyAnswered);
    }

    protected void init(Intent intent)
    {
        username = intent.getStringExtra("username");
        correctlyAnswered = intent.getIntExtra("correctlyAnswered", 0);
        questionsAttempted = intent.getIntExtra("questionsAttempted", 0);
    }

    protected void restoreActivityState(Bundle savedInstanceState)
    {
        username = savedInstanceState.getString("username");
        questionsAttempted = savedInstanceState.getInt("questionsAttempted");
        correctlyAnswered = savedInstanceState.getInt("correctlyAnswered");
    }

    private void displayUsername()
    {
        TextView txtUsername = findViewById(R.id.txtUsername);
        String s = getString(R.string.view_quiz_result_title, username);
        txtUsername.setText(s);
    }

    private void displayQuizScore()
    {
        TextView txtResult = findViewById(R.id.txtQuizResult);
        String s = getString(R.string.quiz_score_text, correctlyAnswered, questionsAttempted);
        txtResult.setText(s);
    }
}
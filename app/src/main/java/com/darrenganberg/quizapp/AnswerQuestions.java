package com.darrenganberg.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Vector;

public class AnswerQuestions extends AppCompatActivity {

    //A repository (i.e. collection) of questions for a quiz.
    private class QuizQuestionRepository
    {
        protected QuizQuestion getQuestion(int questionId) throws IllegalArgumentException
        {

            QuizQuestion q;
            switch(questionId)
            {
                case 1:
                    q = getQuestion1();
                    break;
                case 2:
                    q = getQuestion2();
                    break;
                case 3:
                    q = getQuestion3();
                    break;
                case 4:
                    q = getQuestion4();
                    break;
                case 5:
                    q = getQuestion5();
                    break;
                default:
                    throw new IllegalArgumentException("Invalid question id");

            }
            return q;
        }

        private QuizQuestion getQuestion1()
        {
            Vector<String> answers = new Vector<>(3);
            answers.add("1986");
            answers.add("1960");
            answers.add("1979");
            String text = "Darren Gansberg was born in:";
            return new QuizQuestion("Question 1", answers, text, 2);
        }

        private QuizQuestion getQuestion2()
        {
            Vector<String> answers = new Vector<>(3);
            answers.add("2013");
            answers.add("2016");
            answers.add("2017");
            String text = "The Western Bulldogs won the AFL Premiership in:";
            return new QuizQuestion("Question 2", answers, text, 1);

        }

        private QuizQuestion getQuestion3()
        {
            Vector<String> answers = new Vector<>(2);
            answers.add("True");
            answers.add("False");
            String text = "Cats have fur:";
            return new QuizQuestion("Question 3", answers, text, 0);
        }

        private QuizQuestion getQuestion4()
        {
            Vector<String> answers = new Vector<>(2);
            answers.add("True");
            answers.add("False");
            String text = "Darth Vader is Luke's father";
            return new QuizQuestion("Question 4", answers, text, 0);
        }

        private QuizQuestion getQuestion5()
        {
            Vector<String> answers = new Vector<>(3);
            answers.add("Trinity");
            answers.add("LeBron James");
            answers.add("Neo");
            String text = "Which of the following is not a character in the Matrix:";
            return new QuizQuestion("Question 5", answers, text, 1);

        }

    }


    private class QuizQuestion
    {
        private String title;
        private String text;
        private List<String> answers;
        private Integer correctAnswer;


        public QuizQuestion(@NonNull String title, @NonNull List<String> answers, @NonNull String questionText, Integer correctAnswer)
        {
            if (correctAnswer < 0)
                throw new IllegalArgumentException("The correct answer must be greater than 0");
            this.title = title;
            this.text = questionText;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }

        public String getTitle()
        {
            return title;
        }

        private String getText()
        {
            return text;
        }

        public List<String> getAnswers()
        {
            return answers;
        }

        public Integer getCorrectAnswer()
        {
            return correctAnswer;
        }
    }


    public static final Integer REQUEST_QUIZ_SCORE_DISPLAY = 1;

    //The user's name
    private String username;

    //the number of questions that the user is to attempt
    private Integer questionsToAttempt = 5;
    //the number of correctly answered questions by the user.
    private Integer correctlyAnswered = 0;

    //the answer for the current question that is the correct answer within the questions answer set
    //private Integer correctAnswer = 0; //remove this? because it will be held by the question.
    //the answer within the answer set selected by the user.
    private int selectedAnswer = -1;

    //the number//id of the question within the quiz i.e. 1 == the first question or question 1.
    private Integer questionId = 1;
    private QuizQuestion question;

    //UI elements
    private TextView txtWelcomeMessage;
    //Reference to the TextView that the user has selected as the answer.
    private AppCompatTextView txtSelectedAnswer;
    //References to the TextViews that display the answers for a question.
    private List<AppCompatTextView> answerViews;
    //Reference to the progress bar indicating progress through the quiz.
    private ProgressBar quizProgress;
    //Reference to the TextView displaying the title of the question.
    private AppCompatTextView txtQuestionTitle;
    //Reference to the TextView that displays the text of the question.
    private AppCompatTextView txtQuestionText;

    //An Enumeration used to designate the mode of a button that is used as a
    //submit and next button (i.e. dual roles)
    private enum ButtonMode { NONE, SUBMIT, NEXT }

    //The current mode of the button that is used as a submit/next button.
    private ButtonMode buttonMode;

    //A reference to the button that is used as a submit/next button.
    private Button btnSubmitNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_questions);

        //restore activity state, as required, to ensure users experience
        //is consistent with the UI state.
        if (savedInstanceState != null)
        {
            restoreActivityState(savedInstanceState);
        }
        else
        {
            init(getIntent());
        }

        display();
    }

    private void display() {
        displayWelcomeMessage();
        displayProgress();
        displayQuestion();
        displaySubmitNextButton();
        if (buttonMode == ButtonMode.SUBMIT && selectedAnswer != -1) {
            SelectAnswer(answerViews.get(selectedAnswer));
        }
        else if (buttonMode == ButtonMode.NEXT)
        {
            updateAnswers();
        }
    }

    private void displaySubmitNextButton() {

        String btnText;
        if (buttonMode == ButtonMode.NEXT)
        {
            btnText = getString(R.string.next_button_text);
        }
        else
        {
            btnText = getString(R.string.submit_button_text);
        }
        btnSubmitNext.setText(btnText);
    }

    //Converts an integer value to the equivalent enumeration constant value member of
    // ButtonMode.
    private ButtonMode getMode(Integer value)
    {
        switch (value)
        {
            case 1:
                return ButtonMode.SUBMIT;
            case 2:
                return ButtonMode.NEXT;
            default:
                return ButtonMode.NONE;
        }
    }

    //Converts an enumeration constant value within ButtonMode integer
    //value to the equivalent enumeration constant value.
    private Integer getMode(ButtonMode mode)
    {
        switch (mode)
        {
            case SUBMIT:
                return 1;
            case NEXT:
                return 2;
            default:
                return 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displaySubmitNextButton();
    }

    @Override
    protected void onSaveInstanceState (@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("username", username);
        outState.putInt("questionsToAttempt", questionsToAttempt);
        outState.putInt("questionId", questionId);
        outState.putInt("selectedAnswer", selectedAnswer);
        outState.putInt("buttonMode", getMode(buttonMode));
        outState.putInt("correctlyAnswered", correctlyAnswered);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( (requestCode == REQUEST_QUIZ_SCORE_DISPLAY) && (resultCode == RESULT_OK))
        {
            init(data);
            display();
        }
    }

    private void restoreActivityState(Bundle savedInstanceState)
    {
        getUIElements();
        username = savedInstanceState.getString("username");
        questionsToAttempt = savedInstanceState.getInt("questionsToAttempt");
        questionId = savedInstanceState.getInt("questionId");
        selectedAnswer = savedInstanceState.getInt("selectedAnswer");
        buttonMode = getMode(savedInstanceState.getInt("buttonMode"));
        correctlyAnswered = savedInstanceState.getInt("correctlyAnswered");

        QuizQuestionRepository questions = new QuizQuestionRepository();
        question = questions.getQuestion(questionId);
    }

    protected void init(@Nullable Intent intent)
    {
        getUIElements();
        buttonMode = ButtonMode.SUBMIT;
        questionsToAttempt = 5;
        correctlyAnswered = 0;
        selectedAnswer = -1; //user hasn't selected an answer
        questionId = 1;
        if (intent != null)
        {
            username = intent.getStringExtra("username");
        }
        else
        {
            username = "unknown";
        }

        //grab the data for the quiz question that is displayed to the user.
        QuizQuestionRepository questions = new QuizQuestionRepository();
        question = questions.getQuestion(questionId);


        quizProgress.setMax(questionsToAttempt);
    }

    private void getUIElements()
    {
        //ensure that we have references to the UI elements that need manipulation
        quizProgress = findViewById(R.id.quizProgress);
        txtQuestionTitle = findViewById(R.id.txtQuestionTitle);
        txtQuestionText = findViewById(R.id.txtQuestionText);
        btnSubmitNext = findViewById(R.id.btnSubmitNext);
        txtWelcomeMessage = findViewById(R.id.txtWelcomeMessage);
    }

    private void displayProgress()
    {
        updateProgressBarLabel();
        updateProgressBar();
    }

    private void displayWelcomeMessage()
    {
        txtWelcomeMessage.setText(getString(R.string.welcome_message,username));
    }

    public void onSelectAnswer(View view)
    {
        Button btn = findViewById(R.id.btnSubmitNext);
        CharSequence buttonMode = btn.getText();
        if (buttonMode.equals("Submit"))
        {
            if (selectedAnswer != -1)
            {
                UnSelect(answerViews.get(selectedAnswer));
            }
            SelectAnswer(view);
        }
    }

    private void UnSelect(AppCompatTextView view)
    {
       view.setBackground(AppCompatResources.getDrawable(this, R.drawable.answer_unselected_bg));
    }

    public void onSubmitNextButtonClicked(View view)
    {
        if (buttonMode == ButtonMode.SUBMIT)
        {
            //display message to the user to prompt them to select an
            //answer if they hit submit without selecting an answer
            if (selectedAnswer == -1)
            {
                Toast.makeText(this,"Please select an answer.", Toast.LENGTH_SHORT).show();
                return;
            }
            updateAnswers();
            changeToNextButton((Button)view);
        }
        else if (buttonMode == ButtonMode.NEXT)
        {
            if (questionId < questionsToAttempt)
            {
                nextQuestion();
                updateProgressBar();
                changeToSubmitButton((Button)view);
            }
            else
            {
                Intent intent = new Intent(this, ViewQuizResult.class);
                intent.putExtra("username", username);
                intent.putExtra("questionsAttempted", questionsToAttempt);
                intent.putExtra("correctlyAnswered", correctlyAnswered);
                startActivityForResult(intent, REQUEST_QUIZ_SCORE_DISPLAY);
            }
        }
    }

    private void nextQuestion()
    {
        questionId++;
        updateProgressBarLabel();
        selectedAnswer = -1;
        LinearLayoutCompat l = findViewById(R.id.answersLayout);
        l.removeAllViews();
        QuizQuestionRepository questions = new QuizQuestionRepository();
        question = questions.getQuestion(questionId);
        displayQuestion();
    }

    private void changeToNextButton(@NonNull Button button)
    {
        buttonMode = ButtonMode.NEXT;
        String s = getString(R.string.next_button_text);
        button.setText(s);
    }

    private void changeToSubmitButton(@NonNull Button button)
    {
        buttonMode = ButtonMode.SUBMIT;
        String s = getString(R.string.submit_button_text);
        button.setText(s);
    }

    private void updateAnswers()
    {
        if (selectedAnswer == question.getCorrectAnswer())
        {
            correctlyAnswered++;
            displayCorrect(answerViews.get(selectedAnswer));
        }
        else
        {
            displayIncorrect(answerViews.get(selectedAnswer));
            displayCorrect(answerViews.get(question.getCorrectAnswer()));
        }
    }

    private void updateProgressBarLabel()
    {
        TextView label = findViewById(R.id.lblQuizProgress);
        String s = getString(R.string.progress_bar_text, questionId, questionsToAttempt);
        label.setText(s);
    }

    private void updateProgressBar()
    {
        quizProgress.setProgress(questionId);
    }

    protected void SelectAnswer(View view)
    {
        view.setBackground(AppCompatResources.getDrawable(this, R.drawable.answer_selected_bg));
        txtSelectedAnswer = (AppCompatTextView)view;
        selectedAnswer = answerViews.indexOf(view);
    }

    private void displayCorrect(View answer)
    {
        answer.setBackground(AppCompatResources.getDrawable(this, R.drawable.correct_answer));

    }

    private void displayIncorrect(View answer)
    {
        answer.setBackground(AppCompatResources.getDrawable(this, R.drawable.wrong_answer));
    }

    private void displayQuestion()
    {
        txtQuestionTitle.setText(question.getTitle());
        txtQuestionText.setText(question.getText());
        displayAnswers(question);
    }

    private void displayAnswers(QuizQuestion question)
    {
        LinearLayoutCompat answers = findViewById(R.id.answersLayout);
        answers.removeAllViews();
        List<String> answerStrings = question.getAnswers();
        if (answerViews == null)
        {
            answerViews = new Vector<>(answerStrings.size());
        }
        else
        {
            answerViews.clear();
        }
        for(int i = 0; i < answerStrings.size(); i++)
        {
            answerViews.add(AddAnswerTextView(answerStrings.get(i)));
        }
    }

    private AppCompatTextView AddAnswerTextView(String text)
    {
        //but you probably only need to do this once!
        LinearLayoutCompat layout = findViewById(R.id.answersLayout);
        //failure to set means that the answers aren't displayed centered within
        //the layout.
        layout.setGravity(Gravity.CENTER);
        AppCompatTextView view = new AppCompatTextView(this);
        view.setId(ViewCompat.generateViewId());
        layout.addView(view);


        LinearLayoutCompat.LayoutParams t = new LinearLayoutCompat.LayoutParams(layout.getLayoutParams());

        t.height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
        t.width = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
        t.setMargins(10,10,10,10);
        view.setLayoutParams(t);
        //cause the text within the answer text view to appear centered both vertically and
        //horizontally.
        view.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

        view.setText(text);
        view.setBackground(AppCompatResources.getDrawable(this,R.drawable.answer_unselected_bg));
        view.setOnClickListener(this::onSelectAnswer);
        return view;


    }

}


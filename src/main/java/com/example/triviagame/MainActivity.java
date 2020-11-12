package com.example.triviagame;

import Data.AnswerListAsyncResponse;
import Data.QuestionBank;
import Model.Question;
import Utils.Prefs;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private TextView counterText;
private Button trueButton;
private Button falseButton;
private ImageButton prevButton;
private ImageButton nextButton;
private TextView scoreText;
private TextView highScoreText;
private TextView questionText;


private int currentQuestionIndex = 0;
private int score = 0;

private Prefs prefs;
private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.question_textview);
        counterText = findViewById(R.id.counter_text);
        scoreText = findViewById(R.id.scoreTextView);
        highScoreText = findViewById(R.id.high_score);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);

        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        prefs = new Prefs(MainActivity.this);

        //Setting the state
        scoreText.setText("Current Score: " +score);
        highScoreText.setText("High Score: " +prefs.getHighScore());
        currentQuestionIndex = prefs.getState();

        //Call to API
         questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processedFinished(ArrayList<Question> questionArrayList) {

                questionText.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                counterText.setText(currentQuestionIndex+ "/"+questionArrayList.size());
            }
        });

    }

    //On Click Functions
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.prev_button:
                if(currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;

            case R.id.next_button:
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
                break;

            case R.id.true_button:
                checkAnswer(true);
                updateQuestion();
                break;

            case R.id.false_button:
                checkAnswer(false);
                updateQuestion();
                break;
        }
    }

    //Checks the user answer
    private void checkAnswer(boolean userChoice) {
        boolean isCorrect = questionList.get(currentQuestionIndex).getAnswerTrue();
        int toastMessageID = 0;

        if(userChoice == isCorrect) {
            addScore();
            fadeAnimation();
            toastMessageID = R.string.correct;
        } else {
            subtractScore();
            shakeAnimation();
            toastMessageID = R.string.wrong;
        }

        Toast.makeText(MainActivity.this, toastMessageID, Toast.LENGTH_SHORT).show();
    }

    //Adds 10 points to the score
    private void addScore() {
        score = (score + 10);
        scoreText.setText("Current Score: " + score);
    }

    //Subtracts 10 points from the score
    private void subtractScore() {
        if(score > 0) {
            score = (score - 10);
            scoreText.setText("Current Score: " +score);
        }
    }

    // Updates the UI after the user presses a button
    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionText.setText(question);
        counterText.setText(currentQuestionIndex+ "/"+questionList.size());
    }

    //Provides a "shake" animation if the user gets the question wrong
    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim);

        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
               cardView.setBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }

    //Provides a fade animation if the user gets the question right
    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    @Override
    protected void onPause() {
        prefs.saveHighScore(score);
        prefs.saveState(currentQuestionIndex);
        super.onPause();


    }
}
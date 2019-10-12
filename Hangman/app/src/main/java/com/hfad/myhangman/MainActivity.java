package com.hfad.myhangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements MyAsyncTask.communicate, SubmitAsyncTask.communicate,
        AnswerAsyncTask.communicate, HintAsyncTask.communicate {

    EditText text;
    TextView tv, chosen;
    String hangman, token, answer;
    int countWrong;
    ImageView img;
    Button start, guess, hint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        guess = findViewById(R.id.b);
        hint = findViewById(R.id.hint);
        chosen = findViewById(R.id.chosen);
        tv.setVisibility(View.INVISIBLE);
        setInvisible();
    }

    public void createGame(View v) {
        tv.setVisibility(View.VISIBLE);
        guess.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        hint.setVisibility(View.VISIBLE);
        img = (ImageView) findViewById(R.id.iv);
        img.setImageResource(R.drawable.stage0);
        countWrong = 0;
        try {
            URL url = new URL("http://hangman-api.herokuapp.com/hangman");
            new MyAsyncTask(this).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        start = (Button) findViewById(R.id.start);
        start.setVisibility(View.INVISIBLE);
    }


    public void onClick(View v) {
        answer = text.getText().toString();
        Pattern regex = Pattern.compile("[0-9]");
        Matcher regexMatcher = regex.matcher(answer);
        boolean digit=regexMatcher.find();
        if (!digit && answer.length()==1) {
            setChosen(answer);
            try {
                URL url = new URL("https://hangman-api.herokuapp.com/hangman?token=" + token + "&letter=" + answer);
                new SubmitAsyncTask(this).execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    public void setChosen(String letter) {
        chosen.setText(chosen.getText().toString() + " " + letter);
    }


    public void getHint(View v) {
        try {
            URL url = new URL("https://hangman-api.herokuapp.com/hangman/hint?token=" + token);
            new HintAsyncTask(this).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void setResponse(String h, String t) {
        hangman = h;
        token = t;
        tv.setText(h);
    }

    @Override
    public void setResponse(String h, String t, boolean cr) {
        hangman = h;
        token = t;
        if (!cr) handleWrongAnswer();
        if (!hangman.contains("_")) endGame();
        tv.setText(hangman);
    }

    public String getAnswer() {
        try {
            URL url = new URL("https://hangman-api.herokuapp.com/hangman?token=" + token);
            new AnswerAsyncTask(this).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setResponse(String h) {
        tv.setText(h);
    }

    public void handleWrongAnswer() {
        countWrong++;
        switch (countWrong) {
            case 1:
                img.setImageResource(R.drawable.stage1);
                break;
            case 2:
                img.setImageResource(R.drawable.stage2);
                break;
            case 3:
                img.setImageResource(R.drawable.stage3);
                break;
            case 4:
                img.setImageResource(R.drawable.stage4);
                break;
            case 5:
                img.setImageResource(R.drawable.stage5);
                break;
            case 6:
                img.setImageResource(R.drawable.stage6);
                break;
            case 7:
                img.setImageResource(R.drawable.stage7);
                endGame();
        }
    }

    public void endGame() {
        setInvisible();
        getAnswer();
        start.setVisibility(View.VISIBLE);
    }

    @Override
    public void setHint(String h) {
        text.setText(h);
    }

    public void setInvisible() {
        guess.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        hint.setVisibility(View.INVISIBLE);
    }
}

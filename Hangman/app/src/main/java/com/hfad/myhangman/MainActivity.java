package com.hfad.myhangman;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MyAsyncTask.communicate, SubmitAsyncTask.communicate {

    EditText text;
    TextView tv;
    String hangman, token;
    boolean correct;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.et);
        tv = findViewById(R.id.tv);


        try {
            URL url = new URL("http://hangman-api.herokuapp.com/hangman");
            new MyAsyncTask(this).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void onClick(View v) {
        String answer = text.getText().toString();
        try {
            URL url = new URL("https://hangman-api.herokuapp.com/hangman?token=" + token + "&letter=" + answer);
            new SubmitAsyncTask(this).execute(url);
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
        correct = cr;
        if (!cr) handleWrongAnswer();
        tv.setText(h);
    }

    public void handleWrongAnswer() {
        c++;
        if (c == 7) {
        }
        ImageView img = (ImageView) findViewById(R.id.iv);
        img.setImageResource(R.drawable.stage1);
        //iv.setImageDrawable("@android:drawable/stage1");

    }
}

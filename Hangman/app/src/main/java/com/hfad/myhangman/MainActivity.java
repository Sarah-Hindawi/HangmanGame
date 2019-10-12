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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements MyAsyncTask.communicate, SubmitAsyncTask.communicate,
        AnswerAsyncTask.communicate, HintAsyncTask.communicate {

    private EditText text;
    private TextView tv, chosen;
    private String hangman, token;
    private int countWrong;
    private ImageView img;
    private Button start, guess, hint;
    private List<String> chosenLetters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chosenLetters = new ArrayList<>();
        text = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        guess = findViewById(R.id.b);
        hint = findViewById(R.id.hint);
        chosen = findViewById(R.id.chosen);
        tv.setVisibility(View.INVISIBLE);
        setInvisible();
    }

    /**
     * Creates a new connection to hangman API to create a new game
     *
     * @param v the Start Game button
     */
    public void createGame(View v) {
        tv.setVisibility(View.VISIBLE);
        guess.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        hint.setVisibility(View.VISIBLE);
        img = (ImageView) findViewById(R.id.iv);
        img.setImageResource(R.drawable.stage0);
        // clears the arraylist that holds the chosen letters when the user wants to play again
        chosenLetters.clear();
        setChosen("");
        // clears the last letter entered into the EditText for the new game
        text.setText("");
        // numbers of wrong letters chosen by user
        countWrong = 0;
        try {
            // initial connection
            URL url = new URL("http://hangman-api.herokuapp.com/hangman");
            new MyAsyncTask(this).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // makes the Start Game button invisible until the user finishes the current word
        start = (Button) findViewById(R.id.start);
        start.setVisibility(View.INVISIBLE);
    }


    public void onClick(View v) {
        // get the chosen letter, converted to lower case so that A or a are stored once in the arraylist
        String answer = text.getText().toString().toLowerCase().trim();
        // if the character is not a letter
        Pattern regex = Pattern.compile("[^a-z]");
        Matcher regexMatcher = regex.matcher(answer);
        // will return true it found a non-letter character
        boolean digit = regexMatcher.find();
        // if finding a non letter character was not true (all letters) and the user entered no more than one character
        if (!digit && answer.length() == 1) {
            // if the arraylist is not storing the letter entered already then add it to the arraylist
            if (!chosenLetters.contains(answer)) chosenLetters.add(answer);
            // the method that displays the elements in the arraylist
            setChosen(answer);
            try {
                // make a connection with the provided letter and number of the game
                URL url = new URL("https://hangman-api.herokuapp.com/hangman?token=" + token + "&letter=" + answer);
                // the class that will handel passing url to the API
                new SubmitAsyncTask(this).execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Displays the letter that have been previously chosen
     *
     * @param letter the chosen letter
     */
    public void setChosen(String letter) {
        chosen.setText(String.join(", ", chosenLetters));
    }

    /**
     * Listener for Hint button
     *
     * @param v Hint Button
     */
    public void getHint(View v) {
        try {
            // adds the number of the game to the url
            URL url = new URL("https://hangman-api.herokuapp.com/hangman/hint?token=" + token);
            new HintAsyncTask(this).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the initial string and number of the game (Create game button)
     *
     * @param h the string at the beginning of the game (___)
     * @param t number of the game
     */
    @Override
    public void setResponse(String h, String t) {
        hangman = h;
        token = t;
        tv.setText(h);
    }

    /**
     * gets the current string and a boolean that tells whether the chosen letter was correct or not
     *
     * @param h  the string after choosing the first letter
     * @param t  number of the game
     * @param cr indicates if the submitted letter was correct
     */
    @Override
    public void setResponse(String h, String t, boolean cr) {
        hangman = h;
        token = t;
        // if the chosen letter was not correct call the method that will change the image
        if (!cr) handleWrongAnswer();
        // if the current string returned didn't contain any _ characters then the user guessed the word
        if (!hangman.contains("_")) endGame();
        tv.setText(hangman);
    }

    /**
     * Gets the whole word
     */
    public void getAnswer() {
        try {
            URL url = new URL("https://hangman-api.herokuapp.com/hangman?token=" + token);
            new AnswerAsyncTask(this).execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the correct word at the end of the game
     *
     * @param h the revealed word
     */
    @Override
    public void setResponse(String h) {
        tv.setText(h);
    }

    /**
     * Change the ImageView when the user makes a wrong guess
     */
    public void handleWrongAnswer() {
        // increment the number of wrong chosen letters
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
                // when 7 guesses are wrong end the game
                endGame();
        }
    }

    /**
     * Gets the final answer and displays the Start Game button
     */
    public void endGame() {
        setInvisible();
        getAnswer();
        start.setVisibility(View.VISIBLE);
    }

    /**
     * Sets hint when requested
     *
     * @param h a letter from the unrevealed letters in the word
     */
    @Override
    public void setHint(String h) {
        text.setText(h);
    }

    /**
     * Changes the display when the game is over
     */
    public void setInvisible() {
        guess.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        hint.setVisibility(View.INVISIBLE);
    }
}

package com.hfad.myhangman;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubmitAsyncTask extends android.os.AsyncTask<URL, Void, String> {

    WeakReference<SubmitAsyncTask.communicate> d;
    String hangman, token;
    boolean correct;

    public SubmitAsyncTask(SubmitAsyncTask.communicate dd) {
        d = new WeakReference<>(dd);
    }

    public interface communicate {

        //will pass into MainActivity the new String to update the TextView with
        public void setResponse(String h, String t, boolean correct);

    }


    protected String doInBackground(URL... objects) {


        String str = "";
        try {
            HttpURLConnection h = (HttpURLConnection) objects[0].openConnection();
            h.setRequestMethod("PUT");
            InputStream is = h.getInputStream();
            BufferedReader b = new BufferedReader(new InputStreamReader(is));

            String line;

            while ((line = b.readLine()) != null) str += line + "\n";


            Log.d("pout", str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;

    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject j = new JSONObject(s);
            hangman = (String) j.get("hangman");
            token = (String) j.get("token");
            correct = (boolean) j.get("correct");

            SubmitAsyncTask.communicate a = d.get();
            if (a != null) // if the object died will be null
                a.setResponse(hangman, token, correct);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
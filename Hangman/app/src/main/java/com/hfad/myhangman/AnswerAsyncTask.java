package com.hfad.myhangman;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class AnswerAsyncTask extends android.os.AsyncTask<URL, Void, String> {

    WeakReference<AnswerAsyncTask.communicate> d;
    String answer;

    public AnswerAsyncTask(AnswerAsyncTask.communicate dd) {
        d = new WeakReference<>(dd);
    }

    public interface communicate {

        //will pass into MainActivity the new String to update the TextView with
        public void setResponse(String h);

    }


    protected String doInBackground(URL... objects) {


        String str = "";
        try {
            HttpURLConnection h = (HttpURLConnection) objects[0].openConnection();
            h.setRequestMethod("GET");
            InputStream is = h.getInputStream();
            BufferedReader b = new BufferedReader(new InputStreamReader(is));

            String line;

            while ((line = b.readLine()) != null) str += line + "\n";


            Log.d("answerhere", str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;

    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject j = new JSONObject(s);
            answer = (String) j.get("solution");
            AnswerAsyncTask.communicate a = d.get();
            if (a != null) // if the object died will be null
                a.setResponse(answer);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
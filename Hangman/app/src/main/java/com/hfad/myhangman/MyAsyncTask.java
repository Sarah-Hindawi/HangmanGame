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

public class MyAsyncTask extends android.os.AsyncTask<URL, Void, String> {

    WeakReference<MyAsyncTask.communicate> d;
    String hangman, token;

    public MyAsyncTask(MyAsyncTask.communicate dd) {
        d = new WeakReference<>(dd);
    }

    public interface communicate {

        //will pass into MainActivity the new String to update the TextView with
        public void setResponse(String h, String t);

    }


    protected String doInBackground(URL... objects) {


        String str = "";
        try {
            HttpURLConnection h = (HttpURLConnection) objects[0].openConnection();
            h.setRequestMethod("POST");
            InputStream is = h.getInputStream();
            BufferedReader b = new BufferedReader(new InputStreamReader(is));

            String line;

            while ((line = b.readLine()) != null) str += line + "\n";


            Log.d("currentWord", str);
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

            MyAsyncTask.communicate a = d.get();
            if (a != null)
                a.setResponse(hangman, token);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}


package com.example.android.scrollableview;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.scrollableview.Classes.MyDate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    public ListView mListView;
    public ArrayList<MyDate> mArrayListDates;
    public AllDatesAdapter allDatesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialization();

        String jsonUrl = "http://www.mocky.io/v2/5b90befa2e0000a22ba89f4f";
        getDatesAsyncTask task = new getDatesAsyncTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,jsonUrl);


    }

    public void initialization()
    {
        mListView = (ListView) findViewById(R.id.listview);
        mArrayListDates = new ArrayList<>();
    }

    private class getDatesAsyncTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {


            if(urls.length <1 || urls[0] == null)
                return null ;

            try {
                getJsonResponse(urls[0]);
                return "success";

            }
            catch (Exception ex)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String string)
        {
            if(string == null) {
                Toast.makeText(HomeActivity.this,"No Record Found",Toast.LENGTH_SHORT).show();
                return;
            }
            else
                displayAllDates();
        }
    }

    private void displayAllDates()
    {
        if (mArrayListDates.size() > 0)
        {
            allDatesAdapter = new AllDatesAdapter(this,mArrayListDates);
            mListView.setAdapter(allDatesAdapter);

        }
        else {
            Toast.makeText(this,"No Record Found",Toast.LENGTH_SHORT).show();
        }


    }


    public void getJsonResponse(String openTDbBaseUrl)
    {
        URL url = null;
        try {
            url = new URL(openTDbBaseUrl);
        } catch (MalformedURLException e) {

        }

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e) {
        }

        try {
            extractFeatureFromJson(jsonResponse);
        }
        catch (Exception ex)
        {
        }
    }


    private void extractFeatureFromJson(String jsonObjectURL)
    {
        if (TextUtils.isEmpty(jsonObjectURL)) {
            return ;
        }

        try
        {
            JSONObject rootJsonObject = new JSONObject(jsonObjectURL);

                JSONArray records = rootJsonObject.getJSONArray("records");
                for (int counter = 0; counter < records.length(); counter++) {
                    JSONObject singlePostOfficeJsonObject = records.getJSONObject(counter);
                    String date = singlePostOfficeJsonObject.getString("date");
                    String month = singlePostOfficeJsonObject.getString("month");
                    String year = singlePostOfficeJsonObject.getString("year");


                    MyDate myDate = new MyDate(date,month,year);
                    mArrayListDates.add(myDate);
                }


        }
        catch (Exception e)
        {
        }
    }




    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}

package com.example.assignment2;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    final String APIKEY = "https://api.mongolab.com/api/1/databases/testdatabase/collections/assignment2?apiKey=A5kVWSH3bgxQl6_qgr3NyZSdvAzZXrwH";
    private JSONArray savedArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getData(final View v) {
        Log.d("MSG", "button clicked");
        Toast.makeText(getApplicationContext(), "PULLING DATA" , Toast.LENGTH_LONG).show();
        new pullData().execute("hi");

    }

    public void updateFields() {
        LinearLayout temp = (LinearLayout)findViewById(R.id.contentLayout);
        temp.removeAllViews();

        try {
            for(int i = 0; i < savedArray.length(); i++) {
                TextView tempText = new TextView(MainActivity.this);

                JSONObject obj = savedArray.getJSONObject(i);
                Log.d("MSG", obj.toString());

                String first = obj.getString("first_name");
                Log.d("MSG", "FIRST NAME: " + first);

                String last = obj.getString("last_name");
                //Log.d("MSG", obj.toString());
                Log.d("MSG", "LAST NAME: " + last);

                String email_address = obj.getString("email_address");
                //Log.d("MSG", obj.toString());
                Log.d("MSG", "EMAIL: " + email_address);

                String student_number = obj.getString("student_number");
                //Log.d("MSG", obj.toString());
                Log.d("MSG", "EMAIL: " + student_number);

                tempText.setText(first + " " + last + " " + email_address + " " + student_number);
                temp.addView(tempText);
            }
        } catch(JSONException e) {

        }





    }

    private class pullData extends AsyncTask<String, Integer, Long> {
        private String first;
        private String last;
        private String email_address;
        private String student_number;


        @Override
        protected Long doInBackground(String... params) {
            Log.d("MSG", "STARTING DO IN BACKGROUND");
            HttpClient client = new DefaultHttpClient();
            HttpGet myGet = new HttpGet(APIKEY);

            HttpResponse response;

            try {
                response = client.execute(myGet);

                Log.d("MSG", "Called Execute");
                HttpEntity entity = response.getEntity();

                Log.d("MSG", "Got the response");



                String entityResponse = EntityUtils.toString(entity);

                //JSONObject jObject = new JSONObject(entityResponse);

                JSONArray responseArray = new JSONArray(entityResponse);
                savedArray = responseArray;

                Log.d("MSG", "Array: " + responseArray.toString());
                JSONObject obj = responseArray.getJSONObject(0);


                Log.d("MSG", obj.toString());

                first = obj.getString("first_name");
                Log.d("MSG", "FIRST NAME: " + first);

                last = obj.getString("last_name");
                //Log.d("MSG", obj.toString());
                Log.d("MSG", "LAST NAME: " + last);

                email_address = obj.getString("email_address");
                //Log.d("MSG", obj.toString());
                Log.d("MSG", "EMAIL: " + email_address);

                student_number = obj.getString("student_number");
                //Log.d("MSG", obj.toString());
                Log.d("MSG", "EMAIL: " + student_number);

                /*
                WHEN THE DATABASE HAS THE NAMES IN A NAME OBJECT, USE THE BELOW CODE

                JSONArray responseArray = new JSONArray(entityResponse);
                Log.d("MSG", "Array: " + responseArray.toString());
                JSONObject obj = responseArray.getJSONObject(2);

                JSONObject customerObj = obj.getJSONObject("customer");
                String first = customerObj.getString("firstname");
                Log.d("MSG", customerObj.toString());
                Log.d("MSG", "RESPONSE: " + first);

                 */




            } catch (ClientProtocolException e) {
                Log.d("MSG", "response error caught");
            } catch (IOException e) {
                Log.d("MSG", "IO error caught");
            } catch(JSONException e) {
                Log.d("MSG", "JSON Exception: " + e.getMessage());
            } finally {
            }


            Log.d("MSG", "Got the object I guess");

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Log.d("MSG", "----------PRINTING OUT RECORDS----------------");
            try {
                for(int i = 0; i < savedArray.length(); i++) {
                    JSONObject obj = savedArray.getJSONObject(i);
                    Log.d("MSG", obj.toString());

                    //first = obj.getString("first_name");
                }

            updateFields();

            } catch(JSONException e) {

            }






            //TextView firstField = (TextView)findViewById(R.id.textAnswerFirst);
            //firstField.setText(first);
            //TextView lastField = (TextView)findViewById(R.id.textAnswerLast);
            //lastField.setText(last);
        }
    }
}

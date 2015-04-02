package com.example.assignment2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
    //private String APIKEY = "https://api.mongolab.com/api/1/databases/testdatabase/collections/assignment2?apiKey=A5kVWSH3bgxQl6_qgr3NyZSdvAzZXrwH";
    private String URL = "https://api.mongolab.com/api/1/databases/";
    private String DATABASE = "testdatabase";
    private String URL2 = "/collections/";
    private String COLLECTION = "assignment2";
    private String URL3 = "?apiKey=";
    private String APIKEY = "A5kVWSH3bgxQl6_qgr3NyZSdvAzZXrwH";
    private String fullURL;


    private JSONArray savedArray;
    SQLiteHelper help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        help = new SQLiteHelper(this);
        updateFieldsSQLite();
        fullURL = URL + DATABASE + URL2 + COLLECTION + URL3 + APIKEY;
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
        String database = ((EditText)findViewById(R.id.dbName)).getText().toString();
        String collection = ((EditText)findViewById(R.id.colName)).getText().toString();
        String apikey = ((EditText)findViewById(R.id.apiKey)).getText().toString();

        if(!(database.equals("") && collection.equals("") && apikey.equals(""))) {

            fullURL = URL + database + URL2 + collection + URL3 + apikey;

        }
            Log.d("MSG", fullURL+"|");

        fullURL.trim();

        Toast.makeText(getApplicationContext(), "PULLING DATA" , Toast.LENGTH_LONG).show();
        new pullData().execute("hi");

    }


    public void updateFieldsSQLite() {
        LinearLayout temp = (LinearLayout)findViewById(R.id.contentLayout);
        temp.removeAllViews();

        Cursor CR = help.getInformation(help);
        if(!CR.moveToFirst()) {
            Toast.makeText(getApplicationContext(), "NO MORE LEFT", Toast.LENGTH_LONG).show();
            return;
        }
        do {
            String first = CR.getString(0);
            String last = CR.getString(1);
            String email_address = CR.getString(2);
            String student_number = CR.getString(3);

            TextView tempText = new TextView(MainActivity.this);
            tempText.setText(first + " " + last + " " + email_address + " " + student_number);
            temp.addView(tempText);
        } while(CR.moveToNext());


    }


    public void toastData(final View v) {
        Cursor CR = help.getInformation(help);
        if(!CR.moveToFirst()) {
            Toast.makeText(getApplicationContext(), "NO MORE LEFT", Toast.LENGTH_LONG).show();
            return;
        }
        do {
            String first = CR.getString(0);
            String last = CR.getString(1);

            Toast.makeText(getApplicationContext(), first + " " + last, Toast.LENGTH_SHORT).show();

        } while(CR.moveToNext());
    }

    public void deleteAllData(final View v) {
        help.deleteAll(help);
        updateFieldsSQLite();
    }

    private class pullData extends AsyncTask<String, Integer, Long> {
        @Override
        protected Long doInBackground(String... params) {
            Log.d("MSG", "STARTING DO IN BACKGROUND");
            HttpClient client = new DefaultHttpClient();
            HttpGet myGet = new HttpGet(fullURL);

            HttpResponse response;

            savedArray = new JSONArray();
            try {
                response = client.execute(myGet);

                Log.d("MSG", "Called Execute");
                HttpEntity entity = response.getEntity();

                Log.d("MSG", "Got the response");

                String entityResponse = EntityUtils.toString(entity);

                savedArray = new JSONArray(entityResponse);

            } catch (ClientProtocolException e) {
                Log.d("MSG", "response error caught");
            } catch (IOException e) {
                Log.d("MSG", "IO error caught");
            } catch(JSONException e) {
                Log.d("MSG", "JSON Exception: " + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            //clear the previously saved records on the SQLite database
            help.deleteAll(help);

            Log.d("MSG", "----------PRINTING OUT RECORDS----------------");
            try {
                for(int i = 0; i < savedArray.length(); i++) {
                    JSONObject obj = savedArray.getJSONObject(i);
                    Log.d("MSG", obj.toString());
                    String first = obj.getString("first_name");
                    String last = obj.getString("last_name");
                    String email_address = obj.getString("email_address");
                    String student_number = obj.getString("student_number");

                    help.putInformation(help, first, last, email_address, student_number);

                }

                updateFieldsSQLite();

            } catch(JSONException e) {
                Log.d("MSG", "JSON Exception: " + e.getMessage());
            } catch(NullPointerException e) {
                Toast.makeText(getApplicationContext(), "NOT A VALID DATABASE", Toast.LENGTH_LONG).show();
            }
        }
    }
}

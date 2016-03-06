package com.example.abhi.clean_india;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import io.socket.client.IO;
import io.socket.client.Socket;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Details extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://52.89.16.102:8080");
        } catch (URISyntaxException e) {}
    }
    private ProgressDialog pDialog;
    private SessionManager session;
    String image,longi,lati;
    EditText desc,location;
    String t,l,d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        session = new SessionManager(Details.this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        session = new SessionManager(Details.this);

        desc=(EditText)findViewById(R.id.editText2);
        location=(EditText)findViewById(R.id.editText3);
        Button reg=(Button)findViewById(R.id.button);

        final Spinner sp = (Spinner)findViewById(R.id.spinner);
        sp.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("Electronic");
        categories.add("Biological");
        categories.add("Chemical");
        categories.add("Solid");
        categories.add("Liquid");
        categories.add("Plastics");
        categories.add("Others");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(dataAdapter);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t = sp.getSelectedItem().toString().trim();
                d = desc.getText().toString().trim();
                l = location.getText().toString().trim();

                if (!t.isEmpty() && !d.isEmpty() && !l.isEmpty()) {
                    new DownloadTask().execute("rohit");
                    pDialog.setMessage("Registering ...");
                    showDialog();
                } else
                    Toast.makeText(Details.this, "Pls Fill all details...!!", Toast.LENGTH_LONG).show();
            }
        });
        Intent intent = getIntent();
        longi=intent.getStringExtra("long");
        lati=intent.getStringExtra("lati");
        image=intent.getStringExtra("image");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {

                return downloadContent(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task
            hideDialog();
            mSocket.connect();
            mSocket.emit("report_fir","new_fir_registered");

            Toast.makeText(Details.this, "Request Registered successfully", Toast.LENGTH_LONG).show();
            Intent startIntent = new Intent("Home");
            startActivity(startIntent);

        }
    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 500;

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("rohit", image));
        nameValuePairs.add(new BasicNameValuePair("user_id", session.user()));
        nameValuePairs.add(new BasicNameValuePair("longi", longi));
        nameValuePairs.add(new BasicNameValuePair("lati", lati));
        nameValuePairs.add(new BasicNameValuePair("title", t));
        nameValuePairs.add(new BasicNameValuePair("location", l));
        nameValuePairs.add(new BasicNameValuePair("desc", d));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://code.yakshaq.in/android/insert.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            String st = EntityUtils.toString(response.getEntity());
            Log.v("log_tag", "In the try Loop" + st);

        } catch (Exception e) {
            Log.v("log_tag", "Error in http connection " + e.toString());
        }
        return "Success";
    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

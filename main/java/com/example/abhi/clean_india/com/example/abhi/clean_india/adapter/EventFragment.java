package com.example.abhi.clean_india.com.example.abhi.clean_india.adapter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhi.clean_india.R;
import com.example.abhi.clean_india.SessionManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {


    public EventFragment() {
        // Required empty public constructor
    }

    private EditText title, st, et, lo, des;
    private View rootview;
    private SessionManager session;
    private Button b2;
    private String cam_t, start, end, loc, desc;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_event, container, false);
        title = (EditText) rootview.findViewById(R.id.editText);
        st = (EditText) rootview.findViewById(R.id.editText4);
        et = (EditText) rootview.findViewById(R.id.editText5);
        lo = (EditText) rootview.findViewById(R.id.editText3);
        des = (EditText) rootview.findViewById(R.id.editText2);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        session = new SessionManager(getActivity().getApplicationContext());
        b2 = (Button) rootview.findViewById(R.id.button);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_t = title.getText().toString().trim();
                start = st.getText().toString().trim();
                end = et.getText().toString().trim();
                loc = lo.getText().toString().trim();
                desc = des.getText().toString().trim();
                if (!cam_t.isEmpty() && !start.isEmpty() && !end.isEmpty() && !loc.isEmpty() && !desc.isEmpty()) {
                    pDialog.setMessage("Registering ...");
                    showDialog();
                    new DownloadTask().execute();
                } else
                    Toast.makeText(getActivity(), "Pls Fill all details...!!", Toast.LENGTH_LONG).show();
            }
        });
        return rootview;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if(registerUser())
                return "success";
            else
                return "fail";

        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task

            if(result == "fail")
            {
                showAlertDialog(getActivity(), "Registered Successfully", "Go Ahead");
            }
            else
                showAlertDialog(getActivity(),"Error Occured","Please try again");

        }
    }

    private boolean registerUser() {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", session.user()));
        nameValuePairs.add(new BasicNameValuePair("end_date", end));
        nameValuePairs.add(new BasicNameValuePair("start_date", start));
        nameValuePairs.add(new BasicNameValuePair("title", cam_t));
        nameValuePairs.add(new BasicNameValuePair("location", loc));
        nameValuePairs.add(new BasicNameValuePair("desc", desc));


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://code.yakshaq.in/android/cupload.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            String st = EntityUtils.toString(response.getEntity());
            Toast.makeText(getActivity().getApplicationContext(),st,Toast.LENGTH_LONG).show();
            //Log.v("log_tag", "In the try Loop : " + st.length());
            if(st.length()==3)
            {
                return true;
            }
        } catch (Exception e) {

           //Log.v("log_tag",  e.toString());
        }
        hideDialog();
        return false;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}

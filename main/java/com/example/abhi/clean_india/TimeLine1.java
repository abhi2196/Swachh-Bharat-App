package com.example.abhi.clean_india;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Abhi on 19-Jan-16.
 */
public class TimeLine1 extends Fragment implements DialogInterface.OnClickListener{
    private ImageButton b1,b2,b3;
    public TimeLine1() {
    }

    GridView grid;
    String[] web = null;
    int[] imageId = null;
    View rootView;
    private String t_id,c_id;
    int case_scale;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        case_scale = timeline.case_scale;
        String id = timeline.case_id;

        //Toast.makeText(getActivity(), "scale " + case_scale + "  id :" + id, Toast.LENGTH_LONG).show();
        if (case_scale == 1) {
            rootView = inflater.inflate(R.layout.timeline1, container, false);
            b1=(ImageButton)rootView.findViewById(R.id.imageButton4);
        }
        else if (case_scale == 2) {
            rootView = inflater.inflate(R.layout.timeline_2, container, false);
            b2=(ImageButton)rootView.findViewById(R.id.imageButton5);
        }
        else if (case_scale == 3 || case_scale == 4) {
            rootView = inflater.inflate(R.layout.timeline_3, container, false);
            b3=(ImageButton)rootView.findViewById(R.id.imageButton6);
            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent i = new Intent("Naya");
                    startActivity(i);
                }
            });
        }
        new DownloadTask().execute("https://api.mongolab.com/api/1/databases/swachh/collections/case_info?f={%22_id%22:0,%22case_data%22:1,%22case_data.title%22:1,%22case_data.case_date%22:1,%22case_data.sector%22:1,%22case_data.Cleaner%22:1}&apiKey=oiFA2wjAg4tcIzjfQnfWBrj_Y4RO4GqP");
        return rootView;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //do your request in here so that you don't interrupt the UI thread
            try {
                return downloadContent(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //Here you are done with the task
            int progress_scale = 0;
            View child = null;
            try {
                JSONArray jarray = new JSONArray(result);
                int a = jarray.length();
                web = new String[a];
                imageId = new int[a];
                //Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject obj = jarray.getJSONObject(i);
                    JSONArray arr = obj.getJSONArray("case_data");
                    for (int j = 0; j < arr.length(); j++) {
                        JSONObject mani = arr.getJSONObject(j);
                        // progress_scale=mani.getInt("progress_scale");
                        TextView tv = (TextView) rootView.findViewById(R.id.descp1);
                        tv.setText(mani.getString("title"));
                        TextView tv1 = (TextView) rootView.findViewById(R.id.time1);
                        tv1.setText(mani.getString("case_date"));
                        if (case_scale == 2) {
                            TextView tv2 = (TextView) rootView.findViewById(R.id.descp2);
                            tv2.setText(mani.getString("sector"));
                            TextView tv3 = (TextView) rootView.findViewById(R.id.time2);
                            tv3.setText(mani.getString("case_date"));
                        }
                        if (case_scale == 3) {
                            TextView tv2 = (TextView) rootView.findViewById(R.id.descp2);
                            tv2.setText(mani.getString("sector"));
                            TextView tv3 = (TextView) rootView.findViewById(R.id.time2);
                            tv3.setText(mani.getString("case_date"));

                            TextView tv4 = (TextView) rootView.findViewById(R.id.descp3);
                            tv4.setText(mani.getString("Cleaner"));
                            TextView tv5 = (TextView) rootView.findViewById(R.id.time3);
                            //tv5.setText(mani.getString(""));
                        }

                    }

                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("Respone", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertInputStreamToString(is, length);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }
}
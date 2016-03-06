package com.example.abhi.clean_india;


import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RatingBar;
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
 * A simple {@link Fragment} subclass.
 */
public class rewards extends Fragment {


    public rewards() {
        // Required empty public constructor
    }

    private RatingBar rb;
    private TextView e1, e2, e3,e4;
    private View rootview;
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_rewards, container, false);
        rb = (RatingBar) rootview.findViewById(R.id.ratingBar);
        e1 = (TextView) rootview.findViewById(R.id.t1);
        e2 = (TextView) rootview.findViewById(R.id.t2);
        e3 = (TextView) rootview.findViewById(R.id.er);
        e4= (TextView)rootview.findViewById(R.id.tite6);
        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        new DownloadTask().execute("https://api.mongolab.com/api/1/databases/swachh/collections/user_info?f=%7B%22_id%22%3A0%2C%22user_data%22%3A1%7D&apiKey=oiFA2wjAg4tcIzjfQnfWBrj_Y4RO4GqP");
        pd.setMessage("Loading User Profile...");
        show();
        return rootview;
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
            hide();
            try{
                JSONArray jarray = new JSONArray(result);
                JSONObject obj =jarray.getJSONObject(0);
                JSONArray arr=obj.getJSONArray("user_data");
                JSONObject mani=arr.getJSONObject(0);
                e1.setText(mani.getString("name"));
                int points=mani.getInt("points");
                if(points>=5)
                    e2.setText("Neigbourhood Head");
                else
                    e2.setText("Member");
                float d = (float) (points * 50 / 100);
                rb.setRating(d);
                e3.setText(Integer.toString(points*2));
            }
            catch (JSONException e){
                Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 2000;

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
    private void hide() {
        if (pd.isShowing())
            pd.dismiss();
    }

    private void show() {
        if (!pd.isShowing())
            pd.show();
    }

}
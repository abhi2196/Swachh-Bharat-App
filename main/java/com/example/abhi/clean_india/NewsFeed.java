package com.example.abhi.clean_india;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

public class NewsFeed extends Fragment {
    public NewsFeed(){}
    ProgressDialog pDialog;
    ListView lv;
    View rootView;
    String[] title=null;
    String[] desc=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_news_feed, container, false);
        pDialog = new ProgressDialog(getActivity());
        lv=(ListView)rootView.findViewById(R.id.listView1);
        pDialog.setCancelable(false);

        new DownloadTask().execute("https://api.mongolab.com/api/1/databases/swachh/collections/news_feed?f={%22_id%22:0,%22feed_data%22:1,%22feed_data.news_title%22:1,%22feed_data.news_description%22:1}&apiKey=oiFA2wjAg4tcIzjfQnfWBrj_Y4RO4GqP");
        pDialog.setMessage("Loading News Feeds ...");
        showDialog();
        return rootView;
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
            hideDialog();
            //  Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            try {
                JSONArray jarray = new JSONArray(result);
                int a=jarray.length();
                title=new String[a];
                desc=new String[a];
                for(int i=0; i<jarray.length();i++)
                {
                    JSONObject obj=jarray.getJSONObject(i);
                    JSONArray arr=obj.getJSONArray("feed_data");
                    for(int j=0; j<arr.length();j++)
                    {
                        JSONObject mani=arr.getJSONObject(j);
                        String case_n=mani.getString("news_title");
                        title[i]=case_n;
                        desc[i]=mani.getString("news_description");

                    }
                }
            }
            catch (JSONException e)
            {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

            if(title!=null) {
                custom_data cd = new custom_data(getActivity(),title,desc);

                lv.setAdapter(cd);

            }
            else
                Toast.makeText(getActivity(), "Network Problem .. pls reload", Toast.LENGTH_LONG).show();

        }
    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 10000;

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
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}

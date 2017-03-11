package com.aaa.gerrix.volleycache.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.aaa.gerrix.volleycache.R;
import com.aaa.gerrix.volleycache.model.Lost;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Lost> lostList;
    private RecyclerView recyclerView;
    private LostAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lostList = new ArrayList<>();

        initUi();

        if (!isInternetConnected(this)){

            Thread thread = new Thread(){
                public void run(){
                    for (int i = 0; i < getPosts().size(); i++) {

                        lostList.add(getPosts().get(i));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            };

            thread.start();



        }else {
            Delete.table(Lost.class);
            loadLost();

        }

    }

    private List<Lost> getPosts(){
        List<Lost> organizationList = SQLite.select().
                from(Lost.class).queryList();
        Log.i("Provera getPosts", String.valueOf(organizationList.size()));

        return organizationList;
    }

    private void initUi(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new LostAdapter((ArrayList<Lost>) lostList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void loadLost(){
        String url2 = "http://api.tvmaze.com/singlesearch/shows?q=lost&embed=episodes";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                url2, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject emedded = response.getJSONObject("_embedded");
                    JSONArray episodes = emedded.getJSONArray("episodes");
                    for (int i = 0; i <episodes.length() ; i++) {

                        JSONObject episode = episodes.getJSONObject(i);
                        String name = episode.getString("name");

                        JSONObject image = episode.getJSONObject("image");
                        String imageMedium = image.getString("medium");

                        int id = episode.getInt("id");

                        String url = episode.getString("url");

                        int season = episode.getInt("season");

                        try {
                            URL urlImage = new URL(imageMedium);
                            (new Thread(new Downlaoder(
                                    id,
                                    name,
                                    urlImage
                            ))).start();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(jsonObjReq);
    }

    public  class Downlaoder implements Runnable{
        private URL url;
        private Lost lost;
        private int id;
        private String title;



        Downlaoder(int id, String title,URL url){
            lost = new Lost();
            this.url = url;
            this.id = id;
            this.title = title;

        }
        @RequiresApi(api = VERSION_CODES.KITKAT)
        @Override
        public void run() {
            try {
                byte[] bytes = recoverImageFromUrl(this.url);

                Log.i("Provera run", String.valueOf(recoverImageFromUrl(this.url).length));

                lost.setImage(new Blob(bytes));
                lost.setTitle(this.title);
                lost.setId(this.id);
                lost.save();
            } catch (Exception e) {
                e.printStackTrace();
            }



            lostList.add(lost);
            Log.i("Provera postlist", String.valueOf(lostList.size()));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();

                }
            });

        }

    }
    @RequiresApi(api = VERSION_CODES.KITKAT)
    public byte[] recoverImageFromUrl(URL urlText) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = urlText.openStream()) {
            int n = 0;
            byte [] buffer = new byte[ 1024 ];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}

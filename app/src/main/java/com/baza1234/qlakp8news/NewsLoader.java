package com.baza1234.qlakp8news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Loads the News.
 */
public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {

    private String mUrl;

    public NewsLoader(Context context, String url){
        super (context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground(){
        if(mUrl == null){
            return null;
        }

        ArrayList<News> news = NewsUtility.fillNewsData(mUrl);
        return news;
    }
}

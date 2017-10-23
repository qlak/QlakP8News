package com.baza1234.qlakp8news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<News>> {

    // Adapter for the list of articles.
    private NewsAdapter mAdapter;

    ListView newsListView;

    // Integers to take care of the buttons and page switching.
    int buttonPressed;
    int buttonActive = 0;
    int pageNumberOfArticles = 1;

    // Helper Log String.
    private static final String LOG = "Log Help";

    // Loader ID.
    private static final int NEWS_LOADER_ID = 1;

    // The URL.
    private static String NEWS_REQUEST_URL = "";

    // Shows that text when the list is empty (can't find books or no internet connection).
    private ImageView mEmptyStateView;

    // Used to remember the state of the network for the empty state view.
    boolean networkOn;

    // Integers for random background generator.
    // Image number.
    int number = 1;
    // Previous image number.
    int previousNumber = 0;
    // Image ID.
    int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRandomImage();

        final LoaderManager loadManager = getLoaderManager();

        newsListView = (ListView) findViewById(R.id.newslist);
        mEmptyStateView = (ImageView) findViewById(R.id.empty_view);

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Left Button - Console.
        ImageView consolebutton = (ImageView) findViewById(R.id.consolebutton);
        consolebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(buttonActive == 1){
                    pageNumberOfArticles++;
                }else{
                    pageNumberOfArticles = 1;
                }

                loadingStart();

                // Handler to delay image changing for the animation.
                backgroundAnimation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setRandomImage();
                    }
                }, 560);

                buttonPressed = 1;
                buttonActive = 1;
                updateSearchUrl();
                newsSearch();
            }
        });

        // Middle Button - All most recent.
        ImageView refresh = (ImageView) findViewById(R.id.refreshbutton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(buttonActive == 2){
                    pageNumberOfArticles++;
                }else{
                    pageNumberOfArticles = 1;
                }

                loadingStart();

                backgroundAnimation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setRandomImage();
                    }
                }, 560);

                buttonPressed = 2;
                buttonActive = 2;
                updateSearchUrl();
                newsSearch();
            }
        });

        // Right Button - Board.
        ImageView boardbutton = (ImageView) findViewById(R.id.boardbutton);
        boardbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(buttonActive == 3){
                    pageNumberOfArticles++;
                }else{
                    pageNumberOfArticles = 1;
                }

                loadingStart();

                backgroundAnimation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        setRandomImage();
                    }
                }, 560);

                buttonPressed = 3;
                buttonActive = 3;
                updateSearchUrl();
                newsSearch();
            }
        });
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {
        loadingStart();
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news){

        mAdapter.clear();

        if(news != null && !news.isEmpty()){
            mAdapter.addAll(news);
            loadingFinished();
        }else {
            if (networkOn) {
                mEmptyStateView.setImageResource(R.drawable.emptystateview_connectiontimeout);
                mEmptyStateView.setVisibility(View.VISIBLE);
            } else {
                mEmptyStateView.setImageResource(R.drawable.emptystateview_nonetwork);
                mEmptyStateView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader){
        mAdapter.clear();
    }

    // Method updates URL depending on which button was pressed.
    // Left Button = 1, Mid Button = 2, Right Button = 3.
    public void updateSearchUrl(){
        if(buttonPressed == 1){
            // players OR gamers OR computer games OR console games OR consoles OR roguelike OR rpg OR fpp OR xbox OR steam OR playstation.
            NEWS_REQUEST_URL = "http://content.guardianapis.com/search?show-elements=all&page-size=40&order-by=relevance&page=" + pageNumberOfArticles + "&show-fields=all&section=technology&q=players%20OR%20gamers%20OR%20computer%20games%20OR%20console%20games%20OR%20consoles%20OR%20roguelike%20OR%20rpg%20OR%20fpp%20OR%20xbox%20OR%20steam%20OR%20playstation.&api-key=4032edd6-45bf-4a32-bf4c-2ef405b9b6d3";
        }
        if(buttonPressed == 2){
            // movie OR manga OR anime OR film OR computer game OR console game OR geek OR gamers OR computers OR consoles OR console games OR computer games OR nerd OR gadget OR gadgets OR technology OR board game OR puzzle OR card game OR xbox OR playstation OR microsoft OR apple
            NEWS_REQUEST_URL = "http://content.guardianapis.com/search?show-elements=all&page-size=40&order-by=newest&page=" + pageNumberOfArticles + "&show-fields=all&q=movie%20OR%20manga%20OR%20anime%20OR%20film%20OR%20computer%20game%20OR%20console%20game%20OR%20geek%20OR%20gamers%20OR%20computers%20OR%20consoles%20OR%20console%20games%20OR%20computer%20games%20OR%20nerd%20OR%20gadget%20OR%20gadgets%20OR%20technology%20OR%20board%20game%20OR%20puzzle%20OR%20card%20game%20OR%20xbox%20OR%20playstation%20OR%20microsoft%20OR%20apple&api-key=4032edd6-45bf-4a32-bf4c-2ef405b9b6d3";
        }
        if(buttonPressed == 3){
            // card games OR puzzle OR cards OR board game OR board games OR geek OR nerd OR geeks OR nerds OR toys OR gadgets OR gadget.
            NEWS_REQUEST_URL = "http://content.guardianapis.com/search?show-elements=all&page-size=40&order-by=relevance&page=" + pageNumberOfArticles + "&show-fields=all&q=card%20games%20OR%20puzzle%20OR%20cards%20OR%20board%20game%20OR%20board%20games%20OR%20geek%20OR%20nerd%20OR%20geeks%20OR%20nerds%20OR%20toys%20OR%20gadgets%20OR%20gadget&api-key=4032edd6-45bf-4a32-bf4c-2ef405b9b6d3";
        }
    }

    // Sets the loading screen.
    public void loadingStart(){
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
        mEmptyStateView.setImageResource(R.drawable.emptystateview_loading);
        mEmptyStateView.setVisibility(View.VISIBLE);
    }
    // Removes the loading screen.
    public void loadingFinished(){
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateView.setImageResource(R.drawable.emptystateview_loading);
        mEmptyStateView.setVisibility(View.GONE);
    }

    // Starts the News Search based on the given url and checks the network state.
    public void newsSearch() {

        // Sets the loading screen.
        View loadingIndicator = findViewById(R.id.loading_indicator);
        mEmptyStateView.setImageResource(R.drawable.emptystateview_loading);
        loadingStart();

        ConnectivityManager connectMngr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMngr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
            networkOn = true;
        } else {
            loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            networkOn = false;
        }

        if (networkOn) {
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);

            ListView newsListView = (ListView) findViewById(R.id.newslist);

            mAdapter = new NewsAdapter(this, new ArrayList<News>());
            newsListView.setAdapter(mAdapter);

            // Allow user to click on the list object and read the whole article on the web.
            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    News currentBook = mAdapter.getItem(position);

                    Uri newsUri = Uri.parse(currentBook.getUrl());
                    Intent checkMoreWebInfo = new Intent(Intent.ACTION_VIEW, newsUri);
                    startActivity(checkMoreWebInfo);
                }
            });

        } else {
            Log.i(LOG, "No internet connection");
            mEmptyStateView.setImageResource(R.drawable.emptystateview_nonetwork);
            mEmptyStateView.setVisibility(View.VISIBLE);

            try {
                mAdapter.clear();
            }catch (NullPointerException e){
                Log.e(LOG, "The adapter was empty.");
            }
        }
    }

    public void setRandomImage(){

        number = randomizeNumbers(1, 28, previousNumber);

        image = getResources().getIdentifier("b" + number, "drawable", getPackageName());

        previousNumber = number;

        ImageView background = (ImageView) findViewById(R.id.background);
        background.setImageResource(image);
    }

    /**
     *   Random number generator.
     *   @param min sets minimal random value to choose from.
     *   @param max sets max random value to choose from.
     *   @param without that number can not be chosen - program will reroll to get different random number.
     */
    public int randomizeNumbers(int min, int max, int without){
        Random randomNumber = new Random();
        int randomNum = randomNumber.nextInt((max - min) + 1) + min;
        // Rerolls the dice if that number was marked as not wanted.
        if(randomNum == without){
            while(randomNum == without) {
                randomNum = randomNumber.nextInt((max - min) + 1) + min;
            }
        }
        return randomNum;
    }

    /**
     *   Starts the animation of background change.
     */
    public void backgroundAnimation() {
        final AnimationDrawable backgroundAnimation = new AnimationDrawable();
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation1), 80);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation2), 80);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation3), 80);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation4), 80);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation5), 80);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation6), 80);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation7), 80);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation8), 80);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation9), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation10), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation11), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation12), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation13), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation14), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation15), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation16), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation17), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation18), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation19), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation20), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation21), 250);
        backgroundAnimation.addFrame(getResources().getDrawable(R.drawable.backanimation22), 250);
        backgroundAnimation.setOneShot(true);

        ImageView background = (ImageView) findViewById(R.id.background_animation);
        background.setBackgroundDrawable(backgroundAnimation);
        backgroundAnimation.start();
    }
}
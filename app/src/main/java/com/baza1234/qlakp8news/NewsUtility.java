package com.baza1234.qlakp8news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods to receive Article Info from the server.
 */
public final class NewsUtility {

    private static final String LOG = "Log Help";

    // NewsUtility constructor.
    private NewsUtility(){
    }

    public static ArrayList<News> fillNewsData(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG, "Problem when making the HTTP request", e);
        }

        ArrayList<News> news = extractFromJson(jsonResponse);
        return news;
    }

    // Returns URL object from URL String, prints error when URL is wrong.
    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG, "Problem with URL, MalformedURL", e);
        }
        return url;
    }

    // HTTP request to the URL to get a String.
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request is successful then read the input stream and parse response.
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG, "Error response code: " + urlConnection.getResponseCode());
            }

        }catch (IOException e){
            Log.e(LOG, "Problem getting the JSON results.", e);
        }finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    // Converting inputStream into the String with JSON response.
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    // Return the list of News Objects from JSON Response.
    private static ArrayList<News> extractFromJson(String newsJSON){

        if(TextUtils.isEmpty(newsJSON)){
            return null;
        }

        ArrayList<News> news = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            if(baseJsonResponse.has("response")){
                JSONObject response = baseJsonResponse.getJSONObject("response");

                JSONArray resultsArray = response.getJSONArray("results");

                for (int i = 0; i < resultsArray.length(); i++){

                    JSONObject currentArticle = resultsArray.getJSONObject(i);
                    JSONObject fields = currentArticle.getJSONObject("fields");

                    String title;
                    if(fields.has("headline")) {
                        title = fields.optString("headline");
                    }else{
                        Log.i(LOG, "The Title is UNKNOWN.");
                        title = "No Title";
                    }
                    String author;
                    if(fields.has("byline")){
                        String authorHelp = fields.optString("byline");
                        if(authorHelp.length() > 25){
                            Log.i(LOG, "Should cut now, authorHelp: " + authorHelp.length());
                            author = "by " + authorHelp.substring(0, 25) + "... , ";
                        }else if(authorHelp.equals("") || authorHelp.equals(" ")) {
                            Log.i(LOG, "The Author is UNKNOWN.");
                            author = "Unknown Author, ";
                        }else{
                            author = "by " + fields.optString("byline") + ", ";
                        }
                    }else{
                        Log.i(LOG, "The Author is UNKNOWN.");
                        author = "Unknown Author, ";
                    }
                    String date;
                    if(fields.has("firstPublicationDate")){
                        String dateFormatHelp = fields.optString("firstPublicationDate");
                        date = "";
                        for(int d = 0; d < 10; d++){
                            date = date + dateFormatHelp.charAt(d);
                        }
                        date = "on " + date;
                    }else{
                        Log.i(LOG, "The Date is UNKNOWN.");
                        date = "Unknown Publication Date";
                    }
                    String description;
                    if(fields.has("trailText")) {
                        description = fields.optString("trailText");
                    }else{
                        Log.i(LOG, "The Description is UNKNOWN.");
                        description = "Description is not available for this article.";
                    }
                    String imageLink;
                    if(fields.has("thumbnail")){
                        imageLink = fields.optString("thumbnail");
                    }else{
                        Log.i(LOG, "The Article does not have an Image.");
                        imageLink = "Image is not available for this article.";
                    }
                    String articleText;
                    if(fields.has("bodyText")) {
                        String articleTextCutHelp = fields.optString("bodyText");
                        articleText = "";

                        // Cuts the article properly after checking its character count.
                        String characterCount = fields.optString("charCount");
                        int characterCountHelp = Integer.parseInt(characterCount);
                        characterCountHelp = characterCountHelp / 2;
                        while(characterCountHelp > 1250){
                            characterCountHelp = characterCountHelp / 2;
                        }

                        for(int t = 0; t < characterCountHelp; t++){
                            articleText = articleText + articleTextCutHelp.charAt(t);
                        }

                        if(characterCountHelp > 0) {
                            // Shows only part of the article, after that it places "(...)".
                            try{
                                for (int h = 0; h < 10; h++) {
                                    if (articleTextCutHelp.charAt(characterCountHelp + h) == ' ') {
                                        articleText = articleText + "(...)";
                                        break;
                                    } else {
                                        articleText = articleText + articleTextCutHelp.charAt(characterCountHelp + h);
                                    }
                                }
                            }catch (IndexOutOfBoundsException e){
                                Log.e(LOG, "Wrong Index: " + articleTextCutHelp.length());
                            }
                        }
                    }else{
                        Log.i(LOG, "The Article has no Text.");
                        articleText = "Sorry, something went wrong or the Article is incomplete.";
                    }
                    String moreInfoUrl = fields.optString("shortUrl");

                    News onenews = new News(title, author, date, description, imageLink, articleText, moreInfoUrl);
                    news.add(onenews);
                }

            } else {
                Log.i(LOG, "Could not find any 'response' in the JSON response.");
            }

        }catch (JSONException e){
            Log.e(LOG, "Problem when parsing the newsJSON results - NewsUtility", e);
        }

        return news;
    }

    // Helps to download the Image associated with the Article.
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error decoding Image", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
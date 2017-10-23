package com.baza1234.qlakp8news;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import static android.R.attr.author;
import static java.lang.System.load;

/**
 * Adapter which creates a list item for each News.
 */
public class NewsAdapter extends ArrayAdapter<News>{

    private static final String LOG = "Log Help";

    /**
     * Constructs a new NewsAdapter
     */
    public NewsAdapter(Context context, ArrayList<News> news){
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.news_title);
        title.setText(currentNews.getTitle());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentNews.getAuthor());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(currentNews.getDate());

        TextView description = (TextView) listItemView.findViewById(R.id.description);
        description.setText(currentNews.getDescription());

        ImageView imageLink = (ImageView) listItemView.findViewById(R.id.news_image);
        if(currentNews.getImageLink().equals("Image is not available for this article.")){
            imageLink.setImageResource(R.drawable.emptyimage);
        }else {
            Picasso.with(this.getContext()).load(currentNews.getImageLink()).into(imageLink);
        }

        Typeface typeFace = Typeface.createFromAsset(getContext().getAssets(),"fonts/MOZART_0.ttf");
        TextView articleText = (TextView) listItemView.findViewById(R.id.article_text);
        articleText.setText(currentNews.getArticleText());
        articleText.setTypeface(typeFace);

        ImageView smallButton = (ImageView) listItemView.findViewById(R.id.small_button);
        int smallButtonId = getContext().getResources().getIdentifier("sm" + randomizeNumbers(1,4,0), "drawable", getContext().getPackageName());
        smallButton.setImageResource(smallButtonId);

        return listItemView;
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
}
package com.baza1234.qlakp8news;

/**
 * News Object.
 */
public class News {

    // response => results => fields => headline
    private String mTitle;

    // response => results => fields => byline
    private String mAuthor;

    // response => results => fields => firstPublicationDate
    private String mDate;

    // response => results => fields => trailText
    private String mDescription;

    // response => results => fields => thumbnail
    private String mImageLink;

    // response => results => fields => bodyText
    private String mArticleText;

    // response => results => fields => shortUrl
    private String mUrl;

    public News(String title, String author, String date, String description, String imageLink, String articleText, String url){
        mTitle = title;
        mAuthor = author;
        mDate = date;
        mDescription = description;
        mImageLink = imageLink;
        mArticleText = articleText;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }
    public String getAuthor() {
        return mAuthor;
    }
    public String getDate() {
        return mDate;
    }
    public String getDescription() {
        return mDescription;
    }
    public String getImageLink() {
        return mImageLink;
    }
    public String getArticleText() {
        return mArticleText;
    }
    public String getUrl() {
        return mUrl;
    }
}
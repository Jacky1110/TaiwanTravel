package com.jotangi.nickyen.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewsBean implements Serializable
{
    @SerializedName("nid")
    private String nid;
    @SerializedName("news_subject")
    private String newsSubject;
    @SerializedName("news_date")
    private String newsDate;
    @SerializedName("news_descript")
    private String newsDescript;
    @SerializedName("news_picture")
    private String newsPicture;

    public String getNid()
    {
        return nid;
    }

    public void setNid(String nid)
    {
        this.nid = nid;
    }

    public String getNewsSubject()
    {
        return newsSubject;
    }

    public void setNewsSubject(String newsSubject)
    {
        this.newsSubject = newsSubject;
    }

    public String getNewsDate()
    {
        return newsDate;
    }

    public void setNewsDate(String newsDate)
    {
        this.newsDate = newsDate;
    }

    public String getNewsDescript()
    {
        return newsDescript;
    }

    public void setNewsDescript(String newsDescript)
    {
        this.newsDescript = newsDescript;
    }

    public String getNewsPicture()
    {
        return newsPicture;
    }

    public void setNewsPicture(String newsPicture)
    {
        this.newsPicture = newsPicture;
    }

    @Override
    public String toString()
    {
        return "NewsBean{" +
                "nid='" + nid + '\'' +
                ", newsSubject='" + newsSubject + '\'' +
                ", newsDate='" + newsDate + '\'' +
                ", newsDescript='" + newsDescript + '\'' +
                ", newsPicture='" + newsPicture + '\'' +
                '}';
    }
}

package com.jotangi.nickyen.member.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/7
 * rid: "1",
 * qid: "1",
 * questiontype_name: "點數相關",
 * question_subject: "點數有效期限",
 * question_description: "點數永遠有效!"
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class FAQBean
{
    @SerializedName("rid")
    private String rid;
    @SerializedName("qid")
    private String qid;
    @SerializedName("questiontype_name")
    private String questiontypeName;
    @SerializedName("question_subject")
    private String questionSubject;
    @SerializedName("question_description")
    private String questionDescription;

    private boolean expanded;

    public String getRid()
    {
        return rid;
    }

    public void setRid(String rid)
    {
        this.rid = rid;
    }

    public String getQid()
    {
        return qid;
    }

    public void setQid(String qid)
    {
        this.qid = qid;
    }

    public String getQuestiontypeName()
    {
        return questiontypeName;
    }

    public void setQuestiontypeName(String questiontypeName)
    {
        this.questiontypeName = questiontypeName;
    }

    public String getQuestionSubject()
    {
        return questionSubject;
    }

    public void setQuestionSubject(String questionSubject)
    {
        this.questionSubject = questionSubject;
    }

    public String getQuestionDescription()
    {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription)
    {
        this.questionDescription = questionDescription;
    }

    public boolean isExpanded()
    {
        return this.expanded;
    }

    public void setExpanded(final boolean expanded)
    {
        this.expanded = expanded;
    }

    @Override
    public String toString()
    {
        return "FAQBean{" +
                "rid='" + rid + '\'' +
                ", qid='" + qid + '\'' +
                ", questiontypeName='" + questiontypeName + '\'' +
                ", questionSubject='" + questionSubject + '\'' +
                ", questionDescription='" + questionDescription + '\'' +
                ", expanded=" + expanded +
                '}';
    }
}

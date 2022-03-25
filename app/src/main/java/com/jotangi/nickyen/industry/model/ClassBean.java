package com.jotangi.nickyen.industry.model;

import com.google.gson.annotations.SerializedName;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/9/23
 * cid: "42",
 * store_id: "35",
 * class_code: "001",
 * class_name: "民族服裝體驗",
 * class_descript: "民族服飾是指各民族本身文化中獨有特色的服飾，也可以稱為地方服飾或民俗服飾。在魅力金三角商圈的民族多以滇、緬、泰為主，近年更有其他新住民如越南、馬來西亞、菲律賓、還有回教民族等。為了讓民族服飾體驗更貼近現代生活，更具時尚感，特別邀請在地雲南特色文創職人，將原有民族服飾透過再設計與加工，讓傳統服飾增添現代美感。 ",
 * class_descript2: "「黑山銀花」店內提供了超過百套不同民族服飾租借換裝體驗，樣式繁多，可供挑選。在換裝過程中了解不同服飾的穿搭技巧，感受各民族工藝之美，及深厚的文化底蘊；換裝後，漫步在富有濃濃多元族裔氣息的魅力金三角商圈與忠貞新村文化園區內，享受自由的風姿搖擺，讓拍照定格，分享美麗與快樂、留住感動與回憶。 體驗流程： 1.現場&線上預約：直接到「黑山銀花」2F現場體驗，或透過線上預約訂購體驗服務。 2.定裝：協助您選擇想要體驗的民族服飾種類，並提供搭配的飾品與配件。 3.外出拍照：可前往忠貞新村文化園區及商圈附近景點拍照留念。 4.服飾歸還：體驗時間內，將租借服飾、道具等經確認無損後一併歸還。 租借規則： 1.租借服裝體驗時間為2小時，請於時間內歸還，超過30分鐘則每小時加收100元。 2.租借服裝必須質押有效證件，服裝歸還無誤，退回證件。 3.服裝租借並非個人定制，服裝尺寸無法保證完全符合客戶尺寸，可盡量選擇適合尺寸服飾體驗，服飾職人會協助做服飾尺寸局部固定與調整，以供客戶可方便體驗拍照。 4.租借之服装，請務必愛惜使用，並避免幼童穿著服飾在地上磨擦，若因此造成衣物之破損及無法清洗，需照價賠償(以體驗價的10倍賠償)。 5.租借之服裝歸還後，都會放置清潔處，待清洗乾淨後才會再次提供租借。 6.民族服裝色彩多飽和，紋樣多豐富，建議體驗時下半身盡量以素色為主，可增加拍照美感。 ",
 * class_time: "每日10:00 ~ 15:00",
 * class_price: "200 - 400",
 * class_picture: "uploads/class001.jpg",
 * class_picture2: "uploads/class002.jpg",
 * class_status: "0",
 * class_trash: "0"
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class ClassBean
{
    @SerializedName("cid")
    private String cid;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("class_code")
    private String classCode;
    @SerializedName("class_name")
    private String className;
    @SerializedName("class_descript")
    private String classDescript;
    @SerializedName("class_descript2")
    private String classDescript2;
    @SerializedName("class_time")
    private String classTime;
    @SerializedName("class_price")
    private String classPrice;
    @SerializedName("class_picture")
    private String classPicture;
    @SerializedName("class_picture2")
    private String classPicture2;
    @SerializedName("class_status")
    private String classStatus;
    @SerializedName("class_trash")
    private String classTrash;

    public String getCid()
    {
        return cid;
    }

    public void setCid(String cid)
    {
        this.cid = cid;
    }

    public String getStoreId()
    {
        return storeId;
    }

    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
    }

    public String getClassCode()
    {
        return classCode;
    }

    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassDescript()
    {
        return classDescript;
    }

    public void setClassDescript(String classDescript)
    {
        this.classDescript = classDescript;
    }

    public String getClassTime()
    {
        return classTime;
    }

    public void setClassTime(String classTime)
    {
        this.classTime = classTime;
    }

    public String getClassPrice()
    {
        return classPrice;
    }

    public void setClassPrice(String classPrice)
    {
        this.classPrice = classPrice;
    }

    public String getClassPicture()
    {
        return classPicture;
    }

    public void setClassPicture(String classPicture)
    {
        this.classPicture = classPicture;
    }

    public String getClassStatus()
    {
        return classStatus;
    }

    public void setClassStatus(String classStatus)
    {
        this.classStatus = classStatus;
    }

    public String getClassTrash()
    {
        return classTrash;
    }

    public void setClassTrash(String classTrash)
    {
        this.classTrash = classTrash;
    }

    public String getClassDescript2()
    {
        return this.classDescript2;
    }

    public void setClassDescript2(final String classDescript2)
    {
        this.classDescript2 = classDescript2;
    }

    public String getClassPicture2()
    {
        return this.classPicture2;
    }

    public void setClassPicture2(final String classPicture2)
    {
        this.classPicture2 = classPicture2;
    }

    @Override
    public String toString()
    {
        return "ClassBean{" +
                "cid='" + cid + '\'' +
                ", storeId='" + storeId + '\'' +
                ", classCode='" + classCode + '\'' +
                ", className='" + className + '\'' +
                ", classDescript='" + classDescript + '\'' +
                ", classDescript2='" + classDescript2 + '\'' +
                ", classTime='" + classTime + '\'' +
                ", classPrice='" + classPrice + '\'' +
                ", classPicture='" + classPicture + '\'' +
                ", classPicture2='" + classPicture2 + '\'' +
                ", classStatus='" + classStatus + '\'' +
                ", classTrash='" + classTrash + '\'' +
                '}';
    }
}

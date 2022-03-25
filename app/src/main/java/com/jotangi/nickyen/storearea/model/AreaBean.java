package com.jotangi.nickyen.storearea.model;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/8/27
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class AreaBean
{
    private String areaId;
    private String areaName;
    private String areaDescript;

    public AreaBean(final String areaId, final String areaName, final String areaDescript)
    {
        this.areaId = areaId;
        this.areaName = areaName;
        this.areaDescript = areaDescript;
    }

    public String getAreaId()
    {
        return this.areaId;
    }

    public void setAreaId(final String areaId)
    {
        this.areaId = areaId;
    }

    public String getAreaName()
    {
        return this.areaName;
    }

    public void setAreaName(final String areaName)
    {
        this.areaName = areaName;
    }

    public String getAreaDescript()
    {
        return this.areaDescript;
    }

    public void setAreaDescript(final String areaDescript)
    {
        this.areaDescript = areaDescript;
    }
}

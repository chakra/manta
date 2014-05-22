package com.espendwise.manta.model;

import java.util.Date;

public interface TableObject {

    public Date getAddDate();

    public void setAddDate(Date addDate) ;

    public String getAddBy();

    public void setAddBy(String addBy);

    public Date getModDate();

    public void setModDate(Date modDate);

    public String getModBy();

    public void setModBy(String modBy);
}

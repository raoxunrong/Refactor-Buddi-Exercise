package org.homeunix.thecave.buddi.model.impl;

import java.util.Date;

/**
 * Created by xrrao on 4/10/15.
 */
public class Period {
    private Date startDate;
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Period(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

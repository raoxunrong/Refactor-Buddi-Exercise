package org.homeunix.thecave.buddi.model.impl;

import ca.digitalcave.moss.common.DateUtil;
import org.homeunix.thecave.buddi.model.BudgetCategoryType;

import java.util.Date;

/**
 * Created by xrrao on 4/10/15.
 */
public class BudgetPeriod {
    private final Date startDate;
    private BudgetCategoryType budgetCategoryType;
    private Date endDate;

    public BudgetPeriod(Date date, BudgetCategoryType budgetCategoryType) {
        this.budgetCategoryType = budgetCategoryType;

        this.startDate = budgetCategoryType.getStartOfBudgetPeriod(date);
        this.endDate = budgetCategoryType.getEndOfBudgetPeriod(date);
    }

    public Date getStartDate() {
        return startDate;
    }

    @Override
    public boolean equals(Object obj) {
        return startDate.equals(((BudgetPeriod) obj).startDate);
    }

    public BudgetPeriod nextBudgetPeriod() {
        return new BudgetPeriod(budgetCategoryType.getBudgetPeriodOffset(startDate, 1), budgetCategoryType);
    }

    public boolean before(BudgetPeriod otherBudgetPeriod) {
        return this.startDate.before(otherBudgetPeriod.getStartDate());
    }

    public Date getEndDate() {
        return endDate;
    }

    public BudgetPeriod prev() {
        return new BudgetPeriod(budgetCategoryType.getBudgetPeriodOffset(startDate, -1), budgetCategoryType);
    }

    public long getDaysInPeriod() {
        return budgetCategoryType.getDaysInPeriod(startDate);
    }

    public long daysOverlap(Period period) {
        Date startDate = period.getStartDate().after(getStartDate()) ? period.getStartDate() : getStartDate();
        Date endDate = period.getEndDate().before(getEndDate()) ? period.getEndDate() : getEndDate();
        return (long) DateUtil.getDaysBetween(startDate, endDate, true);
    }
}

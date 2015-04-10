package org.homeunix.thecave.buddi.model.impl;

import org.homeunix.thecave.buddi.model.BudgetCategoryType;

import java.util.Date;

/**
 * Created by xrrao on 4/10/15.
 */
public class BudgetPeriod {
    private final Date startDate;
    private BudgetCategoryType budgetCategoryType;

    public BudgetPeriod(Date date, BudgetCategoryType budgetCategoryType) {
        this.budgetCategoryType = budgetCategoryType;

        this.startDate = budgetCategoryType.getStartOfBudgetPeriod(date);
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
}

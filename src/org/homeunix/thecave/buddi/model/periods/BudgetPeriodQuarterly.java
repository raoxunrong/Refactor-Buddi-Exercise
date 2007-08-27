/*
 * Created on Aug 26, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.model.periods;

import java.util.Date;

import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.model.BudgetPeriodType;
import org.homeunix.thecave.moss.util.DateFunctions;

public class BudgetPeriodQuarterly extends BudgetPeriodType {
	
	public Date getStartOfBudgetPeriod(Date date) {
		return DateFunctions.getStartOfQuarter(date);
	}
	
	public Date getEndOfBudgetPeriod(Date date) {
		return DateFunctions.getEndOfQuarter(date);
	}
	
	public Date getBudgetPeriodOffset(Date date, int offset) {
		return getStartOfBudgetPeriod(DateFunctions.addQuarters(date, offset));
	}
	
	public long getDaysInPeriod(Date date) {
		return DateFunctions.getDaysBetween(getStartOfBudgetPeriod(date), getEndOfBudgetPeriod(date), true);
	}
	
	public String getDateFormat() {
		return "MMM yyyy";
	}
			
	public String getName() {
		return BuddiKeys.BUDGET_PERIOD_QUARTER.toString();
	}
}
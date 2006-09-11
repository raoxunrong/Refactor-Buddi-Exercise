/*
 * Created on Sep 8, 2006 by wyatt
 */
package org.homeunix.drummer.view.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.homeunix.drummer.Buddi;
import org.homeunix.drummer.controller.Translate;
import org.homeunix.drummer.controller.TranslateKeys;
import org.homeunix.drummer.model.Account;
import org.homeunix.drummer.model.Transaction;
import org.homeunix.drummer.prefs.PrefsInstance;
import org.homeunix.drummer.util.Formatter;

public class TransactionCellRenderer extends JLabel implements ListCellRenderer {
	public static final long serialVersionUID = 0;
	private static StringBuffer sb = new StringBuffer();
	
	private Account account;
		
	public TransactionCellRenderer(){
		if (Buddi.isMac()){
			this.putClientProperty("Quaqua.Component.visualMargin", new Insets(0,0,0,0));
		}
	}

	public Component getListCellRendererComponent(JList list, Object obj, int index, boolean isSelected, boolean cellHasFocus) {
		if (obj instanceof Transaction) {
			Transaction transaction = (Transaction) obj;
			setTransaction(transaction, list.getWidth());
		}
		else{
			setTransaction(null, list.getWidth());
		}
			
		if(isSelected)
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		else
			this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		
		
		return this;
	}
		
	/**
	 * Draws the transaction in HTML in the JLabel.
	 * The table is organized as follows:
	 * 20%	40%		20%	20%
	 * 20%	40%		40%
	 * 
	 * The width of the entire table is determined by the width of the JList.
	 * @param transaction
	 * @param width
	 */
	private void setTransaction(Transaction transaction, int width){
		if (sb.length() > 0)
			sb.delete(0, sb.length());
		
		sb.append("<html><table width='" + (width - 20) + "'><tr><td width='20%'>");
		if (transaction != null)
			sb.append(Formatter.getInstance().getDateFormat().format(transaction.getDate()));
		sb.append("</td><td width='40%'>");
		if (transaction != null)
			sb.append(Formatter.getInstance().getLengthFormat(width / 20).format(transaction.getDescription()));
		else
			sb.append("<font color='gray'>")
			.append(Translate.getInstance().get(TranslateKeys.NEW_TRANSACTION))
			.append("</font>");
		
		sb.append("</td><td width='20%'>");
		if (transaction != null)
			sb.append(PrefsInstance.getInstance().getPrefs().getCurrencySymbol())
			.append(Formatter.getInstance().getDecimalFormat().format(((double) transaction.getAmount()) / 100.0));

		sb.append("</td><td width='20%'>");
		if (transaction != null){
			long balanceValue;
			if (account != null){
				if (transaction.getFrom() instanceof Account 
						&& transaction.getFrom().equals(account))
					balanceValue = transaction.getBalanceFrom();
				else
					balanceValue = transaction.getBalanceTo();
				
				if (balanceValue < 0){
					sb.append("<font color='red'>");
					if (balanceValue <= 0 && balanceValue != 0)
						balanceValue *= -1;
					sb.append(PrefsInstance.getInstance().getPrefs().getCurrencySymbol());
					sb.append(Formatter.getInstance().getDecimalFormat().format(((double) balanceValue) / 100.0))
					.append("</font>");
				}
				else{
					sb.append(PrefsInstance.getInstance().getPrefs().getCurrencySymbol());
					sb.append(Formatter.getInstance().getDecimalFormat().format(((double) balanceValue) / 100.0));
				}
			}
		}
		sb.append("</td></tr><tr><td width='20%'>");
		if (transaction != null){
			if (PrefsInstance.getInstance().getPrefs().isShowAdvanced()){
				sb.append((transaction.isCleared() ? "<font color='green'>" + Translate.getInstance().get(TranslateKeys.CLEARED_SHORT) + " </font>" : "  "));
				sb.append((transaction.isReconciled() ? "<font color='green'>" + Translate.getInstance().get(TranslateKeys.RECONCILED_SHORT) + " </font>" : "  "));
			}
			sb.append(Formatter.getInstance().getLengthFormat(width / 40).format(transaction.getNumber()));
		}
		sb.append("</td><td width='40%'>");
		if (transaction != null){
			sb.append(Formatter.getInstance().getLengthFormat(width / 20).format(
					transaction.getFrom() 
					+ " " 
					+ Translate.getInstance().get(TranslateKeys.TO)
					+ " "
					+ transaction.getTo()
			));	
		}
		sb.append("</td><td colspan=2 width='40%'>");
		if (transaction != null){
			sb.append(Formatter.getInstance().getLengthFormat(width / 20).format(transaction.getMemo()));
		}
		sb.append("</td></tr></table></html>");
		
		this.setText(sb.toString());
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
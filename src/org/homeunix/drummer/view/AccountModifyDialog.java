/*
 * Created on May 14, 2006 by wyatt
 */
package org.homeunix.drummer.view;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;

import javax.swing.JOptionPane;

import org.homeunix.drummer.controller.SourceController;
import org.homeunix.drummer.controller.Translate;
import org.homeunix.drummer.controller.TranslateKeys;
import org.homeunix.drummer.controller.TypeController;
import org.homeunix.drummer.model.Account;
import org.homeunix.drummer.model.ModelFactory;
import org.homeunix.drummer.model.Type;
import org.homeunix.drummer.prefs.PrefsInstance;
import org.homeunix.thecave.moss.gui.abstracts.window.AbstractDialog;

public class AccountModifyDialog extends AbstractModifyDialog<Account> {
	public static final long serialVersionUID = 0;

	public AccountModifyDialog(Account account){
		super(MainFrame.getInstance(), account);
		amountLabel.setText(Translate.getInstance().get(TranslateKeys.STARTING_BALANCE));
		pulldownLabel.setText(Translate.getInstance().get(TranslateKeys.ACCOUNT_TYPE));
		check.setVisible(false);
		gap.setVisible(false);
	}

	public AbstractDialog init() {
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		pulldownModel.removeAllElements();
		pulldownModel.addElement(null);

		for (Type t : TypeController.getTypes()) {
			pulldownModel.addElement(t);
		}

		pulldown.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if (!PrefsInstance.getInstance().getPrefs().isShowCreditLimit())
					creditLimit.setValue(0);

				boolean isCreditAccount;
				if (pulldown.getSelectedItem() instanceof Type){
					Type t = (Type) pulldown.getSelectedItem();
					isCreditAccount = t.isCredit();
				}
				else
					isCreditAccount = false;

				if (isCreditAccount){
					creditLimitLabel.setText(Translate.getInstance().get(TranslateKeys.CREDIT_LIMIT) + " " + Translate.getInstance().get(TranslateKeys.OPTIONAL_TAG));
				}
				else{
					creditLimitLabel.setText(Translate.getInstance().get(TranslateKeys.OVERDRAFT_LIMIT) + " " + Translate.getInstance().get(TranslateKeys.OPTIONAL_TAG));
				}
			}
		});

		if (source == null){
			name.setText("");
			amount.setValue(0);
			creditLimit.setValue(0);
			interestRate.setValue(0);
			pulldown.setSelectedItem(null);
			pulldown.setEnabled(true);
			this.setTitle(Translate.getInstance().get(TranslateKeys.ACCOUNT_MODIFY_NEW));
		}
		else{
			name.setText(source.getName());
			if (source.isCredit())
				amount.setValue(source.getStartingBalance() * -1);
			else
				amount.setValue(source.getStartingBalance());
			creditLimit.setValue(Math.abs(source.getCreditLimit()));
			interestRate.setValue(Math.abs(source.getInterestRate()));

			pulldown.setSelectedItem(source.getAccountType());
			pulldown.setEnabled(false);
			this.setTitle(Translate.getInstance().get(TranslateKeys.ACCOUNT_MODIFY_EDIT));
		}

		return this;
	}

	public AbstractDialog updateContent(){
		return this;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(okButton)) {
			if (name.getText().length() == 0 || pulldown.getSelectedItem() == null){
				String[] options = new String[1];
				options[0] = Translate.getInstance().get(TranslateKeys.BUTTON_OK);

				JOptionPane.showOptionDialog(
						AccountModifyDialog.this, 
						Translate.getInstance().get(TranslateKeys.ENTER_ACCOUNT_NAME_AND_TYPE),
						Translate.getInstance().get(TranslateKeys.MORE_INFO_NEEDED),
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						options,
						options[0]);
				return;
			}
			
			final Account a;
			if (source == null){
				for (Account account : SourceController.getAccounts()) {
					if (account.getName().equalsIgnoreCase(name.getText())){
						String[] options = new String[1];
						options[0] = Translate.getInstance().get(TranslateKeys.BUTTON_OK);

						JOptionPane.showOptionDialog(
								AccountModifyDialog.this, 
								Translate.getInstance().get(TranslateKeys.NAME_MUST_BE_UNIQUE),
								Translate.getInstance().get(TranslateKeys.ERROR),
								JOptionPane.DEFAULT_OPTION,
								JOptionPane.ERROR_MESSAGE,
								null,
								options,
								options[0]);
						return;					
					}
				}

				a = ModelFactory.eINSTANCE.createAccount();
				a.setAccountType((Type) pulldown.getSelectedItem());

				//We don't want to modify creation date unless we
				// truly ARE creating it now.  This is in regards
				// to a fix for bug #1650070.
				a.setCreationDate(new Date());
			}
			else{
				a = source;
			}

			a.setName(name.getText());

			long startingBalance = amount.getValue();
			if (a.isCredit())
				startingBalance = startingBalance * -1;

			a.setStartingBalance(startingBalance);					

			if (creditLimit.isEnabled() && creditLimit.getValue() != 0)
				a.setCreditLimit(creditLimit.getValue());

			if (interestRate.getValue() != 0)
				a.setInterestRate(interestRate.getValue());

			SourceController.addAccount(a);

			a.calculateBalance();

			if (source == null)
				SourceController.addAccount(a);

			AccountModifyDialog.this.closeWindow();				
			MainFrame.getInstance().getAccountListPanel().updateContent();
			TransactionsFrame.updateAllTransactionWindows();
			
		}
		else if (e.getSource().equals(cancelButton)){
			AccountModifyDialog.this.closeWindow();
//			MainFrame.getInstance().getAccountListPanel().updateContent();
		}
	}
}

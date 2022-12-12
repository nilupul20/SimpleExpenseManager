package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class DbAccountDAO implements AccountDAO {
    private final DBHandler dbHandler;

    public DbAccountDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return dbHandler.getAccountNumbers();
    }

    @Override
    public List<Account> getAccountsList() {
        return dbHandler.getAccounts() ;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account = dbHandler.getAccount(accountNo);
        if(account == null) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        return account;
    }

    @Override
    public void addAccount(Account account) {
        dbHandler.addAccount(account);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (! dbHandler.getAccountNumbers().contains(accountNo) ){
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        else{
            dbHandler.removeAccount(accountNo);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = dbHandler.getAccount(accountNo);
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        dbHandler.updateAccount(account);
    }
}

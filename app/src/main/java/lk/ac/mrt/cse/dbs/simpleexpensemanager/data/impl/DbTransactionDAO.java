package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DbTransactionDAO implements TransactionDAO {
    private final DBHandler dbHandler;

    public DbTransactionDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        dbHandler.addTransaction(accountNo, date, expenseType.name(), amount);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dbHandler.getTransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = dbHandler.getTransactions();
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        return transactions.subList(size - limit, size);
    }
}

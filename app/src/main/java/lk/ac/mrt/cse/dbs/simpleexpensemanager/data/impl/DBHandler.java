package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DBHandler extends SQLiteOpenHelper {
    private static final int VERSION = 2 ;
    private static final String DBName = "Expense Manager" ;

    // ACCOUNT table
    private static final String ACCOUNT_TABLE = "account" ;
    private static final String ACCOUNT_NO = "accountNo" ;
    private static final String BANK_NAME = "bankName" ;
    private static final String ACCOUNT_HOLDER = "accountHolderName" ;
    private static final String BALANCE = "balance" ;


    // TRANSACTION table
    private static final String TRANSACTION_TABLE = "transactions";
    private static final String TRANSACTION_ID = "id";
    private static final String DATE = "date" ;
    private static final String EXPENSE_TYPE = "expenseType" ;
    private static final String AMOUNT = "amount" ;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DBHandler(@Nullable Context context) {
        super(context, DBName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAccount = "create table "
                + ACCOUNT_TABLE + " ( "
                + ACCOUNT_NO + " text primary key, "
                + BANK_NAME + " text not null , "
                + ACCOUNT_HOLDER + " text not null , "
                + BALANCE + " decimal(10,2) ); ";

        String createTransaction = "create table "
                + TRANSACTION_TABLE + " ( "
                + TRANSACTION_ID + " integer primary key autoincrement, "
                + DATE + " text not null , "
                + ACCOUNT_NO + " text not null, "
                + EXPENSE_TYPE + " text not null , "
                + AMOUNT + " decimal(10,2) not null, "
                + "foreign key ("+ACCOUNT_NO+") references "+ACCOUNT_TABLE+"("+ACCOUNT_NO+"));";

        db.execSQL(createAccount);
        db.execSQL(createTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String dropAccount = "drop table if exists " + ACCOUNT_TABLE + " ; " ;
        String dropTransaction = "drop table if exists " + TRANSACTION_TABLE +" ;";
        db.execSQL(dropTransaction);
        db.execSQL(dropAccount);
        onCreate(db);
    }

    public void addAccount(Account account){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, account.getAccountNo());
        values.put(BANK_NAME, account.getBankName());
        values.put(ACCOUNT_HOLDER, account.getAccountHolderName());
        values.put(BALANCE, account.getBalance());
        db.insert(ACCOUNT_TABLE,null, values);
        db.close();
    }

    public void updateAccount(Account account){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, account.getAccountNo());
        values.put(BANK_NAME, account.getBankName());
        values.put(ACCOUNT_HOLDER, account.getAccountHolderName());
        values.put(BALANCE, account.getBalance());
        db.update(ACCOUNT_TABLE, values, ACCOUNT_NO + " = ?", new String[]{account.getAccountNo()} );
        db.close();
    }

    public Account getAccount(String accNo){
        Account account = null;
        SQLiteDatabase db = getReadableDatabase();
        // Cursor c = db.rawQuery("select * from " + ACCOUNT_TABLE + " where " + ACCOUNT_NO + " = " + accNo, null );
        Cursor c = db.query(ACCOUNT_TABLE, null, ACCOUNT_NO +" =?" ,new String[]{accNo}, null, null, null, null);
        if (c.moveToFirst()) {
            account = new Account(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    c.getDouble(3) );
        }
        c.close();
        db.close();
        return account;
    }

    public List<String> getAccountNumbers(){
        List<String> accountNumbers = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        // Cursor c = db.rawQuery("select " + ACCOUNT_NO + " from " + ACCOUNT_TABLE , null );
        Cursor c = db.query(ACCOUNT_TABLE, null, null ,null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                accountNumbers.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return accountNumbers;
    }

    public List<Account> getAccounts(){
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + ACCOUNT_TABLE , null );
        if (c.moveToFirst()) {
            do {
                accounts.add(new Account(
                        c.getString(0),
                        c.getString(1),
                        c.getString(2),
                        c.getDouble(3) ));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return accounts;
    }

    public void removeAccount( String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ACCOUNT_TABLE, ACCOUNT_NO + " =?", new String[]{accountNo});
        db.close();
    }

    public void addTransaction(String accountNo, Date date, String expenseType, double amount){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACCOUNT_NO, accountNo);
        values.put(DATE, dateFormat.format(date));
        values.put(AMOUNT, amount);
        values.put(EXPENSE_TYPE, expenseType);
        db.insert(TRANSACTION_TABLE,null, values);
        db.close();
    }

    public List<Transaction> getTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + TRANSACTION_TABLE , null );
        try {
            if (c.moveToFirst()) {
                do {
                    transactions.add(new Transaction(
                            dateFormat.parse(c.getString(1)),
                            c.getString(2),
                            ExpenseType.valueOf(c.getString(3)),
                            c.getDouble(4)));
                } while (c.moveToNext());
            }
        } catch (Exception e){}

        c.close();
        db.close();
        return transactions;
    }
}


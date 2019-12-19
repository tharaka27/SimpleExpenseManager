package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl_sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.databaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class SqlAccountDAO implements AccountDAO {
    private databaseHelper databaseHelper;

    public SqlAccountDAO(databaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(databaseHelper.ACCOUNTS_TABLE, new String[] {databaseHelper.KEY_ACCOUNT_NO}, null, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding account to list
                accountNumbersList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return list
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(databaseHelper.ACCOUNTS_TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3))
                );

                accountList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(databaseHelper.ACCOUNTS_TABLE, null, databaseHelper.KEY_ACCOUNT_NO + "=?",
                new String[] { accountNo }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(databaseHelper.KEY_ACCOUNT_NO, account.getAccountNo());
        values.put(databaseHelper.KEY_BANK_NAME, account.getBankName());
        values.put(databaseHelper.KEY_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        values.put(databaseHelper.KEY_BALANCE, account.getBalance());

        db.insert(databaseHelper.ACCOUNTS_TABLE, null, values);
        db.close();

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(databaseHelper.ACCOUNTS_TABLE, databaseHelper.KEY_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(databaseHelper.KEY_BALANCE, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(databaseHelper.KEY_BALANCE, account.getBalance() + amount);
                break;
        }
        // updating row
        db.update(databaseHelper.ACCOUNTS_TABLE, values, databaseHelper.KEY_ACCOUNT_NO + " = ?",
                new String[] { accountNo });
    }
}

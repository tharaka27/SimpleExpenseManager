package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl_sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.databaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class SqlTransactionDAO implements TransactionDAO {
    private databaseHelper databaseHelper;

    public SqlTransactionDAO(databaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(databaseHelper.KEY_ACCOUNT_NO, accountNo);
        values.put(databaseHelper.KEY_EXPENSE_TYPE, expenseType.name());
        values.put(databaseHelper.KEY_AMOUNT, amount);
        values.put(databaseHelper.KEY_DATE, new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(date));


        db.insert(databaseHelper.TRANSACTIONS_TABLE, null, values);
        db.close();
        Log.d("Came Here", values.toString());
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(databaseHelper.TRANSACTIONS_TABLE, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(cursor.getString(1));
                    Transaction transaction = new Transaction(date, cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)), Double.parseDouble(cursor.getString(4))
                    );
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(databaseHelper.TRANSACTIONS_TABLE, null, null, null, null, null, null, limit + "");


        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).parse(cursor.getString(1));
                    Transaction transaction = new Transaction(date, cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)), Double.parseDouble(cursor.getString(4))
                    );

                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return transactionList;
    }
}

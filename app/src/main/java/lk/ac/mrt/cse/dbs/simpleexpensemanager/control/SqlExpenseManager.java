

package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.databaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl_sql.SqlAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl_sql.SqlTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.databaseHelper;

public class SqlExpenseManager  extends ExpenseManager {
    private databaseHelper db;

    public SqlExpenseManager(databaseHelper db) {
        this.db = db;
        setup();
    }

    @Override
    public void setup() {

        TransactionDAO persistentSqlTransactionDAO = new SqlTransactionDAO(db);
        setTransactionsDAO(persistentSqlTransactionDAO);

        AccountDAO persistSqlAccountDAO = new SqlAccountDAO(db);
        setAccountsDAO(persistSqlAccountDAO);

        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Tharaka Ratnayake", 110000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Vihan Melaka", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);

    }
}

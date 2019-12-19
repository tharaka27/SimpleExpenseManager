package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class databaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "170524B";

    /*
       create tables for ACCOUNTS and TRANSACTIONS;
     */
    public static final String ACCOUNTS_TABLE = "accounts";
    public static final String TRANSACTIONS_TABLE = "transactions";



    /*
       create tables for ACCOUNTS ;
     */
    public static final String KEY_ACCOUNT_NO = "accountNo";
    public static final String KEY_BANK_NAME = "bankName";
    public static final String KEY_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String KEY_BALANCE = "balance";


    /*
       create tables for TRANSACTIONS ;
     */
    private static final String KEY_TRANSACTION_ID = "id";
    public static final String KEY_EXPENSE_TYPE = "expenseType";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_DATE = "date";

    public static databaseHelper instance;

    private static final String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + ACCOUNTS_TABLE + "(" + KEY_ACCOUNT_NO + " TEXT PRIMARY KEY," + KEY_BANK_NAME + " TEXT," + KEY_ACCOUNT_HOLDER_NAME + " TEXT," + KEY_BALANCE + " REAL" + ")";

    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TRANSACTIONS_TABLE + "(" + KEY_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT," + KEY_ACCOUNT_NO + " TEXT," + KEY_EXPENSE_TYPE + " TEXT," + KEY_AMOUNT + " REAL," + "FOREIGN KEY(" + KEY_ACCOUNT_NO + ") REFERENCES "+ ACCOUNTS_TABLE +"(" + KEY_ACCOUNT_NO + ") )";


    public static databaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new databaseHelper(context);
        }
        return instance;
    }


    public databaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        System.out.println(CREATE_ACCOUNTS_TABLE);
        sqLiteDatabase.execSQL(CREATE_ACCOUNTS_TABLE);

        System.out.println(CREATE_TRANSACTIONS_TABLE);
        sqLiteDatabase.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + ACCOUNTS_TABLE + "'");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TRANSACTIONS_TABLE + "'");

        // Create tables again
        onCreate(sqLiteDatabase);
    }
}

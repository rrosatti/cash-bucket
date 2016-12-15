package com.example.rodri.cashbucket.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.rodri.cashbucket.model.AutoDeposit;
import com.example.rodri.cashbucket.model.BankAccount;
import com.example.rodri.cashbucket.model.Transaction;
import com.example.rodri.cashbucket.model.User;
import com.example.rodri.cashbucket.model.Wallet;

/**
 * Created by rodri on 12/14/2016.
 */

public class MyDataSource {

    private SQLiteDatabase db;
    private MySQLiteHelper helper;
    private String[] userColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_USERNAME,
            MySQLiteHelper.COLUMN_PASSWORD
    };
    private String[] bankAccountColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_BALANCE,
    };
    private String[] userBankAccountColumns = {
            MySQLiteHelper.COLUMN_USER_ID,
            MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID
    };
    private String[] autoDepositColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID,
            MySQLiteHelper.COLUMN_VALUE,
            MySQLiteHelper.COLUMN_DAY
    };
    private String[] walletColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_BALANCE
    };
    private String[] userWalletColumns = {
            MySQLiteHelper.COLUMN_USER_ID,
            MySQLiteHelper.COLUMN_WALLET_ID
    };
    private String[] transactionColumns = {
            MySQLiteHelper.KEY_ID,
            MySQLiteHelper.COLUMN_PRICE,
            MySQLiteHelper.COLUMN_DATE,
            MySQLiteHelper.COLUMN_TYPE,
            MySQLiteHelper.COLUMN_USER_ID
    };

    public MyDataSource(Context context) {
        helper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = helper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    /** ------------ CREATE ---------------- */

    public User createUser(String name, String username, String password) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_USERNAME, username);
        values.put(MySQLiteHelper.COLUMN_PASSWORD, password);

        long insertId = db.insert(MySQLiteHelper.TABLE_USER, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_USER, userColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        User newUser = cursorToUser(cursor);
        cursor.close();

        return newUser;
    }

    public BankAccount createBankAccount(double balance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BALANCE, balance);

        long insertId = db.insert(MySQLiteHelper.TABLE_BANK_ACCOUNT, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_BANK_ACCOUNT, bankAccountColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        BankAccount newBankAccount = cursorToBankAccount(cursor);
        cursor.close();

        return newBankAccount;
    }

    public AutoDeposit createAutoDeposit(long bankAccountId, double value, int day) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID, bankAccountId);
        values.put(MySQLiteHelper.COLUMN_VALUE, value);
        values.put(MySQLiteHelper.COLUMN_DAY, day);

        long insertId = db.insert(MySQLiteHelper.TABLE_AUTO_DEPOSIT, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_AUTO_DEPOSIT, autoDepositColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        AutoDeposit autoDeposit = cursorToAutoDeposit(cursor);
        cursor.close();

        return autoDeposit;
    }

    public Wallet createWallet(double balance) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_BALANCE, balance);

        long insertId = db.insert(MySQLiteHelper.TABLE_WALLET, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_WALLET, walletColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        Wallet newWallet = cursorToWallet(cursor);
        cursor.close();
        return newWallet;

    }

    public Transaction createTransaction(double price, long date, int type, long userId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PRICE, price);
        values.put(MySQLiteHelper.COLUMN_DATE, date);
        values.put(MySQLiteHelper.COLUMN_TYPE, type);
        values.put(MySQLiteHelper.COLUMN_USER_ID, userId);

        long insertId = db.insert(MySQLiteHelper.TABLE_TRANSACTION, null, values);
        Cursor cursor = db.query(MySQLiteHelper.TABLE_TRANSACTION, transactionColumns,
                MySQLiteHelper.KEY_ID + " = " + insertId, null, null, null, null, null);

        if (isCursorEmpty(cursor)) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();

        Transaction newTransaction = cursorToTransaction(cursor);
        cursor.close();

        return newTransaction;
    }

    public boolean createUserBankAccount(long userId, long bankAccountId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USER_ID, userId);
        values.put(MySQLiteHelper.COLUMN_BANK_ACCOUNT_ID, bankAccountId);

        long insertId = db.insert(MySQLiteHelper.TABLE_USER_BANK_ACCOUNT, null, values);

        if (insertId != 0) return true;
        else return false;

    }

    public boolean createUserWallet(long userId, long walletId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USER_ID, userId);
        values.put(MySQLiteHelper.COLUMN_WALLET_ID, walletId);

        long insertId = db.insert(MySQLiteHelper.TABLE_USER_WALLET, null, values);

        if (insertId != 0) return true;
        else return false;

    }


    /** ------------ CURSOR TO ---------------- */

    public User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        user.setUsername(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        return user;
    }

    public BankAccount cursorToBankAccount(Cursor cursor) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(cursor.getLong(0));
        bankAccount.setBalance(cursor.getDouble(1));
        return bankAccount;
    }

    public AutoDeposit cursorToAutoDeposit(Cursor cursor) {
        AutoDeposit autoDeposit = new AutoDeposit();
        autoDeposit.setId(cursor.getLong(0));
        autoDeposit.setBankAccountId(cursor.getLong(1));
        autoDeposit.setValue(cursor.getDouble(2));
        autoDeposit.setDay(cursor.getInt(3));
        return autoDeposit;
    }

    public Wallet cursorToWallet(Cursor cursor) {
        Wallet wallet = new Wallet();
        wallet.setId(cursor.getLong(0));
        wallet.setBalance(cursor.getDouble(1));
        return wallet;
    }

    public Transaction cursorToTransaction(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getLong(0));
        transaction.setPrice(cursor.getDouble(1));
        transaction.setDate(cursor.getLong(2));
        transaction.setType(cursor.getInt(3));
        transaction.setUserId(cursor.getLong(4));
        return transaction;
    }

    /** ------------ EXTRA ---------------- */

    public boolean isCursorEmpty(Cursor cursor) {
        if (cursor.moveToFirst()) {
            System.out.println("The cursor is empty!!");
            return false;
        }else {
            return true;
        }
    }
}

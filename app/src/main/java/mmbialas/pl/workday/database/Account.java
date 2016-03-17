package mmbialas.pl.workday.database;

/**
 * Created by liangfeng on 2016/3/17.
 */

public class Account
{
    public int _id;
    public String accountId;
    public String date;
    public String info;

    public Account()
    {
    }

    public Account(String accountId,String date, String info)
    {
        this.accountId = accountId;
        this.date = date;
        this.info = info;
    }

}
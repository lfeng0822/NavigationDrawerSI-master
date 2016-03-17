package mmbialas.pl.workday.database;

/**
 * Created by liangfeng on 2016/3/17.
 */

public class Account
{
    public int _id;
    public String date;
    public String info;

    public Account()
    {
    }

    public Account(int id,String date, String info)
    {
        this._id = id;
        this.date = date;
        this.info = info;
    }

}
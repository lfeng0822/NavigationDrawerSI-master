package mmbialas.pl.workday.data;

import java.util.Calendar;

/**
 * Created by liangfeng on 2016/3/16.
 */
public class DateEntry implements Comparable<DateEntry> {

    private int mMonth;
    private int mDay;


    public DateEntry(Calendar calendar) {
        this.mMonth = calendar.get(Calendar.MONTH) + 1;
        this.mDay = calendar.get(Calendar.DAY_OF_MONTH);

    }

    public DateEntry(int month,int day)
    {
        this.mMonth = month;
        this.mDay = day;
    }


    public int getmDay() {
        return mDay;
    }

    public int getmMonth() {
        return mMonth;
    }


    @Override
    public int compareTo(DateEntry dateEntry) {
        if(dateEntry.mMonth > mMonth){
            return 1;
        }else if((dateEntry.mMonth == mMonth) && (dateEntry.mDay > mDay))
        {
            return 2;
        }else if((dateEntry.mMonth == mMonth) && (dateEntry.mDay == mDay))
        {
            return 3;
        }else {
            return 0;
        }
    }
}

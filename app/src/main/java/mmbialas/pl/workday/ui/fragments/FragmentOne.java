package mmbialas.pl.workday.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mmbialas.pl.workday.R;
import mmbialas.pl.workday.data.DateEntry;
import mmbialas.pl.workday.database.Account;
import mmbialas.pl.workday.database.DBManager;

/**
 * Created by Michal Bialas on 19/07/14.
 */
public class FragmentOne extends Fragment implements View.OnClickListener {



    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
    private String dateSet;
    private Calendar calendar;
    private Calendar expireCalender;
    private Context mContext;
    private Date mDate;
    private DBManager dbManager;
    private String accountId;
    private String accountDate;
    private String accountInfo;


    private DateEntry currDateEntry;

    static DateEntry[] holiday_2016 = {
            new DateEntry(1, 1), new DateEntry(1, 2), new DateEntry(1, 3),
            new DateEntry(2, 7), new DateEntry(2, 8), new DateEntry(2, 9), new DateEntry(2, 10), new DateEntry(2, 11), new DateEntry(2, 12), new DateEntry(2, 13),
            new DateEntry(4, 2), new DateEntry(4, 3), new DateEntry(4, 4),
            new DateEntry(4, 30), new DateEntry(5, 1), new DateEntry(5, 2),
            new DateEntry(6, 9), new DateEntry(6, 10), new DateEntry(6, 11),
            new DateEntry(9, 15), new DateEntry(9, 16), new DateEntry(9, 17),
            new DateEntry(10, 1), new DateEntry(10, 2), new DateEntry(10, 3), new DateEntry(10, 4), new DateEntry(10, 5), new DateEntry(10, 6), new DateEntry(10, 7)};

    static DateEntry[] special_workday = {
            new DateEntry(2, 6), new DateEntry(2, 14), new DateEntry(6, 12), new DateEntry(9, 18), new DateEntry(10, 8), new DateEntry(10, 9)
    };


    static int[] interval_workdays = {
            26, 36, 19, 27, 69, 11, 60
    };


    @InjectView(R.id.editEntryDate)
    EditText editEntryDate;
    @InjectView(R.id.btnGetToday)
    Button btnGetToday;
    @InjectView(R.id.btnCalcExpire)
    Button btnCalcExpire;
    @InjectView(R.id.editEntryUserId)
    EditText editEntryUserId;
    @InjectView(R.id.btnSave)
    Button btnSave;
    @InjectView(R.id.expireResult)
    TextView expireResult;
    @InjectView(R.id.entryDate)
    TextView entryDate;
    @InjectView(R.id.editEntryUserNote)
    EditText editEntryUserNote;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState) {
        mContext = this.getActivity();
        calendar = Calendar.getInstance();
        View view = inflater.inflate(R.layout.fragment_calculate, containter, false);
        ButterKnife.inject(this, view);

        editEntryDate.setOnClickListener(this);
        btnGetToday.setOnClickListener(this);
        btnCalcExpire.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        // 初始化DBManager
        dbManager = new DBManager(mContext);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbManager.closeDB();// 释放数据库资源
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.editEntryDate:
                Log.d("FragmentOne", "----- onClick -----editEntryDate");
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        mContext, DateSet, calendar
                        .get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.btnCalcExpire:
                calcExpire(mDate, calendar);
                break;
            case R.id.btnSave:
                saveAccountToDatebase();
                break;
            case R.id.btnGetToday:
                mDate = new Date(System.currentTimeMillis());
                calendar.setTime(mDate);
                currDateEntry = new DateEntry(calendar);
                editEntryDate.setText(formatter.format(mDate));
                break;
        }

    }

    private void calcExpire(Date mDate, Calendar calendar) {
        if (mDate != null && calendar != null && !editEntryDate.getText().equals("")) {
            if (isSpecialWorkday(currDateEntry)) {
                Toast.makeText(mContext, "Is special Workday", Toast.LENGTH_SHORT).show();
            }

            for (DateEntry dateEntry : holiday_2016) {

                int result = dateEntry.compareTo(currDateEntry);
                if (result == 0) {
                    /*
                    Calendar nearCalendar = Calendar.getInstance();
                    nearCalendar.set(Calendar.MONTH,dateEntry.getmMonth() - 1);
                    nearCalendar.set(Calendar.DAY_OF_MONTH, dateEntry.getmDay());

                    //Toast.makeText(mContext,"curr date before " + dateEntry.getmMonth() + "月" + dateEntry.getmDay() + "日" + "Workday = " + getWorkingDay(calendar,nearCalendar) ,Toast.LENGTH_SHORT).show();

                    //test for holiday start
                    Calendar test1Calendar = Calendar.getInstance();
                    test1Calendar.set(Calendar.MONTH,10 - 1);
                    test1Calendar.set(Calendar.DAY_OF_MONTH, 7);

                    Calendar test2Calendar = Calendar.getInstance();

                    test2Calendar.set(Calendar.MONTH,12 - 1);
                    test2Calendar.set(Calendar.DAY_OF_MONTH, 31 - 1);

                    Toast.makeText(mContext,"workday " + getWorkingDay(test1Calendar,test2Calendar) ,Toast.LENGTH_LONG).show();

                    //test for holiday end
                    */
                    calcDateForWorkday(calendar, dateEntry);
                    break;
                } else if (result == 3) {
                    Toast.makeText(mContext, "curr date equals " + dateEntry.getmMonth() + "月" + dateEntry.getmDay() + "日", Toast.LENGTH_SHORT).show();
                    break;
                }

            }

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.please_input_date), Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean isSpecialWorkday(DateEntry dateEntry) {
        boolean mIsSpecial = false;
        for (DateEntry mDateEntry : special_workday) {
            if (dateEntry.compareTo(mDateEntry) == 3) {
                mIsSpecial = true;
                break;
            }

        }
        return mIsSpecial;
    }


    DatePickerDialog.OnDateSetListener DateSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // 每次保存设置的日期
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String str = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            System.out.println("set is " + str);

            mDate = calendar.getTime();
            currDateEntry = new DateEntry(calendar);
            editEntryDate.setText(formatter.format(mDate));
        }
    };


    private int getWorkingDay(Calendar d1, Calendar d2) {
        int result = -1;
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int betweendays = getDaysBetween(d1, d2);
        int charge_date = 0;
        int charge_start_date = 0;//开始日期的日期偏移量
        int charge_end_date = 0;//结束日期的日期偏移量
        // 日期不在同一个日期内
        int stmp;
        int etmp;
        stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
        etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
        if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
            charge_start_date = stmp - 1;
        }
        if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
            charge_end_date = etmp - 1;
        }

        result = (getDaysBetween(this.getNextMonday(d1), this.getNextMonday(d2)) / 7)
                * 5 + charge_start_date - charge_end_date;

        System.out.println("between day is-->" + betweendays);
        return result;

    }


    private Calendar getNextMonday(Calendar date) {
        Calendar result = null;
        result = date;
        do {
            result = (Calendar) result.clone();
            result.add(Calendar.DATE, 1);
        } while (result.get(Calendar.DAY_OF_WEEK) != 2);
        return result;
    }


    private int getDaysBetween(Calendar d1, Calendar d2) {
        if (d1.after(d2)) { // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR)
                - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }


    private int getHolidayIndex(DateEntry dateEntry) {
        int month = dateEntry.getmMonth();
        int day = dateEntry.getmDay();
        int index = 0;
        switch (month) {
            case 1:
                index = 0;
                break;
            case 2:
                index = 1;
                break;
            case 4:
                if (day > 5) {
                    index = 3;
                } else {
                    index = 2;
                }
                break;
            case 5:
                index = 3;
                break;
            case 6:
                index = 4;
                break;
            case 9:
                index = 5;
                break;
            case 10:
                index = 6;
                break;
            default:
                break;
        }
        return index;
    }


    private DateEntry getHolidayDateEntry(int index) {
        int month = 0;
        int day = 0;
        switch (index) {
            case 0:
                month = 1;
                day = 3;
                break;
            case 1:
                month = 2;
                day = 13;
                break;
            case 2:
                month = 4;
                day = 4;
                break;
            case 3:
                month = 5;
                day = 2;
                break;
            case 4:
                month = 6;
                day = 11;
                break;
            case 5:
                month = 9;
                day = 17;
                break;
            case 6:
                month = 10;
                day = 7;
                break;
            default:
                break;
        }
        return new DateEntry(month, day);
    }


    private int getWeekIndex(DateEntry dateEntry) {
        int index = 0;
        Calendar nearCalendar = Calendar.getInstance();
        nearCalendar.set(Calendar.MONTH, dateEntry.getmMonth() - 1);
        nearCalendar.set(Calendar.DAY_OF_MONTH, dateEntry.getmDay());
        index = nearCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        return index;
    }


    private void calcDateForWorkday(Calendar calendar, DateEntry holidayEntry) {
        Log.d("calcDateForWorkday", " month = " + holidayEntry.getmMonth() + " day = " + holidayEntry.getmDay());
        int indexOfHoliday = getHolidayIndex(holidayEntry);
        int workdayForNear = 0;
        int workdayForLast = 0;
        int workdayRemain = 85;
        int lastNeedWeeks;
        int lastNeedDays;
        int realDays;
        int daysBetweenModay;
        Calendar nearCalendar = Calendar.getInstance();
        nearCalendar.set(Calendar.MONTH, holidayEntry.getmMonth() - 1);
        nearCalendar.set(Calendar.DAY_OF_MONTH, holidayEntry.getmDay());
        workdayForNear = getWorkingDay(calendar, nearCalendar);
        workdayRemain = workdayRemain - workdayForNear;
        Log.d("calcDateForWorkday", " indexOfHoliday = " + indexOfHoliday + " workdayForNear = " + workdayForNear);

        do {

            if (indexOfHoliday < 5) {
                Log.d("calcDateForWorkday", " workdayRemain = " + workdayRemain + "interval_workdays[indexOfHoliday] " + interval_workdays[indexOfHoliday]);

                workdayRemain = workdayRemain - interval_workdays[indexOfHoliday];
                indexOfHoliday++;
            }

            Log.d("calcDateForWorkday", " workdayRemain = " + workdayRemain);
            Log.d("calcDateForWorkday", " indexOfHoliday = " + indexOfHoliday);

        } while (interval_workdays[indexOfHoliday] < workdayRemain);
        DateEntry mHolidayDateEntry = getHolidayDateEntry(indexOfHoliday);
        //Toast.makeText(mContext,"workdayRemain = " + workdayRemain + " indexOfHoliday " + indexOfHoliday + " month " +
        //       mHolidayDateEntry.getmMonth() + " day " + mHolidayDateEntry.getmDay(),Toast.LENGTH_LONG).show();


        Calendar holidayCalendar = Calendar.getInstance();
        holidayCalendar.set(Calendar.MONTH, mHolidayDateEntry.getmMonth() - 1);
        holidayCalendar.set(Calendar.DAY_OF_MONTH, mHolidayDateEntry.getmDay());

        Log.d("calcDateForWorkday", " mHolidayDateEntry = " + (holidayCalendar.get(Calendar.MONTH) + 1) + " mHolidayDateEntry.getmDay() = " + holidayCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar nextModday = getNextMonday(holidayCalendar);
        daysBetweenModay = getDaysBetweenModay(holidayCalendar, nextModday, indexOfHoliday);
        Log.d("calcDateForWorkday", " daysBetweenModay = " + daysBetweenModay + " workdayRemain = " + workdayRemain);

        if (daysBetweenModay < workdayRemain) {
            workdayRemain = workdayRemain - daysBetweenModay;
            lastNeedWeeks = workdayRemain / 5;
            lastNeedDays = workdayRemain % 5;
            if (lastNeedDays > 0) {
                realDays = daysBetweenModay + lastNeedWeeks * 7 + lastNeedDays;
            } else {
                realDays = daysBetweenModay + lastNeedWeeks * 7 - 2;
            }

            Log.d("calcDateForWorkday", " workdayRemain = " + workdayRemain
                    + " lastNeedWeeks = " + lastNeedWeeks
                    + " lastNeedDays = " + lastNeedDays
                    + " realDays = " + realDays);

        } else {
            realDays = daysBetweenModay;
        }
        expireCalender = getExpireCalender(holidayCalendar, realDays);

        Log.d("calcDateForWorkday", " MONTH = " + (expireCalender.get(Calendar.MONTH) + 1)
                + " DAY = " + expireCalender.get(Calendar.DAY_OF_MONTH)
                + " realDays = " + realDays);

        //Toast.makeText(mContext, "month = " + (expireCalender.get(Calendar.MONTH) + 1) +
        //        " Day = " + expireCalender.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_LONG).show();
        accountDate = "2016年" + (expireCalender.get(Calendar.MONTH) + 1) + "月" + expireCalender.get(Calendar.DAY_OF_MONTH) + "日";
        expireResult.setText("到期日为 2016 年" + (expireCalender.get(Calendar.MONTH) + 1) + " 月 " +
                expireCalender.get(Calendar.DAY_OF_MONTH) + " 日 ");

    }


    private void saveAccountToDatebase() {
        if (accountDate != null && !accountDate.equals("")) {
            if (!editEntryUserId.getText().equals("")) {
                accountId = editEntryUserId.getText().toString();
                if (!editEntryUserNote.getText().equals("")){
                    accountInfo = editEntryUserNote.getText().toString();
                    Account account = new Account(accountId,accountDate,accountInfo);
                    if(!dbManager.isAccountExist(account))
                    {
                        dbManager.add(account);
                    }else{
                        Toast.makeText(mContext, "此用户已经保存", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(mContext, "用户信息为空，请输入用户信息", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mContext, "户号为空，请输入户号", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, "请计算到期日", Toast.LENGTH_LONG).show();
        }
    }


    private int getDaysBetweenModay(Calendar c1, Calendar c2, int index) {
        int days;
        switch (index) {
            case 2:
            case 4:
            case 5:
                days = getWorkingDay(c1, c2);
                break;
            case 6:
                days = getWorkingDay(c1, c2) + 1;
                break;
            default:
                days = getWorkingDay(c1, c2) - 1;
                break;
        }
        return days;
    }


    private Calendar getExpireCalender(Calendar calendar, int days) {
        Calendar needCalender = calendar;
        needCalender.add(Calendar.DAY_OF_MONTH, days);
        return needCalender;

    }


}

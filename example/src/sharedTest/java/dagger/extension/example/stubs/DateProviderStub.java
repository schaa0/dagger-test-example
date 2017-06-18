package dagger.extension.example.stubs;

import java.util.Calendar;

import dagger.extension.example.service.DateProvider;

public class DateProviderStub extends DateProvider
{

    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minutes;
    private final int seconds;

    public DateProviderStub(int year, int month, int day, int hour, int minutes, int seconds){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public Calendar getCurrentDate()
    {
        Calendar calendar = super.getCurrentDate();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        return calendar;
    }
}

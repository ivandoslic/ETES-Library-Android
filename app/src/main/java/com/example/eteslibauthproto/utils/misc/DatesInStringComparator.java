package com.example.eteslibauthproto.utils.misc;

import android.util.Log;

public class DatesInStringComparator {

    public DatesInStringComparator() {

    }

    public static boolean areDatesTwoHourDifference(String date1, String date2) {
        String[] date1info = splitDateFormat(date1);
        String[] date2info = splitDateFormat(date2);

        if (date1info[0].compareTo(date2info[0]) != 0) {
            return true;
        }

        if (date1info[1].compareTo(date2info[1]) != 0) {
            return true;
        }

        int timeDiff = getTimeDifference(date1info[2], date2info[2]);

        return timeDiff > 7200;
    }

    private static String[] splitDateFormat(String date) {
        String trimmed = date.trim();
        String[] splitInArray = trimmed.split(" ");
        return new String[]{splitInArray[1], splitInArray[2], splitInArray[3]};
    }

    private static int getTimeDifference(String time1, String time2) {
        int timeInSeconds1 = turnHourMinuteSecondsToSeconds(time1);
        int timeInSeconds2 = turnHourMinuteSecondsToSeconds(time2);

        if(timeInSeconds1 == timeInSeconds2)
            return 0;

        return timeInSeconds1 > timeInSeconds2 ? timeInSeconds1 - timeInSeconds2 : timeInSeconds2 - timeInSeconds1;
    }

    private static int turnHourMinuteSecondsToSeconds(String time) {
        String[] hourMinuteSecond = time.split(":");
        int temp = 0;
        temp += Integer.parseInt(hourMinuteSecond[0]) * 3600;
        temp += Integer.parseInt(hourMinuteSecond[1]) * 60;
        temp += Integer.parseInt(hourMinuteSecond[2]);

        return temp;
    }
}

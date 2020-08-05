package com.appoftatar.workgroupcalendar.Common;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.appoftatar.workgroupcalendar.calendar.ItemWorkCalendar;
import com.appoftatar.workgroupcalendar.models.Group;
import com.appoftatar.workgroupcalendar.models.HolidayRequest;
import com.appoftatar.workgroupcalendar.models.User;

import java.util.ArrayList;
import java.util.HashMap;

public  class Common {

//public static HashMap  <String, ArrayList<ItemWorkCalendar>> year;
    public  static HolidayRequest holidayRequest;
public  static User currentUser;
public static boolean manager;
public static boolean aplicationVisible;
public  static String currentGroup;
public static ArrayList<Group> listGroup;


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}

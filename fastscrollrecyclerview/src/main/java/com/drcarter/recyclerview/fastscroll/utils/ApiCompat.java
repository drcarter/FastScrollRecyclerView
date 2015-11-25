package com.drcarter.recyclerview.fastscroll.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Locale;

public class ApiCompat {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        if (VersionUtils.hasLollipop()) {
            return context.getResources().getDrawable(resId, context.getTheme());
        } else {
            return context.getResources().getDrawable(resId);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getFormattedNumber(@NonNull String phoneNumber) {
        if (VersionUtils.hasLollipop()) {
            return PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().getCountry().toUpperCase());
        } else {
            return PhoneNumberUtils.formatNumber(phoneNumber);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void removeOnGlobalLayoutListener(@NonNull View view, @NonNull ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (VersionUtils.hasJellyBean()) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static boolean isScreenOn(@NonNull Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (VersionUtils.hasLollipop()) {
            return pm.isInteractive();
        } else {
            return pm.isScreenOn();
        }
    }

    public static int getColor(@NonNull Context context, @ColorRes int color) {
        if (VersionUtils.hasMarshmallow()) {
            return context.getResources().getColor(color, context.getTheme());
        } else {
            return context.getResources().getColor(color);
        }
    }

    public static ColorStateList getColorStateList(@NonNull Context context, @ColorRes int color) {
        if (VersionUtils.hasMarshmallow()) {
            return context.getResources().getColorStateList(color, context.getTheme());
        } else {
            return context.getResources().getColorStateList(color);
        }
    }
}

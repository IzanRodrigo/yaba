package com.pilasvacias.yaba.util;

import com.pilasvacias.yaba.BuildConfig;

import timber.log.Timber;

/**
 * Created by pablo on 10/11/13.
 * welvi-android
 * <p/>
 * Log module. Inspiration moment with the name.
 * <p/>
 * l.og.
 */
public class L {

    public static final Timber og = BuildConfig.DEBUG ? Timber.DEBUG : Timber.PROD;

    public static final TimeProfiler time = BuildConfig.DEBUG ? TimeProfiler.DEBUG : TimeProfiler.PROD;

}

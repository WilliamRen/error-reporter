package com.guzba.errorreporter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;

public final class UncaughtExceptionReporter implements UncaughtExceptionHandler {
    private static final String TAG = "UncaughtExceptionReporter";
    
    private final Context mContext;
    private final UncaughtExceptionHandler mDefaultUeh;
    
    public UncaughtExceptionReporter(final Context context) {
        this.mContext = context;
        this.mDefaultUeh = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        try {
            reportException(this.mContext, ex, TAG, true);
        } finally {
            this.mDefaultUeh.uncaughtException(thread, ex);
        }
    }
    
    public static void reportException(final Context context, final Throwable ex, final String tag, final boolean fatal) {
        final StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        
        final Intent i = new Intent(ErrorReportingService.INTENT);
        i.putExtra(ErrorReportingService.EXTRA_STACKTRACE, sw.toString());
        i.putExtra(ErrorReportingService.EXTRA_FATAL, fatal);
        i.putExtra(ErrorReportingService.EXTRA_TAG, tag);
        
        context.startService(i);
    }
}

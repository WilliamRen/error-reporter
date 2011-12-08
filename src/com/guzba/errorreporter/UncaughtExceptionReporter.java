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
    
    /**
     * Called when an uncaught exception occurs. This implementation simply sends an intent to our
     * error reporting service and allows the application to force close. Hanging here by blocking
     * on a network request or something similar is both annoying to the user and likely won't finish
     * since when the user hits Force Close, the process is terminated regardless of whether or
     * not this call finished its execution. By sending an intent to our reporting service, Android
     * will restart our process so that our intent can be processed, allowing us to report the exception
     * dependably and without being detrimental to the user's experience.
     * @param thread The thread the uncaught exception was thrown on
     * @param ex The uncaught exception
     */
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        try {
            reportException(this.mContext, ex, TAG, true);
        } finally {
            this.mDefaultUeh.uncaughtException(thread, ex);
        }
    }
    
    /**
     * Helper method that prepares the the intent to start the reporter service
     * @param context Used to start the service
     * @param ex The exception
     * @param tag A tag to help you determine where the error occurred
     * @param fatal Whether or not the exception was fatal
     */
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

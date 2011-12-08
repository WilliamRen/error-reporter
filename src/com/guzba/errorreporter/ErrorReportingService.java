package com.guzba.errorreporter;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public final class ErrorReportingService extends IntentService {
    private static final String TAG = "ErrorReportingService";
    
    public static final String INTENT = "com.guzba.errorreporter.intent.ERROR";
    
    public static final String EXTRA_STACKTRACE = "stacktrace";
    public static final String EXTRA_FATAL = "fatal";
    public static final String EXTRA_TAG = "tag";
    
    public ErrorReportingService() {
        super(TAG);
    }
    
    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d(TAG, "onHandleIntent(intent=" + intent.toString() + ")");
        
        try {
            
            
            
            
            
        } catch (final Exception e) {
            
            
            
        }
    }
}

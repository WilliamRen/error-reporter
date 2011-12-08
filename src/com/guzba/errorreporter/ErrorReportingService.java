package com.guzba.errorreporter;

import com.parse.Parse;
import com.parse.ParseObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public final class ErrorReportingService extends IntentService {
    private static final String TAG = "ErrorReportingService";
    
    private static final String PACKAGE_NAME = "com.guzba.errorreporter";
    
    public static final String INTENT = "com.guzba.errorreporter.intent.ERROR";
    
    public static final String EXTRA_STACKTRACE = "stacktrace";
    public static final String EXTRA_FATAL = "fatal";
    public static final String EXTRA_TAG = "tag";
    
    public ErrorReportingService() {
        super(TAG);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Parse.initialize(this, "APP_ID", "CLIENT_KEY");
    }
    
    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.d(TAG, "onHandleIntent(intent=" + intent.toString() + ")");
        
        try {
            final String trace = intent.getStringExtra(EXTRA_STACKTRACE);
            final String tag = intent.getStringExtra(EXTRA_TAG);
            final boolean fatal = intent.getBooleanExtra(EXTRA_FATAL, true);
            
            ParseObject error;
            if (fatal)
                error = new ParseObject("Fatal");
            else
                error = new ParseObject("Nonfatal");
            
            
            error.put("trace", trace);
            error.put("sdk_int", Integer.toString(Build.VERSION.SDK_INT));
            error.put("model", Build.MODEL);
            error.put("versioncode", Integer.toString(this.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionCode));
            error.put("fatal", Boolean.toString(fatal));
            error.put("tag", tag);
            
            error.save();
        } catch (final Exception e) {
            // guess this one isn't getting reported...
            e.printStackTrace();
        }
    }
}

package com.guzba.errorreporter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public final class DemoActivity extends Activity {
    private static final String TAG = "DemoActivity";
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // You need to call this to set up the hook for when an uncaught exception occurs
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionReporter(this));
        
        this.setContentView(R.layout.main);
        
        this.findViewById(R.id.btn_fatal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                throw new RuntimeException("Oops, this one's fatal!");
                
                // Since this exception isn't caught, it causes the application to force close, but
                // only after the call to our error reporter.
            }
        });
        
        this.findViewById(R.id.btn_nonfatal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    throw new RuntimeException("This one's not a fatal exception.");
                } catch (final Exception e) {
                    UncaughtExceptionReporter.reportException(DemoActivity.this, e, TAG, false);
                    
                    // This is set as nonfatal since it didn't stop the application from functioning but
                    // something bad happened that you might want to know about as the app's developer.
                }
            }
        });
    }
}

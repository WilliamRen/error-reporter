package com.guzba.errorreporter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public final class DemoActivity extends Activity {
    private static final String TAG = "DemoActivity";
    
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionReporter(this));
        
        this.setContentView(R.layout.main);
        
        this.findViewById(R.id.btn_fatal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                throw new RuntimeException("Oops, this one's fatal!");
            }
        });
        
        this.findViewById(R.id.btn_nonfatal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                try {
                    throw new RuntimeException("This one's not a fatal exception.");
                } catch (final Exception e) {
                    UncaughtExceptionReporter.reportException(DemoActivity.this, e, TAG, false);
                }
            }
        });
    }
}

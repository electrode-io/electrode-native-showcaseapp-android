package com.walmartlabs.ern.showcase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.walmartlabs.ern.container.ElectrodeReactActivityDelegate;

public abstract class ElectrodeCoreActivity extends AppCompatActivity implements ElectrodeReactActivityDelegate.BackKeyHandler, ElectrodeReactActivityListener {
    private static final String TAG = ElectrodeCoreActivity.class.getSimpleName();
    private ElectrodeReactActivityDelegate mReactActivityDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactActivityDelegate = new ElectrodeReactActivityDelegate(this);
        mReactActivityDelegate.setBackKeyHandler(this);
    }

    @Override
    public void onBackKey() {
        //TODO: handle what needs to happen when a back key is pressed on a react native view.
//        Toast.makeText(this, "TODO: Handle onBackKey()", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mReactActivityDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReactActivityDelegate.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReactActivityDelegate.onDestroy();
    }

    @Override
    public void onBackPressed() {
        mReactActivityDelegate.onBackPressed();
    }

    @Override
    public ElectrodeReactActivityDelegate getElectrodeDelegate() {
        if (mReactActivityDelegate == null) {
            throw new IllegalStateException("Something is not right, looks like reactActivityDelegate is not initialized");
        }
        return mReactActivityDelegate;
    }
}

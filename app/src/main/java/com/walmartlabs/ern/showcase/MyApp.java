package com.walmartlabs.ern.showcase;

import android.app.Application;

import com.walmartlabs.electrode.reactnative.bridge.helpers.Logger;
import com.walmartlabs.ern.container.ElectrodeReactContainer;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ElectrodeReactContainer.initialize(this, new ElectrodeReactContainer.Config().isReactNativeDeveloperSupport(true));
        Logger.overrideLogLevel(Logger.LogLevel.DEBUG);
    }
}

package com.walmartlabs.ern.showcase;

import android.app.Application;

import com.walmartlabs.electrode.reactnative.bridge.helpers.Logger;
import com.walmartlabs.ern.container.ElectrodeReactContainer;
import com.walmartlabs.ern.container.plugins.CodePushPlugin;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ElectrodeReactContainer.initialize(
                this,
                new ElectrodeReactContainer.Config().isReactNativeDeveloperSupport(true),
                new CodePushPlugin.Config("LoNcxCcr-T6oPrXuZN1TFGUEvCQ7H1T1L1o7f").enableDebugMode(true));

        Logger.overrideLogLevel(Logger.LogLevel.DEBUG);
    }
}

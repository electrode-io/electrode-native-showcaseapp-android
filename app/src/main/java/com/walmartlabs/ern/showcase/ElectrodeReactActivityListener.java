package com.walmartlabs.ern.showcase;

import android.support.annotation.NonNull;

import com.walmartlabs.ern.container.ElectrodeReactActivityDelegate;

public interface ElectrodeReactActivityListener {

    ElectrodeReactActivityDelegate getElectrodeDelegate();

    void updateTitle(@NonNull String title);
}

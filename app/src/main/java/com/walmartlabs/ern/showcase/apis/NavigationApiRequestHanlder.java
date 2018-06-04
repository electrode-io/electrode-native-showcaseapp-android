package com.walmartlabs.ern.showcase.apis;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ErnShowcaseNavigationApi.ern.api.ErnNavigationApi;
import com.ErnShowcaseNavigationApi.ern.model.ErnRoute;
import com.walmartlabs.electrode.reactnative.bridge.BridgeFailureMessage;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeRequestHandler;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeResponseListener;
import com.walmartlabs.electrode.reactnative.bridge.None;
import com.walmartlabs.ern.container.ElectrodeReactContainer;

public class NavigationApiRequestHanlder {
    public static final String ERN_ROUTE = "ernRoute";

    public static void register() {
        ErnNavigationApi.requests().registerNavigateRequestHandler(new ElectrodeBridgeRequestHandler<ErnRoute, None>() {
            @Override
            public void onRequest(@Nullable ErnRoute ernRoute, @NonNull ElectrodeBridgeResponseListener<None> responseListener) {
                if (ernRoute != null && ElectrodeReactContainer.getCurrentActivity() != null) {
                    String uri = "electrode-native://ern.walmartlabs.com/" + ernRoute.getPath();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.putExtra(ERN_ROUTE, ernRoute.toBundle());
                    ElectrodeReactContainer.startActivitySafely(intent);
                    responseListener.onSuccess(None.NONE);
                } else {
                    responseListener.onFailure(BridgeFailureMessage.create("NAVIGATION_FAILED", "Activity context not found to fire the intent"));
                }
            }
        });
    }
}

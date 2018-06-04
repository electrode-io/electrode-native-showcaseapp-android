package com.walmartlabs.ern.showcase.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ErnShowcaseNavigationApi.ern.model.ErnRoute;

public class GenericMiniAppFragment extends ElectrodeMiniAppFragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ElectrodeMiniAppFragment.
     */
    public static ElectrodeMiniAppFragment newInstance(@NonNull ErnRoute route) {
        ElectrodeMiniAppFragment fragment = new GenericMiniAppFragment();
        putMiniAppNameArgument(route, fragment);
        return fragment;
    }
}

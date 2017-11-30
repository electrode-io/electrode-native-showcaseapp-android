package com.walmartlabs.ern.showcase.fragment;

import android.os.Bundle;

public class MovieListMiniAppFragment extends ElectrodeMiniAppFragment {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ElectrodeMiniAppFragment.
     */
    public static ElectrodeMiniAppFragment newInstance() {
        ElectrodeMiniAppFragment fragment = new MovieListMiniAppFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    String getMiniAppName() {
        return "MovieListMiniApp";
    }
}

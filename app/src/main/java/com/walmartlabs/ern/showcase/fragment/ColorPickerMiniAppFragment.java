package com.walmartlabs.ern.showcase.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ErnShowcaseNavigationApi.ern.model.ErnRoute;
import com.colorpickerApi.ern.api.ColorPickerApi;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeEventListener;
import com.walmartlabs.ern.showcase.R;

public class ColorPickerMiniAppFragment extends ElectrodeMiniAppFragment {

    public static ColorPickerMiniAppFragment newInstance(@NonNull ErnRoute route) {
        ColorPickerMiniAppFragment fragment = new ColorPickerMiniAppFragment();
        putMiniAppNameArgument(route, fragment);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View miniAppView = super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_color_picker, container, false);
        ((FrameLayout) view.findViewById(R.id.color_picker_holder)).addView(miniAppView);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerForColorSelectedEvent();
    }

    private void registerForColorSelectedEvent() {
        ColorPickerApi.events().addColorSelectedEventListener(new ElectrodeBridgeEventListener<String>() {
            @Override
            public void onEvent(@Nullable String selectedColor) {
                if (ColorPickerMiniAppFragment.this.getActivity() != null && !ColorPickerMiniAppFragment.this.getActivity().isFinishing()) {
                    try {
                        ColorPickerMiniAppFragment.this.getActivity().findViewById(R.id.textView2).setBackgroundColor(Color.parseColor(selectedColor));
                        Toast.makeText(ColorPickerMiniAppFragment.this.getActivity(), selectedColor, Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(ColorPickerMiniAppFragment.this.getActivity(), "Invalid color value received : " + selectedColor, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

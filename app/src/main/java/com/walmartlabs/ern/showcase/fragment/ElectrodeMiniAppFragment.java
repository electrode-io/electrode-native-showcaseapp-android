package com.walmartlabs.ern.showcase.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ErnShowcaseNavigationApi.ern.model.ErnRoute;
import com.walmartlabs.electrode.reactnative.bridge.helpers.Logger;
import com.walmartlabs.ern.showcase.ElectrodeReactActivityListener;

import javax.annotation.Nullable;

import static com.walmartlabs.ern.showcase.apis.NavigationApiRequestHanlder.ERN_ROUTE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ElectrodeMiniAppFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public abstract class ElectrodeMiniAppFragment extends Fragment {
    public static final String KEY_MINI_APP_NAME = "MiniAppName";
    private static final String TAG = ElectrodeMiniAppFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private ElectrodeReactActivityListener mElectrodeReactActivityListener;

    private ErnRoute ernRoute;
    /**
     * Registered component name of your MiniApp
     *
     * @return String
     */
    String getMiniAppName() {
        if (null == getArguments() || null == getArguments().getString(KEY_MINI_APP_NAME)) {
            throw new IllegalArgumentException("Arguments missing MiniApp name");
        }
        return getArguments().getString(KEY_MINI_APP_NAME);
    }

    /**
     * Return your bundle that will be passed to the MiniApp when the MiniApp is launched.
     * Return null if no props needs to be passed to a MiniApp when started.
     *
     * @return Bundle
     */
    @Nullable
    Bundle getProps() {
        if(ernRoute != null) {
            return ernRoute.toBundle();
        }
        return null;
    }

    public ElectrodeMiniAppFragment() {
        // Required empty public constructor
    }

    @Override
    @CallSuper
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().getBundle(ERN_ROUTE) != null) {
            ernRoute = new ErnRoute(getArguments().getBundle(ERN_ROUTE));
        } else {
            Logger.w(TAG, "Unable to retrieve Route object");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.getActivity() != null && !this.getActivity().isFinishing()) {
            Log.i(TAG, "Inflate view for MiniApp: " + getMiniAppName());
            return mElectrodeReactActivityListener.getElectrodeDelegate().createMiniAppRootView(getMiniAppName(), getProps());
        } else {
            Log.i(TAG, "Activity is finishing or already destroyed.");
            return null;
        }
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        if(ernRoute.getNavBar() != null) {
            mElectrodeReactActivityListener.updateTitle(ernRoute.getNavBar().getTitle());
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (context instanceof ElectrodeReactActivityListener) {
            mElectrodeReactActivityListener = (ElectrodeReactActivityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ElectrodeReactActivityListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected static void putMiniAppNameArgument(@NonNull ErnRoute route, @NonNull ElectrodeMiniAppFragment fragment) {
        Bundle args = new Bundle();
        args.putString(KEY_MINI_APP_NAME, route.getPath());
        args.putBundle(ERN_ROUTE, route.toBundle());
        fragment.setArguments(args);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

package com.walmartlabs.ern.showcase.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.walmartlabs.ern.showcase.ElectrodeReactActivityListener;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ElectrodeMiniAppFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public abstract class ElectrodeMiniAppFragment extends Fragment {
    private static final String TAG = ElectrodeMiniAppFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private ElectrodeReactActivityListener mElectrodeReactActivityListener;

    /**
     * Registered component name of your MiniApp
     *
     * @return String
     */
    abstract String getMiniAppName();

    /**
     * Return your bundle that will be passed to the MiniApp when the MiniApp is launched.
     * Return null if no props needs to be passed to a MiniApp when started.
     *
     * @return Bundle
     */
    @Nullable
    Bundle getProps() {
        return null;
    }

    public ElectrodeMiniAppFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (this.getActivity() != null && !this.getActivity().isFinishing()) {
            Log.i(TAG, "Inflate view for MiniApp: " + getMiniAppName());
            return mElectrodeReactActivityListener.getElectrodeDelegate().onCreate(this.getActivity(), getMiniAppName(), getProps());
        } else {
            Log.i(TAG, "Activity is finishing or already destroyed.");
            return null;
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

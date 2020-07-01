package com.codepath.apps.restclienttemplate.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.codepath.apps.restclienttemplate.R;

public class ComposeFragment extends DialogFragment {

    private static final String TAG = "ComposeFragment";
    private EditText etCompose;
    public ComposeFragment() {
    }

    public static ComposeFragment newInstance(String title) {
        Log.e(TAG, "In fragment");
        ComposeFragment frag = new ComposeFragment();
        Bundle args = new Bundle();
        args.putString("Compose New Tweet", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etCompose = (EditText) view.findViewById(R.id.etCompose);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "What's going on?");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etCompose.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}

package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SeekBar;
import android.widget.TextView;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.utils.PrefUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private SeekBar intervalSeekBar;
    private TextView intervalValueText;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        intervalSeekBar = view.findViewById(R.id.sleep_interval_seek_bar);
        intervalValueText = view.findViewById(R.id.sleep_interval_value_text);
        intervalSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        int currentInterval = PrefUtils.getSleepInterval(getActivity());
        intervalSeekBar.setProgress(currentInterval / 100);
        intervalValueText.setText(currentInterval + " ms");
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int interval = progress * 100;
        if (fromUser) {
            PrefUtils.setSleepInterval(getActivity(), interval);
        }
        intervalValueText.setText(interval + " ms");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

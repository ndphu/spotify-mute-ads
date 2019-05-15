package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment;


import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SeekBar;
import android.widget.TextView;
import org.w3c.dom.Text;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.observers.AudioVolumeObserver;
import vn.com.phudnguyen.tools.autovolumemanager.listener.observers.OnAudioVolumeChangedListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class VolumeControllerFragment extends Fragment implements OnAudioVolumeChangedListener, SeekBar.OnSeekBarChangeListener {

    private SeekBar volumeSeekBar;
    private TextView volumeValueText;
    private AudioVolumeObserver mAudioVolumeObserver;

    public VolumeControllerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_volume_controller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        volumeSeekBar = view.findViewById(R.id.volume_seek_bar);
        volumeValueText = view.findViewById(R.id.volume_value_text);
        volumeSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAudioVolumeObserver == null) {
            mAudioVolumeObserver = new AudioVolumeObserver(getActivity());
        }
        mAudioVolumeObserver.register(AudioManager.STREAM_MUSIC, this);
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        onAudioVolumeChanged(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAudioVolumeObserver != null) {
            mAudioVolumeObserver.unregister();
        }
    }

    @Override
    public void onAudioVolumeChanged(int currentVolume, int maxVolume) {
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);
        volumeValueText.setText(currentVolume + " of " + maxVolume);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            ((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE)).setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}

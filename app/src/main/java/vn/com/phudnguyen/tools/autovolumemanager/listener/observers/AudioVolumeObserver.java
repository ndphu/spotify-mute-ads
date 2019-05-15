package vn.com.phudnguyen.tools.autovolumemanager.listener.observers;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.support.annotation.NonNull;

public class AudioVolumeObserver {

    private final Context mContext;
    private final AudioManager mAudioManager;
    private AudioVolumeContentObserver mAudioVolumeContentObserver;

    public AudioVolumeObserver(@NonNull Context context) {
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void register(int audioStreamType,
                         @NonNull OnAudioVolumeChangedListener listener) {

        Handler handler = new Handler();

        mAudioVolumeContentObserver = new AudioVolumeContentObserver(
                handler,
                mAudioManager,
                audioStreamType,
                listener);

        mContext.getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI,
                true,
                mAudioVolumeContentObserver);
    }

    public void unregister() {
        if (mAudioVolumeContentObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mAudioVolumeContentObserver);
            mAudioVolumeContentObserver = null;
        }
    }
}

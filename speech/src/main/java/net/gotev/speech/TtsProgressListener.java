package net.gotev.speech;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.UtteranceProgressListener;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * @author Kristiyan Petrov (kristiyan@igenius.net)
 */

public class TtsProgressListener extends UtteranceProgressListener {

    private final Map<String, TextToSpeechCallback> mTtsCallbacks;
    private final WeakReference<Context> contextWeakReference;

    public TtsProgressListener(final Context context, final Map<String, TextToSpeechCallback> mTtsCallbacks) {
        contextWeakReference = new WeakReference<>(context);
        this.mTtsCallbacks = mTtsCallbacks;
    }

    @Override
    public void onStart(final String utteranceId) {
        final TextToSpeechCallback callback = mTtsCallbacks.get(utteranceId);
        final Context context = contextWeakReference.get();

        if (callback != null && context != null) {
            new Handler(context.getMainLooper()).post(() -> callback.onStart());
        }
    }

    @Override
    public void onDone(final String utteranceId) {
        final TextToSpeechCallback callback = mTtsCallbacks.get(utteranceId);
        final Context context = contextWeakReference.get();
        if (callback != null && context != null) {
            new Handler(context.getMainLooper()).post(() -> {
                callback.onCompleted();
                mTtsCallbacks.remove(utteranceId);
            });
        }
    }

    @Override
    public void onError(final String utteranceId) {
        final TextToSpeechCallback callback = mTtsCallbacks.get(utteranceId);
        final Context context = contextWeakReference.get();

        if (callback != null && context != null) {
            new Handler(context.getMainLooper()).post(() -> {
                callback.onError();
                mTtsCallbacks.remove(utteranceId);
            });
        }
    }
}

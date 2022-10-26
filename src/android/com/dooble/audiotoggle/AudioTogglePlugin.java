package com.dooble.audiotoggle;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import android.content.Context;
import android.media.AudioManager;

public class AudioTogglePlugin extends CordovaPlugin {
	public static final String ACTION_SET_AUDIO_MODE = "setAudioMode";
    public static final String TAG = "AudioTogglePlugin";

	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equals(ACTION_SET_AUDIO_MODE)) {
			if (!setAudioMode(args.getString(0))) {
				callbackContext.error("Invalid audio mode");
				return false;
			}

			return true;
		}

		callbackContext.error("Invalid action");
		return false;
	}

	public boolean setAudioMode(String mode) {
	    Context context = webView.getContext();
	    AudioManager audioManager =
	    	(AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        Log.v(TAG, "Change audio mode to: " + mode);
        if (mode.equals("earpiece")) {
            // workaround for issue https://issuetracker.google.com/issues/204914497
            Integer currentMode = audioManager.getMode();
            if (currentMode.equals(AudioManager.MODE_IN_COMMUNICATION)) {
                audioManager.setMode(AudioManager.MODE_NORMAL);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            }
            audioManager.setSpeakerphoneOn(false);
            return true;
        } else if (mode.equals("speaker")) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            return true;
        } else if (mode.equals("ringtone")) {
            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(false);
            return true;
        } else if (mode.equals("normal")) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(false);
            return true;
        }
	    return false;
	}
}

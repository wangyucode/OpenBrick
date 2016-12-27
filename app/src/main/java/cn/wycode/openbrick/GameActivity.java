package cn.wycode.openbrick;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;


public class GameActivity extends Activity {
	private PowerManager.WakeLock wakeLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new GameView(this));
		acquireWakeLock();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseWakeLock();
	}

	private void acquireWakeLock() {
		if (wakeLock ==null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, this.getClass().getCanonicalName());
			wakeLock.acquire();
		}

	}


	private void releaseWakeLock() {
		if (wakeLock !=null&& wakeLock.isHeld()) {
			wakeLock.release();
			wakeLock =null;
		}

	}

}

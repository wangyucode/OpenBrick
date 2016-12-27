package cn.wycode.openbrick;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "GameView";

	private GameThread mGameThread;

	public GameView(Context context) {
		super(context);
		init();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		Log.d(TAG, "surfaceCreated");
		mGameThread = new GameThread(holder, new GameHandler(getContext()));

		mGameThread.start();
	}




	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mGameThread.isGamePause = true;

		Log.d(TAG, "surfaceDestroyed");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d(TAG, "p->" + event.getX());
		mGameThread.setBat(event.getX());
		return super.onTouchEvent(event);

	}

	@Override
	protected Parcelable onSaveInstanceState() {

		return super.onSaveInstanceState();
	}

}

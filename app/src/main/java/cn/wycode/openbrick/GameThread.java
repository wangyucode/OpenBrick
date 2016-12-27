package cn.wycode.openbrick;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private static final String TAG = "GameThread";

	private SurfaceHolder holder;

	private Paint ballPaint;
	private Paint batPaint;
	private Paint brickPaint;
	private Paint textPaint;

	private int width, height;

	private Ball mBall;
	private Bat mBat;
	private ArrayList<Brick> mBricks;

	long lastDrawTime;

	private GameHandler mHandler;
	public boolean isGameOver;
	public boolean isGamePause;

	private int offsetToBottom = 20;

	private int brickNum = 200;
	private int offset2Border = 10;
	private int spaceBrick = 5;
	private int brickNumRow = 20;

	private int score = 0;

	public GameThread(SurfaceHolder holder, GameHandler handler) {
		this.holder = holder;
		mHandler = handler;

		ballPaint = new Paint();
		ballPaint.setStyle(Style.FILL);
		ballPaint.setColor(Color.YELLOW);

		batPaint = new Paint();
		batPaint.setColor(Color.WHITE);
		batPaint.setStrokeWidth(5);

		brickPaint = new Paint();
		brickPaint.setColor(Color.GREEN);
		brickPaint.setStyle(Style.FILL);

		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(50);

		width = holder.getSurfaceFrame().right;
		height = holder.getSurfaceFrame().bottom;

		mBall = new Ball();
		mBall.y = height - offsetToBottom;
		mBat = new Bat();
		mBricks = getBricks();

		lastDrawTime = System.nanoTime();
	}

	private ArrayList<Brick> getBricks() {
		ArrayList<Brick> bricks = new ArrayList<>();

		int rectWidth = (width - 2 * offset2Border - (brickNumRow - 1)
				* spaceBrick)
				/ brickNumRow;
		int rectHeight = 30;

		for (int i = 0; i < brickNum; i++) {
			int l = offset2Border + i % brickNumRow * spaceBrick + i
					% brickNumRow * rectWidth;
			int t = offset2Border + i / 20 * spaceBrick + i / 20 * rectHeight;
			int r = l + rectWidth;
			int b = t + rectHeight;
			Rect rect = new Rect(l, t, r, b);

			bricks.add(new Brick(rect));
		}

		return bricks;
	}

	@Override
	public void run() {
		while (!isGameOver&&!isGamePause) {
			Canvas canvas = holder.lockCanvas();
			canvas.drawColor(Color.BLACK);

			long dTime = System.nanoTime() - lastDrawTime;
			lastDrawTime = System.nanoTime();

			setBall(dTime);

			checkHitBrick();

			drawBrick(canvas);

			drawBall(canvas);

			drawBat(canvas);

			drawScore(canvas);

			holder.unlockCanvasAndPost(canvas);

		}
	}

	private void drawScore(Canvas canvas) {
		canvas.drawText("Score:" + score, 50, height - 50, textPaint);
	}

	private void checkHitBrick() {
		for (int i = 0; i < mBricks.size(); i++) {
			Rect rect = mBricks.get(i).rect;
			if (rect.contains(mBall.x, mBall.y)) {
				if (mBall.xSpeed > 0 && mBall.ySpeed > 0) {
					if (mBall.x - rect.left > mBall.y - rect.top) {
						mBall.ySpeed = -mBall.ySpeed;
						mBall.y = mBall.y - 2 * (mBall.y - rect.top);
					} else {
						mBall.xSpeed = -mBall.xSpeed;
						mBall.x = mBall.x - 2 * (mBall.x - rect.left);
					}
				} else if (mBall.xSpeed > 0 && mBall.ySpeed < 0) {
					if (mBall.x - rect.left > rect.bottom - mBall.y) {
						mBall.ySpeed = -mBall.ySpeed;
						mBall.y = mBall.y + 2 * (rect.bottom - mBall.y);
					} else {
						mBall.xSpeed = -mBall.xSpeed;
						mBall.x = mBall.x - 2 * (mBall.x - rect.left);
					}
				} else if (mBall.xSpeed < 0 && mBall.ySpeed > 0) {
					if (rect.right - mBall.x > mBall.y - rect.top) {
						mBall.ySpeed = -mBall.ySpeed;
						mBall.y = mBall.y - 2 * (mBall.y - rect.top);
					} else {
						mBall.xSpeed = -mBall.xSpeed;
						mBall.x = mBall.x + 2 * (rect.right - mBall.x);
					}
				} else {
					if (rect.right - mBall.x > rect.bottom - mBall.y) {
						mBall.ySpeed = -mBall.ySpeed;
						mBall.y = mBall.y + 2 * (rect.bottom - mBall.y);
					} else {
						mBall.xSpeed = -mBall.xSpeed;
						mBall.x = mBall.x + 2 * (rect.right - mBall.x);
					}
				}
				mBricks.remove(i);
				score += 50;
				i--;
			}
		}
	}

	private void drawBrick(Canvas canvas) {
		for (Brick b : mBricks) {
			canvas.drawRect(b.rect, brickPaint);
		}
	}

	private void drawBat(Canvas canvas) {
		canvas.drawLine(mBat.x - mBat.length / 2, height - offsetToBottom,
				mBat.x + mBat.length / 2, height - offsetToBottom, batPaint);
	}

	public void setBat(float x) {
		mBat.x = (int) x;
		if (mBat.x < mBat.length / 2) {
			mBat.x = mBat.length / 2;
		} else if (mBat.x > width - mBat.length / 2) {
			mBat.x = width - mBat.length / 2;
		}
	}

	private void drawBall(Canvas canvas) {
		canvas.drawCircle(mBall.x, mBall.y, mBall.radius, ballPaint);
		Log.d(TAG, mBall.x + "," + mBall.y);
	}

	private void setBall(long dTime) {
		mBall.x += mBall.xSpeed * dTime / 100000000;
		mBall.y += mBall.ySpeed * dTime / 100000000;

		if (mBall.x > width) {
			mBall.x = width - (mBall.x - width);
			mBall.xSpeed = -mBall.xSpeed;
		}

		if (mBall.x < 0) {
			mBall.x = -mBall.x;
			mBall.xSpeed = -mBall.xSpeed;
		}
		// hit the ball
		if (mBall.y > height - offsetToBottom) {

			if (mBall.x > mBat.x - mBat.length / 2
					&& mBall.x < mBat.x + mBat.length / 2) {
				mBall.y = height - offsetToBottom
						- (mBall.y - (height - offsetToBottom));

				double offset = (mBall.x - mBat.x) / mBat.length;

				mBall.xSpeed += offset * mBall.xSpeed;

				mBall.ySpeed = -mBall.ySpeed;
			}
		}

		if (mBall.y > height) {

			isGameOver = true;
			mHandler.sendEmptyMessage(0);
		}

		if (mBall.y < 0) {
			mBall.y = -mBall.y;
			mBall.ySpeed = -mBall.ySpeed;
		}
	}

}

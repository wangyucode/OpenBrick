package cn.wycode.openbrick;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class GameHandler extends Handler {
	
	public static final int MSG_GAME_OVER = 100;
	
	private Context mContext;
	
	public GameHandler(Context context){
		mContext = context;
	}

	@Override
	public void handleMessage(Message msg) {
		Toast.makeText(mContext, "Game Over!", Toast.LENGTH_LONG).show();
		super.handleMessage(msg);
	}
}

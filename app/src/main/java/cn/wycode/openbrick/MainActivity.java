package cn.wycode.openbrick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.start:
			intent.setClass(this, GameActivity.class);
			startActivity(intent);
			break;

		}
	}
}

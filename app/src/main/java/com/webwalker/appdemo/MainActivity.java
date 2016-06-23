package com.webwalker.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.smtt.sdk.QbSdk;
import com.webwalker.appdemo.web.FirstLoadingX5Service;
import com.webwalker.appdemo.web.X5Activity;

public class MainActivity extends BaseActivity {
	public static long startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!QbSdk.isTbsCoreInited()) {
			Intent intent = new Intent(this, FirstLoadingX5Service.class);
			startService(intent);
		}

		Button button = (Button) findViewById(R.id.button);
		final EditText etUrl = (EditText) findViewById(R.id.etUrl);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startTime = System.currentTimeMillis();
				openWeb();
			}
		});
		openWeb();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void openWeb() {
		Intent intent = new Intent(MainActivity.this, X5Activity.class);
		startActivity(intent);
		finish();
	}
}

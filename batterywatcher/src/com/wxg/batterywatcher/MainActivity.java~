package com.wxg.batterywatcher;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import com.wxg.service.WatchService;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	private BroadcastReceiver mReceiver = null;
	private TextView tvView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvView = (TextView)findViewById(R.id.tv);
		mReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		
		registerReceiver(mReceiver, intentFilter);
		
		Intent serIntent = new Intent();
		serIntent.setClass(MainActivity.this, WatchService.class);
		startService(serIntent);
	}
	
	class MyBroadcastReceiver extends BroadcastReceiver{
		    public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
					tvView.setText("voltage = "+intent.getIntExtra("voltage", 0)+"   level = "+intent.getIntExtra("level", 0));
			}
	    }
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
}

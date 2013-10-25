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
	 
	/**
	 * 在android的机子上执行linux下终端命令
	 * @param cmds
	 * @throws Exception
	 */
	public void runAsRoot(String[] cmds) throws Exception {
	    Process p = Runtime.getRuntime().exec("su");
	    DataOutputStream os = new DataOutputStream(p.getOutputStream());
	    InputStream is = p.getInputStream();
	    for (String tmpCmd : cmds) {
	        os.writeBytes(tmpCmd+"\n");
	        int readed = 0;
	        byte[] buff = new byte[4096];

	        // if cmd requires an output
	        // due to the blocking behaviour of read(...)
	        boolean cmdRequiresAnOutput = true;
	        if (cmdRequiresAnOutput) {
	            while( is.available() <= 0) {
	                try { Thread.sleep(200); } catch(Exception ex) {}
	            }

	            while( is.available() > 0) {
	                readed = is.read(buff);
	                if ( readed <= 0 ) break;
	                String seg = new String(buff,0,readed);
	                System.out.println("#> "+seg);
	            }
	        }
	    }        
	    os.writeBytes("exit\n");
	    os.flush();
	}
	
	
	/**
	 * android终端下的命令为：
	 *   adb shell
	 *   # /system/bin/screencap -p /sdcard/myscreenshot.png
	 *      
	 *      
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public void screenShot(String path) throws IOException, InterruptedException{
		Process sh = Runtime.getRuntime().exec("su", null,null);
        OutputStream os = sh.getOutputStream();
		os.write(("/system/bin/screencap -p " + path).getBytes("ASCII"));
		os.flush();
        os.close();
        sh.waitFor();
	}
	
	
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

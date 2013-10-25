package com.wxg.service;


import com.wxg.batterywatcher.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;

public class WatchService extends Service {

	private NotificationManager m_NotificationManager;
	private PendingIntent m_PendingIntent;
	private Notification  m_Notification;
	private BroadcastReceiver mReceiver = null;
	private int level ;
	@Override
	public void onCreate() {
		super.onCreate();
		
		m_NotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		mReceiver = new MyBroadcastReceiver(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		
		registerReceiver(mReceiver, intentFilter);
	}
	
	class MyBroadcastReceiver extends BroadcastReceiver{
		
		public MyBroadcastReceiver(Context context) {

		}
	    public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				
//				int m_level = intent.getIntExtra("level", 0);
				int m_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
//				System.out.println("level = "+m_level);
				setLevel(m_level);

				if (getLevel()<=20) {
					String rsName = "battery_digit_"+getLevel();
					 int icon = WatchService.this.getResources().getIdentifier(rsName, "drawable", getPackageName());
					 showNotification(icon);
				}
				else {
					String rsName = "battery_digit_blue_"+getLevel();
					int icon = WatchService.this.getResources().getIdentifier(rsName, "drawable", getPackageName());
					showNotification(icon);
				}
			}
	    }
	};

	 private void showNotification(int icon){ 
		 Intent newIntent = new Intent();
		 newIntent.setClass(this, MainActivity.class);
		 
//		 Intent intent =  new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		 
		  m_PendingIntent = PendingIntent.getActivity(WatchService.this, 0, newIntent, 0); //如果转移内容则用m_Intent();
		  //构造Notification对象 
		  m_Notification = new Notification(); 
		  //设置通知在状态栏显示的图标 
		  m_Notification.icon = icon;
		  
		  //当我们点击通知时显示的内容 
//			  m_Notification.when = System.currentTimeMillis();
		  
		  //通知时发出默认的声音 
//			  m_Notification.defaults = Notification.DEFAULT_SOUND; 
		  
		  m_Notification.setLatestEventInfo(WatchService.this, "当前电量", "just for 文雯", m_PendingIntent); 
		  
		  startForeground(1, m_Notification);
	 }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(mReceiver);
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
}


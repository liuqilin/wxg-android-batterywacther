package com.wxg.startup;

import com.wxg.service.WatchService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(arg0,WatchService.class);
		
		arg0.startService(intent);
		System.out.println("sodproject    开机启动、、、、、、");
	}
}

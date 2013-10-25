package com.wxg.widget;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.wxg.batterywatcher.R;

import android.R.bool;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.RemoteViews;
import android.widget.Toast;

public class SettingWidget extends AppWidgetProvider {
	
	 private static final String WIFI_CLICK_ACTION = "com.wxg.action.widget_wifi.click";
	 private static final String AIRPLAN_CLICK_ACTION = "com.wxg.action.widget_airplan.click";
	 private static final String BLUETOOTH_CLICK_ACTION = "com.wxg.action.widget_bluetooth.click";
	 private static final String G3_CLICK_ACTION = "com.wxg.action.widget_g3.click";
	 
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		updateWidgetDone(context);
		
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		
	}
	public static void updateWidgetDone(Context context) {
	    AppWidgetManager manager = AppWidgetManager.getInstance(context);
	    ComponentName comp = new ComponentName(context, SettingWidget.class);
	    RemoteViews views = new RemoteViews(context.getPackageName(),
	                                        R.layout.widget_layout);
	    
	    Intent wifiIntent = new Intent(WIFI_CLICK_ACTION);
	    PendingIntent wifiPendingIntent = PendingIntent.getBroadcast(context, 0, wifiIntent, 0);
	    
	    Intent airplanIntent = new Intent(AIRPLAN_CLICK_ACTION);
	    PendingIntent airplanPendingIntent = PendingIntent.getBroadcast(context, 0, airplanIntent, 0);
	    
	    Intent g3_Intent = new Intent(G3_CLICK_ACTION);
	    PendingIntent g3_PendingIntent = PendingIntent.getBroadcast(context, 0, g3_Intent, 0);
	    
	    Intent bluetoothIntent = new Intent(BLUETOOTH_CLICK_ACTION);
	    PendingIntent bluetoothPendingIntent = PendingIntent.getBroadcast(context, 0, bluetoothIntent, 0);
	    
	    views.setOnClickPendingIntent(R.id.wifi_btn, wifiPendingIntent);
	    views.setOnClickPendingIntent(R.id.airplan_btn, airplanPendingIntent);
	    views.setOnClickPendingIntent(R.id.g3_btn, g3_PendingIntent);
	    views.setOnClickPendingIntent(R.id.bluetooth_btn, bluetoothPendingIntent);
	    
	    updateWifiState(context, views);
	    updateG3(context, views);
	    updateAirplane(context, views);
	    updateBluetooth(context, views);
	    
	    manager.updateAppWidget(comp, views);
	  }
	
	public static void updateWifiState(Context context,RemoteViews remoteViews){
		int state = -1;
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		state = wifiManager.getWifiState();
		if (state==WifiManager.WIFI_STATE_DISABLED) {
			remoteViews.setInt(R.id.wifi_btn, "setImageResource",
	    		        R.drawable.settings_wifi_off);
		}
	    else if (state==WifiManager.WIFI_STATE_ENABLED) {
	    	remoteViews.setInt(R.id.wifi_btn, "setImageResource",
	    		        R.drawable.settings_app_wifi_on);
		}
	}
	
	public static void updateAirplane(Context context, RemoteViews remoteViews) {
		boolean isEnabled = Settings.System.getInt(
				context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,
				0) == 1;
		if (isEnabled) {
//			Settings.System.putInt(context.getContentResolver(),
//					Settings.System.AIRPLANE_MODE_ON, 0);
			remoteViews.setInt(R.id.airplan_btn, "setImageResource", R.drawable.settings_app_airplan_on);
		} else if (!isEnabled) {
//			Settings.System.putInt(context.getContentResolver(),
//					Settings.System.AIRPLANE_MODE_ON, 1);
			remoteViews.setInt(R.id.airplan_btn, "setImageResource", R.drawable.settings_airplan_off);
		}

//		Intent i = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//		i.putExtra("state", !isEnabled);
//		context.sendBroadcast(i);
	}
	
	private static void toggleMobileData(Context context, boolean enabled) {
	     ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

	     Class<?> conMgrClass = null; // ConnectivityManager类
	     Field iConMgrField = null; // ConnectivityManager类中的字段
	     Object iConMgr = null; // IConnectivityManager类的引用
	     Class<?> iConMgrClass = null; // IConnectivityManager类
	     Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法

	     try {
	      // 取得ConnectivityManager类
	      conMgrClass = Class.forName(conMgr.getClass().getName());
	      // 取得ConnectivityManager类中的对象mService
	      iConMgrField = conMgrClass.getDeclaredField("mService");
	      // 设置mService可访问
	      iConMgrField.setAccessible(true);
	      // 取得mService的实例化类IConnectivityManager
	      iConMgr = iConMgrField.get(conMgr);
	      // 取得IConnectivityManager类
	      iConMgrClass = Class.forName(iConMgr.getClass().getName());
	      // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
	      setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	      // 设置setMobileDataEnabled方法可访问
	      setMobileDataEnabledMethod.setAccessible(true);
	      // 调用setMobileDataEnabled方法
	      setMobileDataEnabledMethod.invoke(iConMgr, enabled);
	     } catch (ClassNotFoundException e) {
	      e.printStackTrace();
	     } catch (NoSuchFieldException e) {
	      e.printStackTrace();
	     } catch (SecurityException e) {
	      e.printStackTrace();
	     } catch (NoSuchMethodException e) {
	      e.printStackTrace();
	     } catch (IllegalArgumentException e) {
	      e.printStackTrace();
	     } catch (IllegalAccessException e) {
	      e.printStackTrace();
	     } catch (InvocationTargetException e) {
	      e.printStackTrace();
	     }
	    }  
	
	public static void updateG3(Context context ,RemoteViews remoteViews){
		boolean state = getMobileDataStatus(context);
		if (state) {
			remoteViews.setInt(R.id.g3_btn, "setImageResource", R.drawable.settings_app_mobile_on);
		} else if (!state) {
			remoteViews.setInt(R.id.g3_btn, "setImageResource", R.drawable.settings_mobile_off);
		}
		
	}
	

private static boolean getMobileDataStatus(Context context) {
		boolean state = false ; 
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
         Class<?> conMgrClass = null; // ConnectivityManager类
         Field iConMgrField = null; // ConnectivityManager类中的字段
         Object iConMgr = null; // IConnectivityManager类的引用
         Class<?> iConMgrClass = null; // IConnectivityManager类
         Method getMobileDataEnabledMethod = null; // setMobileDataEnabled方法
         
         try {
             // 取得ConnectivityManager类
             conMgrClass = Class.forName(conMgr.getClass().getName());
             // 取得ConnectivityManager类中的对象mService
             iConMgrField = conMgrClass.getDeclaredField("mService");
             // 设置mService可访问
             iConMgrField.setAccessible(true);
             // 取得mService的实例化类IConnectivityManager
             iConMgr = iConMgrField.get(conMgr);
             // 取得IConnectivityManager类
             iConMgrClass = Class.forName(iConMgr.getClass().getName());
             // 取得IConnectivityManager类中的getMobileDataEnabled(boolean)方法
             getMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("getMobileDataEnabled");
             // 设置getMobileDataEnabled方法可访问
             getMobileDataEnabledMethod.setAccessible(true);
             // 调用getMobileDataEnabled方法
             state = (Boolean) getMobileDataEnabledMethod.invoke(iConMgr);
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
            } catch (NoSuchFieldException e) {
             e.printStackTrace();
            } catch (SecurityException e) {
             e.printStackTrace();
            } catch (NoSuchMethodException e) {
             e.printStackTrace();
            } catch (IllegalArgumentException e) {
             e.printStackTrace();
            } catch (IllegalAccessException e) {
             e.printStackTrace();
            } catch (InvocationTargetException e) {
             e.printStackTrace();
            }
          return state;
    }
	
	/**
	 * 打开关闭蓝牙
	 * 
	 */
	public static void updateBluetooth(Context context, RemoteViews remoteViews) {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if (adapter.isEnabled()) {
//			adapter.disable();// 关闭蓝牙
			remoteViews.setInt(R.id.bluetooth_btn, "setImageResource", R.drawable.settings_app_bluetooth_on);
			
		} else {
			remoteViews.setInt(R.id.bluetooth_btn, "setImageResource", R.drawable.settings_bluetooth_off);
//			adapter.enable();// 打开蓝牙
		}
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		
		 AppWidgetManager manager = AppWidgetManager.getInstance(context);
		 ComponentName comp = new ComponentName(context, SettingWidget.class);
		 RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
		
		 WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			
		String action = intent.getAction();

		if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			int state = wifiManager.getWifiState();
			if (state==WifiManager.WIFI_STATE_ENABLED) {
				views.setInt(R.id.wifi_btn, "setImageResource",
	    		        R.drawable.settings_app_wifi_on);
			}
			else {
				views.setInt(R.id.wifi_btn, "setImageResource",
		    		        R.drawable.settings_wifi_off);
			}
		}
		else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			boolean state = getMobileDataStatus(context);
			if (state) {
				views.setInt(R.id.g3_btn, "setImageResource", R.drawable.settings_app_mobile_on);
			} else if (!state) {
				views.setInt(R.id.g3_btn, "setImageResource", R.drawable.settings_mobile_off);
			}
		}
		else if (action.equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
//			boolean isEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,0) == 1;
			boolean isEnabled = intent.getBooleanExtra("state", false);
			
			if (isEnabled) {
				views.setInt(R.id.airplan_btn, "setImageResource", R.drawable.settings_app_airplan_on);
			} else if (!isEnabled) {
				views.setInt(R.id.airplan_btn, "setImageResource", R.drawable.settings_airplan_off);
			}
		}
		else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			boolean isEnabled = adapter.isEnabled();
//			boolean isEnabled = intent.getBooleanExtra("state", false);
			if (isEnabled) {
				views.setInt(R.id.bluetooth_btn, "setImageResource", R.drawable.settings_app_bluetooth_on);
			} else {
				views.setInt(R.id.bluetooth_btn, "setImageResource", R.drawable.settings_bluetooth_off);
			}
		}
		
		if (action.equals(WIFI_CLICK_ACTION)) {
			int state = wifiManager.getWifiState();
			if (state==WifiManager.WIFI_STATE_ENABLED) {
				wifiManager.setWifiEnabled(false);
				Toast.makeText(context, "关闭 wifi", 2000).show();
			}
			else {
				wifiManager.setWifiEnabled(true);
				Toast.makeText(context, "开启 wifi", 6000).show();
			}
		}
		else if (action.equals(G3_CLICK_ACTION)) {
			boolean state = getMobileDataStatus(context);
			if (state) {
				toggleMobileData(context, false);
				Toast.makeText(context, "关闭移动网络", 2000).show();
			}
			else {
				toggleMobileData(context, true);
				Toast.makeText(context, "开启移动网络", 2000).show();
			}
		}
		else if (action.equals(AIRPLAN_CLICK_ACTION)) {
			boolean isEnabled = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON,0) == 1;
			if (isEnabled) {
				Settings.System.putInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0);
				Toast.makeText(context, "关闭飞行模式", 2000).show();
			} else if (!isEnabled) {
				Settings.System.putInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 1);
				Toast.makeText(context, "开启飞行模式", 2000).show();
			}
			
			Intent airplan = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
			airplan.putExtra("state", !isEnabled);
			context.sendBroadcast(airplan);
		}
		else if (action.equals(BLUETOOTH_CLICK_ACTION)) {
			BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
			boolean isEnabled = adapter.isEnabled();
			if (isEnabled) {
				adapter.disable();// 关闭蓝牙
				Toast.makeText(context, "关闭蓝牙", 2000).show();
			} else {
				adapter.enable();// 打开蓝牙
				Toast.makeText(context, "开启蓝牙", 2000).show();
			}
		}
		manager.updateAppWidget(comp, views);
		
	}
}

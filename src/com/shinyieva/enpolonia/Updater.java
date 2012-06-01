package com.shinyieva.enpolonia;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.shinyieva.enpolonia.sdl.EntryService;
import com.shinyieva.enpolonia.settings.AppSettings;
import com.shinyieva.enpolonia.utils.LocalBinder;

public class Updater extends Service {
	public static Binder mBinder;
	private final String Tag = "EnPolonia";
	private final int INTERVAL = 60000;

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private BroadcastReceiver connChangeReceiver;
	private Message msg;
	private Timer timer;
	Intent intent;

	private EntryService service;

	int counter = 0;

	private NotificationManager mNM;
	private EnPoloniaApp app;

	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private final int NOTIFICATION = R.string.local_service_started;

	@Override
	public void onCreate() {
		Log.i(this.Tag, "onCreate[Updater]");
		this.app = (EnPoloniaApp) this.getApplication();
		this.mNM = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);

		this.init();
		// this.startTimer();

	}

	public void init() {
		Log.i(this.Tag, "init[Updater]");
		HandlerThread thread = new HandlerThread("ServiceUserData",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		if (this.mServiceHandler != null) {
			this.mServiceHandler = null;
		}
		this.mServiceLooper = thread.getLooper();
		this.mServiceHandler = new ServiceHandler(this.mServiceLooper);
		this.app.setLocalService(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		this.msg = this.mServiceHandler.obtainMessage();
		this.msg.arg1 = startId;
		if (this.timer == null) {
			this.timer = new Timer();
		}
		if ((intent != null) && intent.hasExtra("forceTimer")
				&& intent.getExtras().getBoolean("forceTimer")
				&& this.app.getUpdateOffset() > 0) {
			this.startTimer();
		}

		return START_STICKY;
	}

	private void startTimer() {
		Log.d(this.Tag,
				"startTimer with IntervalOffset = "
						+ String.valueOf(this.app.getUpdateOffset()) + "min");
		if (this.timer != null) {
			this.timer.cancel();
		}
		this.timer = new Timer();
		if (this.mServiceHandler == null) {
			this.mServiceHandler = new ServiceHandler(this.mServiceLooper);
			this.app.setLocalService(this);
		}
		this.timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ConnectivityManager conMgr = (ConnectivityManager) Updater.this
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
				if (Updater.this.mServiceHandler != null
						&& activeNetwork.isConnectedOrConnecting()) {
					Updater.this.mServiceHandler.sendEmptyMessage(69);
				}
			}
		}, 0, this.INTERVAL * this.app.getUpdateOffset());
	}

	private void stopTimer() {
		if (this.timer != null) {
			Log.i(this.Tag, "Stopping timer...");
			this.timer.cancel();
			this.timer.purge();
			this.timer = null;
			this.counter = 0;
		}
	}

	public void reset() {
		this.stopTimer();
		this.init();
		if (this.app.getUpdateOffset() > 0) {
			this.startTimer();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (mBinder != null) {
			mBinder = new LocalBinder<Updater>(this);
		}
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(this.Tag, "onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		this.mNM.cancel(this.NOTIFICATION);
		Log.i(this.Tag, "onDestroy");
		try {
			this.unregisterReceiver(this.connChangeReceiver);
		} catch (Exception e) {
		}
		this.stopTimer();
		this.stopSelf();
		super.onDestroy();
		this.mServiceHandler = null;
		this.intent = null;
		// Tell the user we stopped.
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		String text = this.getString(R.string.new_entries);
		text = text
				.replace("%d", String.valueOf(this.app.getNewEntriesCount()));

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.app_icon, text,
				System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification

		Intent fromNot = new Intent(this, MainActivity.class);

		fromNot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				fromNot, 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this, this.getText(R.string.app_name),
				text, contentIntent);

		// Send the notification.
		this.mNM.cancel(this.NOTIFICATION);
		this.mNM.notify(this.NOTIFICATION, notification);
	}

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			Updater.this.service = new EntryService(Updater.this.app);
			Updater.this.service.getEntries(AppSettings.URL_BASE, true, false);

			if (Updater.this.app.getNewEntriesCount() > 0) {
				Updater.this.showNotification();
			}
		}
	}
}

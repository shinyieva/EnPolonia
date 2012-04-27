package com.shinyieva.enpolonia.utils.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.shinyieva.enpolonia.EnPoloniaApp;
import com.shinyieva.enpolonia.R;
import com.shinyieva.enpolonia.Updater;
import com.shinyieva.enpolonia.sdl.data.VisibilityMode;
import com.shinyieva.enpolonia.settings.AppSettings;

public class AbstractActivity extends Activity {

	private onVisibilityModeChange _Listener;
	protected EnPoloniaApp app;
	protected Intent mServiceIntent;

	// Menu delegate Methods
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		this.app = (EnPoloniaApp) this.getApplication();
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.my_tools).setVisible(true);
		menu.findItem(R.id.search).setVisible(true);

		if (AppSettings.visibilityMode == VisibilityMode.All) {
			menu.findItem(R.id.visibility).setTitle("No le’dos");
		} else {
			menu.findItem(R.id.visibility).setTitle("Ver Todos");
		}
		menu.findItem(R.id.visibility).setVisible(true);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle item selection
		switch (item.getItemId()) {

		case R.id.visibility:
			VisibilityMode aux = VisibilityMode.All;
			if (item.getTitle().toString().equalsIgnoreCase("No le’dos")) {
				aux = VisibilityMode.UnreadOnly;
			} else {
				aux = VisibilityMode.All;
			}
			this._Listener.onVisibilityChanged(aux);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public interface onVisibilityModeChange {
		public void onVisibilityChanged(VisibilityMode mode);
	}

	protected void initService(boolean p_launchTimer) {
		Log.i("EnPolonia", "initService");
		if (this.app.getLocalService() != null) {
			this.app.getLocalService().onDestroy();
		}
		if (this.mServiceIntent == null) {
			this.mServiceIntent = new Intent(this, Updater.class);
			this.mServiceIntent.putExtra("forceTimer", p_launchTimer);
			this.startService(this.mServiceIntent);
		}
	}
}

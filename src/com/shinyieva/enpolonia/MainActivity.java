package com.shinyieva.enpolonia;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import com.shinyieva.enpolonia.net.cache.Cache;
import com.shinyieva.enpolonia.net.cache.CacheSQLite;
import com.shinyieva.enpolonia.sdl.EntryService;
import com.shinyieva.enpolonia.sdl.data.Entry;
import com.shinyieva.enpolonia.settings.AppSettings;
import com.shinyieva.enpolonia.ui.EntryList;
import com.shinyieva.enpolonia.utils.ui.AbstractActivity;

public class MainActivity extends AbstractActivity {

	Updater s;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		this.Tag = this.getClass().getName();

		if (this.app.getLocalService() != null) {
			this.app.getLocalService().onDestroy();
		}

		// int versionCode = 2;
		// try {
		// versionCode = this.getPackageManager().getPackageInfo(
		// this.getPackageName(), 0).versionCode;
		// } catch (NameNotFoundException e) {
		// Log.e("EnPolonia", "[MainActivity]Error al obtener versionCode. "
		// + e.getLocalizedMessage());
		// }

		// INICIALIZACION DE CACHE
		try {
			CacheSQLite.Init(
					this.app,
					this.getPackageManager().getPackageInfo(
							this.getPackageName(), 0).versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e(this.Tag,
					"Hubo algœn problema al intntar inicializar CacheSQLite");
		}

		// OBTENCION DE DATOS DE LA CACHE
		ArrayList<Cache> data = CacheSQLite.Current().GetAll();
		EntryService entryS = new EntryService(
				(EnPoloniaApp) this.getApplication());
		if (data != null) {
			this.app.setEntries((ArrayList<Entry>) entryS.getEntries(data));
		}

		this.initService(true);

		// ARRANQUE DE LA PANTALLA PRINCIPAL
		Intent intent = new Intent(this, EntryList.class);
		intent.putExtra("URL", AppSettings.URL_BASE);
		this.startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Si no se pone al dar a back en el listado rearranca
		this.finish();
	}

}
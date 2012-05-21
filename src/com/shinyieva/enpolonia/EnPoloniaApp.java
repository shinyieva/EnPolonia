package com.shinyieva.enpolonia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.shinyieva.enpolonia.net.cache.Cache;
import com.shinyieva.enpolonia.net.cache.CacheSQLite;
import com.shinyieva.enpolonia.sdl.EntryService;
import com.shinyieva.enpolonia.sdl.data.Entry;
import com.shinyieva.enpolonia.sdl.data.VisibilityMode;

public class EnPoloniaApp extends Application {

	private ConcurrentHashMap<String, Entry> _EntriesMap;
	private Updater localService;
	private int newEntriesCount;
	private final String Tag = "EnPoloniaApp";

	public boolean isOffLine() {

		return false;
	}

	public int setEntries(ArrayList<Entry> entries) {
		Log.i(this.Tag, "setEntries(ArrayList<Entry>)");

		int newEntries = 0;
		if (this._EntriesMap == null) {
			this.getEntries(VisibilityMode.All);
		}

		for (int i = 0; i < entries.size(); i++) {
			if (!this._EntriesMap.containsKey(entries.get(i).getGuid())) {
				this._EntriesMap.put(entries.get(i).getGuid(), entries.get(i));
				Entry e = entries.get(i);

				try {
					CacheSQLite.Init(
							this.localService,
							this.getPackageManager().getPackageInfo(
									this.getPackageName(), 0).versionCode);
					CacheSQLite.Current().Set(e.getGuid(), e.getTitle(),
							e.getLink(), e.getDescription(),
							String.valueOf(e.getDate().getTime()),
							e.getCreator(), e.getCommentRssUrl(),
							String.valueOf(e.getNumComments()), e.getContent(),
							String.valueOf(e.getUnread()));
				} catch (NameNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				newEntries++;
			}
		}

		return newEntries;
	}

	public ArrayList<Entry> getEntries(VisibilityMode mode) {
		Log.i(this.Tag, "getEntries(VisibilityMode)");
		if (this._EntriesMap == null || this._EntriesMap.isEmpty()) {
			this._EntriesMap = new ConcurrentHashMap<String, Entry>();
			// OBTENCION DE DATOS DE LA CACHE
			try {
				CacheSQLite.Init(this.localService, this.getPackageManager()
						.getPackageInfo(this.getPackageName(), 0).versionCode);
				ArrayList<Cache> data = CacheSQLite.Current().GetAll();
				EntryService entryS = new EntryService(this);
				if (data != null) {
					this.setEntries((ArrayList<Entry>) entryS.getEntries(data));
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ArrayList<Entry> temp = new ArrayList<Entry>();
		try {
			Iterator it = this._EntriesMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry e = (Map.Entry) it.next();
				temp.add((Entry) e.getValue());
			}

			Collections.sort(temp);

			for (int i = 0; i < temp.size(); i++) {
				if (mode == VisibilityMode.UnreadOnly
						&& temp.get(i).getUnread() == 0) {
					temp.remove(i);
				}
			}
		} catch (Exception e) {
			Log.e(this.Tag, "Algo ha ido mal en EnPoloniaApp.getEntries.");
		}

		return temp;
	}

	public boolean setUnread(String id) {
		Log.i(this.Tag, "setUnread(" + id + ")");
		if (this._EntriesMap.containsKey(id)) {
			Entry e = new Entry();
			e = this._EntriesMap.get(id);
			e.setUnread(0);
			CacheSQLite.Current().Set(e.getGuid(), e.getTitle(), e.getLink(),
					e.getDescription(), String.valueOf(e.getDate().getTime()),
					e.getCreator(), e.getCommentRssUrl(),
					String.valueOf(e.getNumComments()), e.getContent(),
					String.valueOf(e.getUnread()));
			return true;
		} else {
			return false;
		}
	}

	public void setLocalService(Updater mService) {
		this.localService = mService;
	}

	public Updater getLocalService() {
		return this.localService;
	}

	public void setNewEntriesCount(int count) {
		this.newEntriesCount = count;
	}

	public int getNewEntriesCount() {
		return this.newEntriesCount;
	}

}

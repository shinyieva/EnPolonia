package com.shinyieva.enpolonia.sdl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.shinyieva.enpolonia.EnPoloniaApp;
import com.shinyieva.enpolonia.net.cache.Cache;
import com.shinyieva.enpolonia.sdl.data.Entry;
import com.shinyieva.enpolonia.sdl.data.VisibilityMode;

public class EntryService {

	private final EnPoloniaApp _application;
	private final String Tag = "EntryService";

	public EntryService(EnPoloniaApp app) {
		this._application = app;
	}

	public boolean setUnreadEntry(String id) {
		return this._application.setUnread(id);
	}

	public List<Entry> getEntries(String url, boolean p_save, boolean p_creator) {
		Log.i(this.Tag, "getEntries( " + url + " , " + String.valueOf(p_save)
				+ " , " + String.valueOf(p_creator) + " ) ");
		List<Entry> noticias = new ArrayList<Entry>();

		noticias = this._application.getEntries(VisibilityMode.All);

		if (noticias != null && noticias.size() > 0 && !p_creator) {

			if (this.checkConnectivity()) {
				this._application.setNewEntriesCount(this.updateEntries(url,
						p_save));
			}

		} else {
			if (this.checkConnectivity()) {
				RssParserSax saxparser = new RssParserSax(url);
				noticias = saxparser.parse();
				if (p_save) {
					this._application.setEntries((ArrayList<Entry>) noticias);
				}
			}

		}

		return noticias;
	}

	public List<Entry> getEntries(ArrayList<Cache> p_entries) {
		Log.i(this.Tag, "getEntries(ArrayList<Cache>)");
		List<Entry> temp = new ArrayList<Entry>();

		for (int i = 0; i < p_entries.size(); i++) {
			temp.add(new Entry(p_entries.get(i)));
		}

		return temp;
	}

	public int updateEntries(String url, boolean p_save) {
		Log.i("EnPolonia", "updateEntries");
		List<Entry> noticias = new ArrayList<Entry>();
		int count = 0;
		RssParserSax saxparser = new RssParserSax(url);
		if (!url.equalsIgnoreCase("")) {
			noticias = saxparser.parse();
			if (p_save) {
				count = this._application
						.setEntries((ArrayList<Entry>) noticias);
			}
		}
		return count;
	}

	public boolean checkConnectivity() {
		ConnectivityManager conMgr = (ConnectivityManager) this._application
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean result = true;

		if (conMgr != null) {
			NetworkInfo i = conMgr.getActiveNetworkInfo();
			if (i != null) {
				if (!i.isConnected())
					result = false;
				if (!i.isAvailable())
					result = false;
			} else if (i == null) {
				result = false;
			}
		}

		return result;
	}
}

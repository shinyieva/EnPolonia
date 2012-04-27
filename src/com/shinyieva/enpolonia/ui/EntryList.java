package com.shinyieva.enpolonia.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.shinyieva.enpolonia.EnPoloniaApp;
import com.shinyieva.enpolonia.R;
import com.shinyieva.enpolonia.net.cache.CacheSQLite;
import com.shinyieva.enpolonia.sdl.EntryService;
import com.shinyieva.enpolonia.sdl.data.Entry;
import com.shinyieva.enpolonia.sdl.data.ListRowType;
import com.shinyieva.enpolonia.sdl.data.VisibilityMode;
import com.shinyieva.enpolonia.utils.ui.AbstractActivity;
import com.shinyieva.enpolonia.utils.ui.AbstractActivity.onVisibilityModeChange;
import com.shinyieva.enpolonia.utils.ui.IconListView;
import com.shinyieva.enpolonia.utils.ui.IconListViewRow;

public class EntryList extends AbstractActivity implements Runnable,
		onVisibilityModeChange {

	private List<Entry> _Rows;

	private EnPoloniaApp _application;
	private String _url = "";

	private ArrayList<IconListViewRow> _IconListViewRows;
	private IconListView _IconListView;
	private ListView _ListView;

	private VisibilityMode visibilityMode = VisibilityMode.All;

	private EntryService _entryS;
	private DBTask _task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_view);

		this._application = ((EnPoloniaApp) this.getApplication());

		this._ListView = (ListView) this.findViewById(R.id.ListView);
		this._ListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				IconListViewRow iconListViewRow = (IconListViewRow) parent
						.getItemAtPosition(position);
				EntryList.this._GoTo(iconListViewRow.Activity, position);
			}
		});

		this._IconListViewRows = new ArrayList<IconListViewRow>();
		this._IconListView = new IconListView(this, this,
				R.layout.list_view_row, this._IconListViewRows,
				this._application);
		this._ListView.setAdapter(this._IconListView);

		this.findViewById(R.id.loader).setVisibility(View.VISIBLE);

		this._Rows = this._application.getEntries(this.visibilityMode);

		if (this._Rows != null && this._Rows.size() > 0
				&& !this.getIntent().hasExtra("creator")) {

			this._LoadUi();
		}

		new Thread(this).start();

	}

	@Override
	protected void onResume() {

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adView);
		adView.loadAd(new AdRequest());

		if (this._Rows != null && !this.getIntent().hasExtra("creator")) {
			this._Rows = this._application.getEntries(this.visibilityMode);
			this._LoadUi();
		}

		super.onResume();
	}

	private void _GoTo(Class<?> p_activity, int p_position) {

		if (this._Rows != null && this._Rows.size() > 0
				&& this._Rows.get(p_position) != null) {
			Intent intent = new Intent(this, EntryDetail.class);
			intent.putExtra("Entry", this._Rows.get(p_position));
			this.startActivity(intent);
		}

	}

	private void _LoadUi() {
		IconListViewRow row;

		this._IconListView.clear();
		// Header

		for (int i = 0; i < this._Rows.size(); i++) {

			row = new IconListViewRow(this._Rows.get(i), ListRowType.EntryRow,
					null);
			this._IconListView.add(row);
		}

		this._IconListView.notifyDataSetChanged();
		this.findViewById(R.id.loader).setVisibility(View.GONE);
	}

	private List<Entry> _LoadData() {

		this._entryS = new EntryService(this._application);
		boolean save = true;
		boolean creator = false;
		if (this.getIntent().hasExtra("creator")) {
			save = false;
			creator = true;
		}
		return this._entryS.getEntries(this._url, save, creator);
	}

	public void run() {
		if (this.getIntent().hasExtra("URL")) {
			this._url = this.getIntent().getExtras().getString("URL");
		}

		this._Rows = new ArrayList<Entry>();
		this._Rows = this._LoadData();

		this._loadHandler.sendEmptyMessage(0);
	}

	private final Handler _loadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (EntryList.this._task != null) {
					EntryList.this._task.cancel(true);
				}
				EntryList.this._task = (DBTask) new DBTask().execute();

				EntryList.this._Rows = EntryList.this._application
						.getEntries(EntryList.this.visibilityMode);

				EntryList.this._LoadUi();
				break;
			}
		}
	};

	/*
	 * Tarea as’ncrona de actualizaci—n/almacenamiento de base de datos
	 */

	private class DBTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... arg0) {

			ArrayList<Entry> entries = EntryList.this._application
					.getEntries(VisibilityMode.All);

			if (entries != null) {
				for (int i = 0; i < entries.size(); i++) {
					CacheSQLite.Current().Set(entries.get(i).getGuid(),
							entries.get(i).getTitle(),
							entries.get(i).getLink(),
							entries.get(i).getDescription(),
							String.valueOf(entries.get(i).getDate().getTime()),
							entries.get(i).getCreator(),
							entries.get(i).getCommentRssUrl(),
							String.valueOf(entries.get(i).getNumComments()),
							entries.get(i).getContent(),
							String.valueOf(entries.get(i).getUnread()));
				}
			}
			return 0;

		}

		// protected void onProgressUpdate() {
		//
		// }

		@Override
		protected void onPostExecute(Integer action) {

		}
	}

	public void onVisibilityChanged(VisibilityMode mode) {
		this.findViewById(R.id.loader).setVisibility(View.VISIBLE);

		this.visibilityMode = mode;

		this._LoadUi();
	}

}
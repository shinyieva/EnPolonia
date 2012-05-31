package com.shinyieva.enpolonia.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinyieva.enpolonia.EnPoloniaApp;
import com.shinyieva.enpolonia.R;
import com.shinyieva.enpolonia.sdl.EntryService;
import com.shinyieva.enpolonia.sdl.data.Entry;
import com.shinyieva.enpolonia.settings.AppSettings;
import com.shinyieva.enpolonia.utils.ui.AbstractActivity;

public class EntryDetail extends AbstractActivity implements Runnable {

	private WebView _Browser;
	private Entry _Entry;

	private ImageView _Creator;
	private TextView _Title;
	private TextView _Date;
	private Button _Comments;
	private Button _Share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.entry_detail);

		this.app = ((EnPoloniaApp) this.getApplication());

		this._Creator = (ImageView) this.findViewById(R.id.detail_creator);
		this._Title = (TextView) this.findViewById(R.id.detail_title);
		this._Date = (TextView) this.findViewById(R.id.detail_date);
		this._Comments = (Button) this.findViewById(R.id.detail_comments);
		this._Share = (Button) this.findViewById(R.id.detail_share);

		this._Browser = (WebView) this.findViewById(R.id.browser);
		this._Browser.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		this._Browser.setBackgroundColor(0);

		// /* Set up the Zoom controls */
		// FrameLayout mContentView = (FrameLayout) getWindow().
		// getDecorView().findViewById(android.R.id.content);
		// final View zoom = _Browser.getZoomControls();
		// mContentView.addView(zoom, LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// zoom.setVisibility(View.VISIBLE);

		this._Browser.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view,
					String urlNewString) {
				Log.d(AppSettings.TAG_LOG, "BROWSER: shouldOverrideUrlLoading");
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(urlNewString));

				EntryDetail.this.startActivity(myIntent);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				Log.d(AppSettings.TAG_LOG, "BROWSER: onPageStarted");
				EntryDetail.this.findViewById(R.id.loader).setVisibility(
						View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				EntryDetail.this.findViewById(R.id.loader).setVisibility(
						View.GONE);
				Log.d(AppSettings.TAG_LOG, "BROWSER: onPageFinished");
				EntryService _entryS = new EntryService(EntryDetail.this.app);
				Log.d(AppSettings.TAG_LOG,
						"guid = " + EntryDetail.this._Entry.getGuid());
				_entryS.setUnreadEntry(EntryDetail.this._Entry.getGuid());
			}
		});

		new Thread(this).start();

	}

	private void _LoadUi() {

		this._Title.setText(this._Entry.getTitle());

		if (this._Entry.getCreator().trim().equalsIgnoreCase("r0dAs")) {
			this._Creator.setImageResource(R.drawable.rodas);
		} else {
			this._Creator.setImageResource(R.drawable.sob);
		}
		this._Creator.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EntryDetail.this.searchCreatorFeeds();
			}
		});

		this._Date.setText(this._Entry.getDate().toLocaleString());

		this._Share.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				EntryDetail.this.share(EntryDetail.this._Entry.getTitle(),
						EntryDetail.this._Entry.getLink());
			}
		});

		this._Comments.setText(String.valueOf(this._Entry.getNumComments()));
		this._Comments.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EntryDetail.this.showComments();
			}
		});

		Log.d(AppSettings.TAG_LOG, "html to load = " + this._Entry.getContent());
		this._Browser.loadDataWithBaseURL("fake://not/needed",
				this._Entry.getContent(), "text/html", "utf-8", "");
	}

	private Entry _LoadData() {

		if (this.getIntent().hasExtra("Entry")) {
			return this.getIntent().getParcelableExtra("Entry");

		}
		return null;

	}

	public void searchCreatorFeeds() {
		Log.d(AppSettings.TAG_LOG,
				"searchCreatorFeeds: " + this._Entry.getCreator());

		Intent intent = new Intent(this, EntryList.class);

		String url = AppSettings.URL_CREATOR_SEARCH;
		url = url.replace("{creator}", this._Entry.getCreator());
		intent.putExtra("URL", url);
		intent.putExtra("creator", "Creator");

		this.startActivity(intent);
	}

	public void showComments() {
		if (this._Entry.getNumComments() > 0) {
			Log.d(AppSettings.TAG_LOG,
					"commentsURL: " + this._Entry.getCommentRssUrl());
			// _Browser.loadUrl(_Entry.getCommentRssUrl());
		}
	}

	public void share(String subject, String text) {
		final Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);

		this.startActivity(Intent.createChooser(intent,
				this.getString(R.string.share)));
	}

	public void run() {
		this._Entry = this._LoadData();

		this._loadHandler.sendEmptyMessage(0);
	}

	private final Handler _loadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// Aqu� hemos acabado de hacer la carga. Refrescamos o lo que
				// sea que quieras hacer
				EntryDetail.this._LoadUi();
				break;
			}
		}
	};

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return false;
	}
}

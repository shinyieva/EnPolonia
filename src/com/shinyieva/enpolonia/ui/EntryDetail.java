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

public class EntryDetail extends AbstractActivity implements Runnable{

	private WebView _Browser;
	private Entry _Entry;

	private EnPoloniaApp _application;

	private ImageView _Creator;
	private TextView  _Title;
	private TextView  _Date;
	private Button    _Comments;
	private Button    _Share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry_detail);

		_application = ((EnPoloniaApp) this.getApplication());

		_Creator  = (ImageView)findViewById(R.id.detail_creator);
		_Title    = (TextView) findViewById(R.id.detail_title);
		_Date     = (TextView) findViewById(R.id.detail_date);
		_Comments = (Button)   findViewById(R.id.detail_comments);
		_Share    = (Button)   findViewById(R.id.detail_share);

		_Browser = (WebView)findViewById(R.id.browser);
		_Browser.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		_Browser.setBackgroundColor(0);

//		/* Set up the Zoom controls */
//		FrameLayout mContentView = (FrameLayout) getWindow().
//		getDecorView().findViewById(android.R.id.content);
//		final View zoom = _Browser.getZoomControls();
//		mContentView.addView(zoom, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		zoom.setVisibility(View.VISIBLE);
		
		_Browser.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
				Log.d(AppSettings.TAG_LOG, "BROWSER: shouldOverrideUrlLoading");
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlNewString));

				startActivity(myIntent);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				Log.d(AppSettings.TAG_LOG, "BROWSER: onPageStarted");
				findViewById(R.id.loader).setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				findViewById(R.id.loader).setVisibility(View.GONE);
				Log.d(AppSettings.TAG_LOG, "BROWSER: onPageFinished");
				EntryService _entryS = new EntryService(_application);
				Log.d(AppSettings.TAG_LOG, "guid = " + _Entry.getGuid());
				_entryS.setUnreadEntry(_Entry.getGuid());
			}
		});

		new Thread(this).start();

	}
	
	private void _LoadUi(){

		_Title.setText(_Entry.getTitle());

		if (_Entry.getCreator().trim().equalsIgnoreCase("r0dAs")) {
			_Creator.setImageResource(R.drawable.rodas);
		}else{
			_Creator.setImageResource(R.drawable.sob);
		}
		_Creator.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				searchCreatorFeeds();
			}
		});

		_Date.setText(_Entry.getDate().toLocaleString());

		_Share.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				share(_Entry.getTitle(), _Entry.getLink());
			}
		});

		_Comments.setText(String.valueOf(_Entry.getNumComments()));
		_Comments.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showComments();
			}
		});

		Log.d(AppSettings.TAG_LOG, "html to load = " + _Entry.getContent());
		_Browser.loadDataWithBaseURL("fake://not/needed", _Entry.getContent(), "text/html", "utf-8", "");
	}

	private Entry _LoadData(){

		if (getIntent().hasExtra("Entry")) {
			return getIntent().getParcelableExtra("Entry");

		}
		return null;

	}

	public void searchCreatorFeeds(){
		Log.d(AppSettings.TAG_LOG, "searchCreatorFeeds: " + _Entry.getCreator());

		Intent intent = new Intent(this, EntryList.class);

		String url = AppSettings.URL_CREATOR_SEARCH;
		url = url.replace("{creator}", _Entry.getCreator());
		intent.putExtra("URL", url);
		intent.putExtra("creator", "Creator");

		startActivity(intent);
	}

	public void showComments(){
		if (_Entry.getNumComments() > 0) {
			Log.d(AppSettings.TAG_LOG, "commentsURL: " + _Entry.getCommentRssUrl());
			//_Browser.loadUrl(_Entry.getCommentRssUrl());
		}
	}

	public void share(String subject,String text) {
		final Intent intent = new Intent(Intent.ACTION_SEND);

		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, text);

		startActivity(Intent.createChooser(intent, getString(R.string.share)));
	}
	public void run() {
		_Entry = _LoadData();

		_loadHandler.sendEmptyMessage(0);
	}

	private Handler _loadHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				//Aqu� hemos acabado de hacer la carga. Refrescamos o lo que sea que quieras hacer
				_LoadUi();
				break;
			}
		}
	};
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		
		return false;
	}
}

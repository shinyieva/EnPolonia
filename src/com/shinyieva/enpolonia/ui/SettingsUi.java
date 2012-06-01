package com.shinyieva.enpolonia.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.shinyieva.enpolonia.R;
import com.shinyieva.enpolonia.utils.ui.AbstractActivity;

public class SettingsUi extends AbstractActivity {

	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.list_view);

		this.list = (ListView) this.findViewById(R.id.List);

		String[] values = new String[] { this
				.getString(R.string.update_interval) };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		this.list.setAdapter(adapter);
		this.list.setTextFilterEnabled(true);
		this.list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					SettingsUi.this.chooseUpdateInterval();
				}
			}
		});

		this.findViewById(R.id.loader).setVisibility(View.GONE);
	}

	private void chooseUpdateInterval() {
		final CharSequence[] items = { this.getString(R.string.update_never),
				this.getString(R.string.update_each_5),
				this.getString(R.string.update_each_10),
				this.getString(R.string.update_each_15),
				this.getString(R.string.update_each_20),
				this.getString(R.string.update_each_25),
				this.getString(R.string.update_each_30),
				this.getString(R.string.update_each_hour) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.getString(R.string.update_interval));
		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						Toast.makeText(SettingsUi.this.getApplicationContext(),
								items[item], Toast.LENGTH_SHORT).show();
						dialog.dismiss();

						switch (item) {
						case 0:
							SettingsUi.this.app.setUpdateInterval(0);
							break;
						case 7:
							SettingsUi.this.app.setUpdateInterval(60);
							break;

						default:
							SettingsUi.this.app.setUpdateInterval(item * 5);
							break;
						}
					}
				});

		AlertDialog alert = builder.create();
		alert.show();
	}
}

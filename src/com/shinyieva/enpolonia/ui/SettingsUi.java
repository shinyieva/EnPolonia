package com.shinyieva.enpolonia.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shinyieva.enpolonia.R;

public class SettingsUi extends Activity {

	private ListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.list_view);

		this.list = (ListView) this.findViewById(R.id.List);

		String[] values = new String[] { "Intervalo de actualizaci—n" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		this.list.setAdapter(adapter);
		this.list.setTextFilterEnabled(true);
		this.list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
	}
}

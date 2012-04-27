package com.shinyieva.enpolonia.utils.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shinyieva.enpolonia.EnPoloniaApp;
import com.shinyieva.enpolonia.R;
import com.shinyieva.enpolonia.utils.image.ImageLoader;

public class IconListView extends ArrayAdapter<IconListViewRow>{

	protected ArrayList<IconListViewRow> _Items;
	protected EnPoloniaApp _application;
	protected int _CurrentPos;
	protected Activity _activity;
	protected ViewGroup _currentView; 

	protected com.shinyieva.enpolonia.utils.image.ImageLoader _imageLoader;

	public HashMap<View, ImageLoader> imageLoadsRegister = new HashMap<View, ImageLoader>();

	public IconListView(Activity p_activity, Context p_context, int p_textViewResourceId, ArrayList<IconListViewRow> p_items, EnPoloniaApp p_app) {
		super(p_context, p_textViewResourceId, p_items);
		_Items = p_items;
		_application = p_app;
		_activity = p_activity;
		_imageLoader = new com.shinyieva.enpolonia.utils.image.ImageLoader(p_activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		_currentView = parent;

		IconListViewRow iconListViewRow = _Items.get(position);
		_CurrentPos = position;

		LayoutInflater vi = LayoutInflater.from(getContext());
		if (iconListViewRow.Entry.getUnread() == 0) {
			v = vi.inflate(R.layout.list_view_row_readed, null);
		}else{
			v = vi.inflate(R.layout.list_view_row, null);
		}


		if (iconListViewRow != null) {

			switch (iconListViewRow.Type) {
			case EntryRow:

				if (iconListViewRow.Entry.getCreator().trim().equalsIgnoreCase("r0dAs")) {
					((ImageView)v.findViewById(R.id.creator_icon)).setImageResource(R.drawable.rodas);
				}else{
					((ImageView)v.findViewById(R.id.creator_icon)).setImageResource(R.drawable.sob);
				}

				((TextView)v.findViewById(R.id.row_title)).setText(iconListViewRow.Entry.getTitle());

				((TextView)v.findViewById(R.id.row_subtitle)).setText(iconListViewRow.Entry.getCreator());
				break;

			case Header:

				break;
			default:

				break;
			}

		}

		return v;
	}

}

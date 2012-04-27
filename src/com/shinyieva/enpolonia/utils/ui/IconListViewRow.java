package com.shinyieva.enpolonia.utils.ui;

import com.shinyieva.enpolonia.sdl.data.Entry;
import com.shinyieva.enpolonia.sdl.data.ListRowType;


public class IconListViewRow {
	
	public Entry  Entry; 
	public ListRowType Type;
	public Class<?> Activity;
	
	public IconListViewRow(Entry p_entry, ListRowType p_type, Class<?> p_class){
		Entry    = p_entry;
		Type     = p_type;
		Activity = p_class;
	}
}

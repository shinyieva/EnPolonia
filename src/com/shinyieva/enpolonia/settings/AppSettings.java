package com.shinyieva.enpolonia.settings;

import com.shinyieva.enpolonia.sdl.data.VisibilityMode;

public class AppSettings {

	public static String TAG_LOG = "EnPolonia";
	
	public static String URL_BASE           = "http://www.sucedioenpolonia.com/feed/";
	public static String URL_CREATOR_SEARCH = "http://www.sucedioenpolonia.com/author/{creator}/feed/";
	public static String URL_COMMENTS       = "http://www.sucedioenpolonia.com/comments/feed/";
	public static String IMAGE_PATH         = "/.images";
	
	public static VisibilityMode visibilityMode = VisibilityMode.All;
	
}

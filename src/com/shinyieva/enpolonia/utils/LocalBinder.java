package com.shinyieva.enpolonia.utils;

import java.lang.ref.WeakReference;

import android.os.Binder;

public class LocalBinder<S> extends Binder {
	private final WeakReference<S> mService;

	public LocalBinder(S service) {
		this.mService = new WeakReference<S>(service);
	}

	public S getService() {
		return this.mService.get();
	}
}

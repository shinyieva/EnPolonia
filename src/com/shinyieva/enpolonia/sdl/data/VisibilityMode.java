package com.shinyieva.enpolonia.sdl.data;

public enum VisibilityMode {
	All(0), UnreadOnly(1);
	
	private final int value;
	VisibilityMode(int val) {
		this.value = val;
	}
	public int value() {
		return this.value;
	}
	public static VisibilityMode fromInt (int val) {
		try {
			return values()[val];
		} catch (Exception e) {
			return values()[0];
		}
	}
}

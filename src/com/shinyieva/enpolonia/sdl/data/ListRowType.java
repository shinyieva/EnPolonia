package com.shinyieva.enpolonia.sdl.data;

public enum ListRowType {

	Unknown(0), Header(1), EntryRow(2);
	
	private final int value;
	
	ListRowType(int val) {
		this.value = val;
	}
	public int value() {
		return this.value;
	}
	public static ListRowType fromInt (int val) {
		ListRowType result;
		try {
			result = values()[val];
		} catch (Exception e) {
			result = Unknown;
		}
		return result;
	}

}

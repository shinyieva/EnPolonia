package com.shinyieva.enpolonia.sdl.data;

public class Channel {
	String Title;
	String Language;
	String UpdatePeriod;
	int    UpdateFrequency;
	
	public Channel(){
		
	}
	
	public void setTitle(String title){
		Title = title;
	}
	
	public void setLanguage(String lang){
		Language = lang;
	}
	
	public void setUpdatePeriod(String period){
		UpdatePeriod = period;
	}
	
	public void setUpdateFrequency(int freq){
		UpdateFrequency = freq;
	}
	
	public String getTitle(){
		return Title;
	}
	
	public String getLanguage(){
		return Language;
	}
	
	public String getUpdatePeriod(){
		return UpdatePeriod;
	}
	
	public int getUpdateFrequency(){
		return UpdateFrequency;
	}
}

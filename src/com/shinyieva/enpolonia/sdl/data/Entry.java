package com.shinyieva.enpolonia.sdl.data;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.shinyieva.enpolonia.net.cache.Cache;

public class Entry implements Parcelable, Comparable<Entry>{

	String 		_BlogTitle;
	String      _Guid;
	String 		_Title;
	String 		_Url;
	Date   		_Date;
	String 		_Creator;
	String      _Description;
	int    		_NumComments = -1;
	int 		_Unread = 1;//1 -> Unread, 0 -> read
	String      _Content;
	String      _CommentRssUrl;
	
	public Entry(){
		
	}

	public Entry (Cache cache){
		this();
		
		setGuid(cache.getGuid());
		setTitle(cache.getTitle());
		setLink(cache.getLink());
		setDate(cache.getDate());
		setCreator(cache.getCreator());
		setDescripcion(cache.getDescription());
		setNumComments(Integer.parseInt(cache.getNumComm()));
		setUnread(Integer.parseInt(cache.getUnread()));
		setContent(cache.getContent());
		setCommentRssUrl(cache.getUrlComm());
		
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(_BlogTitle);
		dest.writeString(_Guid);
		dest.writeString(_Title);
		dest.writeString(_Url);
		dest.writeString(String.valueOf(_Date.getTime()));
		dest.writeString(_Creator);
		dest.writeString(_Description);
		dest.writeInt(_NumComments);
		dest.writeInt(_Unread);     //if _unread == true, byte == 1
		dest.writeString(_Content);
		dest.writeString(_CommentRssUrl);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<Entry> CREATOR = new Parcelable.Creator<Entry>() {

		public Entry createFromParcel(Parcel in) {
			return new Entry(in);
		}

		public Entry[] newArray(int size) {
			return new Entry[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated
	// with it's values
	private Entry(Parcel in) {
		this();

		_BlogTitle   	= in.readString();
		_Guid           = in.readString();
		_Title       	= in.readString();
		_Url         	= in.readString();
		_Date        	= new Date(Long.parseLong(in.readString()));
		_Creator   	 	= in.readString();
		_Description    = in.readString();
		_NumComments 	= in.readInt();
		_Unread		    = in.readInt();
		_Content     	= in.readString();
		_CommentRssUrl  = in.readString();
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getTitle() {
        return _Title;
    }
 
    public String getLink() {
        return _Url;
    }
 
    public String getDescription() {
        return _Description;
    }
 
    public String getGuid() {
        return _Guid;
    }
 
    public Date getDate() {
        return _Date;
    }
 
    public String getCreator() {
    	return _Creator;
    }
   
    public String getContent() {
    	return _Content;
    }
    
    public int getNumComments(){
    	return _NumComments;
    }
    
    public String getCommentRssUrl(){
    	return _CommentRssUrl;
    }
    
    public int getUnread () {
    	return _Unread;
    }
    
    public void setTitle(String t) {
        _Title = t;
    }
 
    public void setLink(String l) {
        _Url = l;
    }
 
    public void setDescripcion(String d) {
        _Description = d;
    }
 
    public void setGuid(String g) {
        _Guid = g;
    }
 
    public void setDate(String f) {
    	 _Date = new Date(Long.parseLong(f.trim()));
    }
    
    public void setCreator(String C) {
    	_Creator = C;
    }
    
    public void setContent(String C) {
    	_Content = C;
    }
	
    public void setNumComments(int N){
    	_NumComments = N;
    }
    
    public void setCommentRssUrl(String url){
    	_CommentRssUrl = url;
    }
    
    public void setUnread (int unread) {
    	_Unread = unread;
    }

	public int compareTo(Entry another) {
		
		return another.getDate().compareTo(this.getDate());
	}
}

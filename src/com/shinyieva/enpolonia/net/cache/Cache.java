package com.shinyieva.enpolonia.net.cache;


public class Cache {
	
	public String Guid;
	public String Title;
	public String Link;
	public String Description;
	public String Fecha;
	public String Creator;
	public String UrlComm;
	public String NumComm;
	public String Content;
	public String Unread;
	
	public Cache (String fieldGuid, String fieldTitle, String fieldLink,
			String fieldDescription, String fieldDate, String fieldCreator,
			String fieldUrlcomm, String fieldNumcomm, String fieldContent,
			String fieldUnread) {

		setGuid(fieldGuid);
		settitle(fieldTitle);
		setLink(fieldLink);
		setDescription(fieldDescription);
		setDate(fieldDate);
		setCreator(fieldCreator);
		setUrlComm(fieldUrlcomm);
		setNumComm(fieldNumcomm);
		setContent(fieldContent);
		setUnread(fieldUnread);
		
	}

	public void setGuid (String id) {
		Guid = id;
	}
	
	public String getGuid () {
		return Guid;
	}
	
	public void settitle (String title) {
		Title = title;
	}
	
	public String getTitle () {
		return Title;
	}
	
	public void setLink (String link) {
		Link = link;
	}
	
	public String getLink () {
		return Link;
	}
	
	public void setDescription (String description) {
		Description = description;
	}
	
	public String getDescription () {
		return Description;
	}
	
	public void setDate (String p_date) {
		
		Fecha = p_date;
	}
	
	public String getDate () {
		return Fecha;
	}
	
	public void setCreator (String creator) {
		Creator = creator;
	}
	
	public String getCreator () {
		return Creator;
	}
	
	public void setUrlComm (String urlComm) {
		UrlComm = urlComm;
	}
	
	public String getUrlComm () {
		return UrlComm;
	}
	
	public void setNumComm (String numComm) {
		NumComm = numComm;
	}
	
	public String getNumComm () {
		return NumComm;
	}
	
	public void setContent (String content) {
		Content = content;
	}
	
	public String getContent () {
		return Content;
	}
	
	public void setUnread (String unread) {
		Unread = unread;
	}
	
	public String getUnread () {
		return Unread;
	}
}

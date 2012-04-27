package com.shinyieva.enpolonia.sdl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.shinyieva.enpolonia.sdl.data.Entry;

public class RssHandler extends DefaultHandler {
	private List<Entry> noticias;
	private Entry noticiaActual;
	private StringBuilder sbTexto;

	public List<Entry> getNoticias() {
		return this.noticias;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		super.characters(ch, start, length);

		if (this.noticiaActual != null)
			this.sbTexto.append(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {

		super.endElement(uri, localName, name);

		if (this.noticiaActual != null) {
			if (localName.equals("title")) {
				this.noticiaActual.setTitle(this.sbTexto.toString().trim());
			} else if (localName.equals("link")) {
				this.noticiaActual.setLink(this.sbTexto.toString().trim());
			} else if (localName.equals("description")) {
				this.noticiaActual.setDescripcion(this.sbTexto.toString()
						.trim());
			} else if (localName.equals("guid")) {
				this.noticiaActual.setGuid(this.sbTexto.toString().trim());
			} else if (localName.equals("pubDate")) {
				// SimpleDateFormat f = new
				// SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date(this.sbTexto.toString().trim());

				this.noticiaActual.setDate(String.valueOf(date.getTime()));
			} else if (localName.equals("creator")) {
				this.noticiaActual.setCreator(this.sbTexto.toString().trim());
			} else if (localName.equals("encoded")) {
				this.noticiaActual.setContent(this.sbTexto.toString().trim());
			} else if (localName.equals("commentRss")) {
				this.noticiaActual.setCommentRssUrl(this.sbTexto.toString()
						.trim());
			} else if (localName.equals("comments")) {
				String aux = this.sbTexto.toString().trim();
				if (this.TryParseInt(aux)) {
					this.noticiaActual.setNumComments(Integer.parseInt(aux));
				}
			} else if (localName.equals("item")) {
				this.noticias.add(this.noticiaActual);
			}

			this.sbTexto.delete(0, this.sbTexto.length());
			this.sbTexto.setLength(0);
		}
	}

	@Override
	public void startDocument() throws SAXException {

		super.startDocument();

		this.noticias = new ArrayList<Entry>();

		this.sbTexto = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {

		super.startElement(uri, localName, name, attributes);

		if (localName.equals("item")) {
			this.noticiaActual = new Entry();
		}
	}

	boolean TryParseInt(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
package com.shinyieva.enpolonia.sdl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.util.Log;

import com.shinyieva.enpolonia.sdl.data.Entry;
import com.shinyieva.enpolonia.settings.AppSettings;

public class RssParserSax
{
    private URL rssUrl;
 
    public RssParserSax(String url)
    {
        try
        {
            this.rssUrl = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }
 
    public List<Entry> parse()
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        Log.d(AppSettings.TAG_LOG, "parse()");
        try
        {
            SAXParser parser = factory.newSAXParser();
            RssHandler handler = new RssHandler();
            parser.parse(this.getInputStream(), handler);
            return handler.getNoticias();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
 
    private InputStream getInputStream()
    {
        try
        {
            return rssUrl.openConnection().getInputStream();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}

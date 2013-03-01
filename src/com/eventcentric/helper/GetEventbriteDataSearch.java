package com.eventcentric.helper;


import android.content.res.XmlResourceParser;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetEventbriteDataSearch {
    private static Matcher digitsMatcher = Pattern.compile(".*\\((\\d+)\\).*").matcher("");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat resultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static List<EventObject> getFeed(String categ, String strEventbriteKey,
                                          String strSearchFor,
                                          String strLat,
                                          String strLon,
                                          Date strUTCDate,
                                          String strCityState) {
        List<EventObject> events = new ArrayList<EventObject>();
        String strSearchDate = "";
        strSearchDate = formatDateToUTC(strUTCDate);
        String URLaddress = "";
        String strSearchKeyWords = "";
        
        String category = "";
        if(categ.equals("1"))
        	category = "&category=music&keywords=music%20OR%20concert";
        else if(categ.equals("2"))
        	category = "&category=food";
        else if(categ.equals("3"))
        	category = "&keywords=performances,fairs,recreation,entertainment,sports,social";
        else if(categ.equals("4"))
        	category = "&category=conference,fundraisers,other,tradeshows,seminars";

        if (strSearchFor.length()>0)
            strSearchKeyWords = "&keywords=" + strSearchFor;

        if (strLat.length() == 0 | strLon.length() == 0) {
            URLaddress = "https://www.eventbrite.com/xml/event_search?app_key=" + strEventbriteKey +category+"&date=Future&within=45&within_unit=M&max=30&sort_by=date" + strCityState + strSearchKeyWords;
        } else {
            URLaddress = "https://www.eventbrite.com/xml/event_search?app_key=" + strEventbriteKey +category+ "&latitude=" + strLat + "&longitude=" + strLon + "&date=Future&within=45&within_unit=M&max=30&sort_by=date" + strSearchKeyWords;
        }
 Log.i("GETEB",URLaddress);
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            URL episode_feed_url = new URL(URLaddress);
//If we want to set timeout
/*URLConnection urlConnection = episode_feed_url.openConnection();
urlConnection.setReadTimeout(30000);   // 30 second timeout
xpp.setInput(urlConnection.getInputStream(), null);*/

           xpp.setInput(episode_feed_url.openConnection().getInputStream(), null);
            int eventType = xpp.getEventType();

            String strEventID = "";
            String strTitle = "";
            String strEventURL = "";
            String strVenueName = "";
            String strVenueAddress = "";
            String strVenueCity = "";
            String strVenueLat = "";
            String strVenueLon = "";
            String strPrice = "";
            String strDescription = "";
            String strStartDate = "";
            String strEndDate = "";
            String strEventImage = "http://www.shizzlr.com/images/eventbrite_square.png";
            String strEventPhoto = "";
            String strOrgName= "";
            String strOrgUrl= "";
            String strOrgDesc = "";
            int iEventLevel = -1;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag_name = null;
                switch (eventType) {
                    //Found the start of the feed
                    case XmlResourceParser.START_DOCUMENT:
                        break;
                    //Found a start tag
                    case XmlResourceParser.START_TAG:
                        tag_name = xpp.getName();
                        if (tag_name.equalsIgnoreCase("event")) {
                            iEventLevel = xpp.getDepth() + 1;
                        }

                        // Event Level Tags
                        if (iEventLevel ==xpp.getDepth())
                        {
                            if (tag_name.equals("id")) {
                                        try {
                                            strEventID = (xpp.nextText());
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                            if (tag_name.equals("title")) {
                                try {
                                    strTitle = (xpp.nextText());
                                } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                            } else if (tag_name.equals("start_date")) {
                                try {
                                    strStartDate = (xpp.nextText());
                                } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                            } else if (tag_name.equals("end_date")) {
                                try {
                                    strEndDate = (xpp.nextText());
                                } catch (org.xmlpull.v1.XmlPullParserException ex) {}

                            } else if (tag_name.equals("logo")) {
                                try {
                                    strEventPhoto = "";
                                    strEventPhoto = (xpp.nextText());
                                } catch (org.xmlpull.v1.XmlPullParserException ex) {}

                            } else if (tag_name.equals("description")) {
                                try {
                                    strDescription = (xpp.nextText());
                                } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                            } else if (tag_name.equals("url")) {
                                try {
                                    strEventURL = (xpp.nextText());
                                } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                            }
                        } // End Event Level Tags

                        // Event Venue Level Data
                        if (tag_name.equalsIgnoreCase("venue")) {
                            eventType = xpp.next();
                            boolean bScanVenue = true;
                            while (bScanVenue) {
                                if (eventType == XmlResourceParser.START_TAG) {
                                    tag_name = xpp.getName();
                                    if (tag_name.equals("name")) {
                                        try {
                                            strVenueName = (xpp.nextText());
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                    if (tag_name.equals("address")) {
                                        try {
                                            strVenueAddress = (xpp.nextText());
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                    if (tag_name.equals("city")) {
                                        try {
                                            strVenueCity = (xpp.nextText());
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                    if (tag_name.equals("latitude")) {
                                        try {
                                            strVenueLat = (xpp.nextText());
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                    if (tag_name.equals("longitude")) {
                                        try {
                                            strVenueLon = (xpp.nextText());
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                }
                                if (eventType == XmlResourceParser.END_TAG) {
                                    if (xpp.getName().equalsIgnoreCase("venue"))
                                        bScanVenue = false;
                                }
                                eventType = xpp.next();
                            }
                        }

                        // Event Ticket Level data
                        if (tag_name.equalsIgnoreCase("tickets")) {
                            eventType = xpp.next();
                            boolean bScanTickets = true;
                            while (bScanTickets) {
                                if (eventType == XmlResourceParser.START_TAG) {
                                    tag_name = xpp.getName();
                                   if (tag_name.equals("price")) {
                                        try {
                                            String strValidPrice = xpp.nextText();
                                            if (strValidPrice.contentEquals("0.00"))
                                            {
                                              //Dont use it..
                                            }
                                            else
                                            {
                                            strPrice = strPrice + "$" + strValidPrice + ",";
                                            }
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                }
                                if (eventType == XmlResourceParser.END_TAG) {
                                    if (xpp.getName().equalsIgnoreCase("tickets"))
                                        bScanTickets = false;
                                }
                                eventType = xpp.next();
                            }
                        }

                        // organizer level data
                        if (tag_name.equalsIgnoreCase("organizer")) {
                            eventType = xpp.next();
                            boolean bOrganizer = true;
                            while (bOrganizer) {
                                if (eventType == XmlResourceParser.START_TAG) {
                                    tag_name = xpp.getName();
                                   if (tag_name.equals("name")) {
                                        try {
                                            strOrgName = (xpp.nextText()) ;
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                    if (tag_name.equals("url")) {
                                        try {
                                            strOrgUrl =  (xpp.nextText()) ;
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                    if (tag_name.equals("description")) {
                                        try {
                                            strOrgDesc= (xpp.nextText());
                                        } catch (org.xmlpull.v1.XmlPullParserException ex) {}
                                    }
                                }
                                if (eventType == XmlResourceParser.END_TAG) {
                                    if (xpp.getName().equalsIgnoreCase("organizer"))
                                        bOrganizer = false;
                                }
                                eventType = xpp.next();
                            }
                        }
                    break;
                    //An end tag has been reached
                    case XmlResourceParser.END_TAG:
                        tag_name = xpp.getName();
                        //End of an event -  Create the event object -
                        //                   add to the events list
                        if (tag_name.equalsIgnoreCase("event")) {
                            EventObject myEvent = new EventObject();
                            myEvent.setDate((formatToDate(strStartDate)));
                            myEvent.setEventID(strEventID);
                            myEvent.setId(-1);
                            myEvent.setEventType(8);
                            SimpleDateFormat myEventDate = new SimpleDateFormat("EEE M/d hh:mm aa");
                            try{
                                Date dstartDate = formatToDate(strStartDate);
                                StringBuilder strMyFormatedDate = new StringBuilder(myEventDate.format(dstartDate));
                                strStartDate = strMyFormatedDate.toString();

                            } catch (Exception ex) {}

                            try{
                                Date dendDate = formatToDate(strEndDate);
                                StringBuilder strMyFormatedDate = new StringBuilder(myEventDate.format(dendDate));
                                strEndDate = strMyFormatedDate.toString();

                            } catch (Exception ex) {}
                            myEvent.setStartDate(strStartDate);
                            myEvent.setEndDate(strEndDate);
                            myEvent.setName(strTitle);
                            myEvent.setLocation(strVenueName);
                            myEvent.setStreet(strVenueAddress);
                            myEvent.setEventAddress(strVenueAddress);
                            myEvent.setTown(strVenueCity);
                            myEvent.setState("");
                            myEvent.setEventLat(strVenueLat);
                            myEvent.setEventLon(strVenueLon);
                            myEvent.setEventLogo("http://www.shizzlr.com/images/eventbrite_logo.png");
                            myEvent.setEventLogoLabel("Eventbrite Event");

                            if (strEventPhoto.length() > 0) {
                                strEventImage = strEventPhoto;
                            }
                            //Remove last comma
                            if (strPrice.length() >0)
                            {
                                           strPrice = strPrice.substring(0,strPrice.length()-1);
                            
                            }
                            myEvent.setImage(strEventImage);
                            myEvent.setEventURL(strEventURL);

                            if (strOrgUrl.length()>0)
                               strOrgUrl = "\n" + strOrgUrl;
                            if (strOrgDesc.length()>0)
                                strOrgDesc = "\n" + strOrgDesc;
                            myEvent.setDescription(strDescription);
                           
                            
                            
                            
                            //if(strEventImage != "http://www.shizzlr.com/images/eventbrite_square.png")
                            events.add(myEvent);
                            
                            
                            
                            
                            strEventID = "";
                            strTitle = "";
                            strEventURL = "";
                            strPrice = "";
                            strVenueName = "";
                            strVenueAddress = "";
                            strVenueCity = "";
                            strVenueLat = "";
                            strVenueLon = "";
                            strDescription = "";
                            strStartDate = "";
                            strEndDate = "";
                            strEventImage = "http://www.shizzlr.com/images/eventbrite_square.png";
                            strEventPhoto = "";
                            strOrgUrl = "";
                            strOrgName = "";
                            strOrgDesc = "";

                        }
                        break;
                }
                eventType = xpp.next();

            }

        episode_feed_url.openStream().close();
        } catch (Exception e) {
        }
        Log.i("GETEB CNT",Integer.toString(events.size()));
        return events;

    }

    public static Date convertFromGmt(long time) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(time);
        cal.setTimeInMillis(time);
        return cal.getTime();
    }

    private static Date getDate(String s) throws Exception {
        digitsMatcher.reset(s);
        if (!digitsMatcher.find())
            return null;
        return convertFromGmt(Long.parseLong(digitsMatcher.group(1)));
    }


    static String formatDateToUTC(Date date) {
        Calendar myPassedDate = Calendar.getInstance();
        myPassedDate.setTimeInMillis(date.getTime());
        String strPassedDate = dateFormat.format(myPassedDate.getTime());
        return strPassedDate;
    }

    static Date formatToDate(String strPassedDate) {
        Date formattedDate = null;
        try {
            formattedDate = resultDateFormat.parse(strPassedDate);
        } catch (ParseException e) {   
        }
        return formattedDate;
    }

}


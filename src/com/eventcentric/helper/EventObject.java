package com.eventcentric.helper;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

//Eventbrite event object
public class EventObject implements Parcelable {

    //example: {"EventID":159886457379846,"EventName":"PLUS, (All White) CLUB PASSION! RENAISSANCE / BLACK CHINEY / LIQUID","Lat":"26.0108","Long":"-80.1497","Date":"\/Date(1290747600000)\/","EventImage":"http://profile.ak.fbcdn.net/hprofile-ak-snc4/hs478.snc4/50552_159886457379846_3479059_t.jpg","location":"PASSION NIGHTCLUB l Hard Rock Hotel & Casino","town":"Hollywood","state":"FL","street":"5701 Seminole Way","description":"Hard Rock, Passion Nightclub, Rush promotions..\n++ PLUS ++ \"All White Edition\", Thanks Giving Night!\n\nMusic By:\nRENAISSANCE / BLACK CHINEY / ZJ LIQUID \n\nThursday Nov 25th 2010 @ Passion Night Club\n5701 Seminole Way, Hollywood, FL 33314\n\nNo School, No Work the next day!\n\nFor VIP Reservation & Info:\n(754)234-1341\n\n$20 PRE-SOLD\n$35 PRE-SOLD VIP \n\nTICKET LOCATIONS: \nAunt I\u0027s Lauderhill:(954)321-0191\nAunt I\u0027s Pembroke Pines : (954)433-0223\nAunt I\u0027s Miami: (305)654-9638\n\nIsland Restaurant \nKendall: (305)388-5118\n\nDIRECTIONS:\nTurnpike to Griffin Rd (east) to 441 South. The Hard Rock will be on the right. \n","AttendingCount":7,"MaybeCount":11}]
    private long id;
    private String name;
    private String image;
    private String location;
    private Date date;
    private double latitude, longitude;
    
    private String description;
    private String htmlDescription;
    private String state, town, street;
   
    
    private int eventType;
    
    private String eventID;
    
    private String startDate;
    private String endDate;
    private String eventLat;
    private String eventLon;
    private String eventURL;
    private String eventLogo;
    private String eventLogoLabel;
    
    private String eventAddress;

    public EventObject() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     public String getHTMLDescription() {
        return htmlDescription;
    }

    public void setHTMLDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    

    /*public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setfbwallContent(List<fbwallContent_Post> posts) {
        this.wallposts = posts;
    }

    public List<fbwallContent_Post> getfbwallContent() {
        return wallposts;
    }*/

   

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int iEventType) {
        this.eventType = iEventType;
    }

   

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String strEventID) {
        this.eventID = strEventID;
    }

    
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String strStartDate) {
        this.startDate = strStartDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String strEndDate) {
        this.endDate = strEndDate;
    }

    public String getEventLat() {
        return eventLat;
    }

    public void setEventLat(String strEventLat) {
        this.eventLat = strEventLat;
    }

    public String getEventLon() {
        return eventLon;
    }

    public void setEventLon(String strEventLon) {
        this.eventLon = strEventLon;
    }

    public String getEventURL() {
        return eventURL;
    }

    public void setEventURL(String strEventURL) {
        this.eventURL = strEventURL;
    }

    public String getEventLogo() {
        return eventLogo;
    }

    public void setEventLogo(String strEventLogo) {
        this.eventLogo = strEventLogo;
    }

    public String getEventLogoLabel() {
        return eventLogoLabel;
    }

    public void setEventLogoLabel(String strEventLogoLabel) {
        this.eventLogoLabel = strEventLogoLabel;
    }

   

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String strEventAddress) {
        this.eventAddress = strEventAddress;
    }


    public static final Parcelable.Creator<EventObject> CREATOR = new Parcelable.Creator<EventObject>() {
        public EventObject createFromParcel(Parcel in) {
            return new EventObject(in);
        }

        public EventObject[] newArray(int size) {
            return new EventObject[size];
        }
    };


    private EventObject(Parcel in) {
        id = in.readLong();
        name = in.readString();
        image = in.readString();
        date = new Date(in.readLong());
        location = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        description = in.readString();
        htmlDescription = in.readString();
        state = in.readString();
        town = in.readString();
        street = in.readString();
        
        eventType = in.readInt();
       
        eventID = in.readString();
        
        startDate = in.readString();
        endDate = in.readString();
        eventLat = in.readString();
        eventLon = in.readString();
        eventURL = in.readString();
        eventLogo = in.readString();
        eventLogoLabel = in.readString();
        
        eventAddress = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(image);
        if (date != null) {
            dest.writeLong(date.getTime());
        } else {
            dest.writeLong(-1);
        }
        dest.writeString(location);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        
        dest.writeString(description);
        dest.writeString(htmlDescription);
        dest.writeString(state);
        dest.writeString(town);
        dest.writeString(street);
        
        dest.writeInt(eventType);
        
        dest.writeString(eventID);
        
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(eventLat);
        dest.writeString(eventLon);
        dest.writeString(eventURL);
        dest.writeString(eventLogo);
        dest.writeString(eventLogoLabel);
        
        dest.writeString(eventAddress);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}

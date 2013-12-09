package cz.muni.fi.sdipr.web.facebook.model;

import facebook4j.EventPrivacyType;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 27.11.2013
 * Time: 17:58
 */
public class Event {

    private String eid;
    private String name;
    private String description;
    private EventPrivacyType privacyType;
    private String location;
    private EventStatus status;
    private Date startTime;
    private Date endTime;

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventPrivacyType getPrivacyType() {
        return privacyType;
    }

    public void setPrivacyType(EventPrivacyType privacyType) {
        this.privacyType = privacyType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

package cz.muni.fi.sdipr.web.facebook.model;

import facebook4j.EventPrivacyType;

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
    private String startTime;
    private String endTime;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

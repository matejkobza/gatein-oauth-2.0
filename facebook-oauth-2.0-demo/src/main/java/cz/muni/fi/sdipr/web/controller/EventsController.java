package cz.muni.fi.sdipr.web.controller;

import com.google.gson.Gson;
import cz.muni.fi.sdipr.core.FacebookLoginService;
import cz.muni.fi.sdipr.core.FacebookScope;
import cz.muni.fi.sdipr.core.interceptor.FacebookLogin;
import cz.muni.fi.sdipr.web.facebook.model.Event;
import cz.muni.fi.sdipr.web.facebook.model.EventStatus;
import facebook4j.*;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 26.11.2013
 * Time: 10:49
 */
@Named
@SessionScoped
public class EventsController implements Serializable {

    private static final long serialVersionUID = -5551713344085777485L;

    @Inject
    private FacebookLoginService facebookLoginService;

    private Facebook facebook;
    private List<Event> events;
    private List<Event> userEvents;
    private Gson gson = new Gson();
    private Event event = new Event();

    @PostConstruct
    public void init() {
    }

    @FacebookLogin
    public void process() throws FacebookException {
        facebook = facebookLoginService.getFacebook();

        try {
            loadEvents();
        } catch (JSONException e) {
            throw new FacebookException("Unable to load events.", e);
        }
    }

    private void loadEvents() throws FacebookException, JSONException {
        events = new ArrayList<Event>();
        userEvents = new ArrayList<Event>();
        // Events the current user is attending
        Map<String, String> queries = new HashMap<String, String>();

        queries.put("attending", "SELECT eid, name, start_time, end_time FROM event WHERE eid IN (SELECT eid FROM event_member WHERE uid = me() and rsvp_status = 'attending') AND (start_time > now() OR end_time > now()) ORDER BY start_time");
        queries.put("declined", "SELECT eid, name, start_time, end_time FROM event WHERE eid IN (SELECT eid FROM event_member WHERE uid = me() and rsvp_status = 'declined') AND (start_time > now() OR end_time > now()) ORDER BY start_time");
        queries.put("unsure", "SELECT eid, name, start_time, end_time FROM event WHERE eid IN (SELECT eid FROM event_member WHERE uid = me() and rsvp_status = 'unsure') AND (start_time > now() OR end_time > now()) ORDER BY start_time");
        queries.put("not_replied", "SELECT eid, name, start_time, end_time FROM event WHERE eid IN (SELECT eid FROM event_member WHERE uid = me() and rsvp_status = 'not_replied') AND (start_time > now() OR end_time > now()) ORDER BY start_time");
        queries.put("users", "SELECT eid FROM event WHERE creator=me()");

        Map<String, JSONArray> results = facebook.executeMultiFQL(queries);

        JSONArray attending = results.get("attending");
        for (int i = 0; i < attending.length(); i++) {
            JSONObject jsonObject = attending.getJSONObject(i);
            Event event = convertObjectToEvent(jsonObject);
            event.setStatus(EventStatus.ATTENDING);
            events.add(event);
        }

        JSONArray declined = results.get("declined");
        for (int i = 0; i < declined.length(); i++) {
            JSONObject jsonObject = declined.getJSONObject(i);
            Event event = convertObjectToEvent(jsonObject);
            event.setStatus(EventStatus.DECLINED);
            events.add(event);
        }

        JSONArray unsure = results.get("unsure");
        for (int i = 0; i < unsure.length(); i++) {
            JSONObject jsonObject = unsure.getJSONObject(i);
            Event event = convertObjectToEvent(jsonObject);
            event.setStatus(EventStatus.UNSURE);
            events.add(event);
        }

        JSONArray notReplied = results.get("not_replied");
        for (int i = 0; i < notReplied.length(); i++) {
            Event event = convertObjectToEvent(notReplied.getJSONObject(i));
            event.setStatus(EventStatus.NOT_REPLIED);
            events.add(event);
        }

        JSONArray users = results.get("users");
        for (int i = 0; i < users.length(); i++) {
            JSONObject jsonObject = users.getJSONObject(i);
            Event event = gson.fromJson(jsonObject.toString(), Event.class);
            userEvents.add(event);
        }
    }

    private Event convertObjectToEvent(JSONObject object) throws FacebookException {
        Event event = gson.fromJson(object.toString(), Event.class);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            event.setStartTime(sdf.parse(object.getString("start_time")));
        } catch (JSONException e) {
            throw new FacebookException("Unable to get field start_time.", e);
        } catch (ParseException e) {
            throw new FacebookException("Unable to parse field start_time.", e);
        }
        try {
            String endTime = object.getString("end_time");
            if (endTime != null) {
                try {
                    event.setEndTime(sdf.parse(endTime));
                } catch (ParseException e) {
                    throw new FacebookException("Unable to parse field end_time.", e);
                }
            }
        } catch (JSONException e) {
            throw new FacebookException("Unable to get field end_time.", e);
        }

        return event;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setScopes() {
        facebookLoginService.addScope(FacebookScope.USER_EVENTS.getValue());
        facebookLoginService.addScope(FacebookScope.FRIENDS_EVENTS.getValue());
        facebookLoginService.addScope(FacebookScope.CREATE_EVENT.getValue());
        facebookLoginService.addScope(FacebookScope.RSVP_EVENT.getValue());
    }

    public void rsvpEventAsAttending(String eid) throws FacebookException {
        facebook.rsvpEventAsAttending(eid);
    }

    public void rsvpEventAsDeclined(String eid) throws FacebookException {
        facebook.rsvpEventAsDeclined(eid);
    }

    public void rsvpEventAsMaybe(String eid) throws FacebookException {
        facebook.rsvpEventAsMaybe(eid);
    }

    public void saveOrUpdateEvent() throws FacebookException {
        EventUpdate eventUpdate = new EventUpdate();

        eventUpdate.setName(event.getName());
        eventUpdate.setDescription(event.getDescription());
        eventUpdate.setLocation(event.getLocation());
        eventUpdate.setPrivacyType(event.getPrivacyType());

        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getStartTime());
        eventUpdate.setStartTime(cal);
        cal = Calendar.getInstance();
        cal.setTime(event.getEndTime());
        eventUpdate.setEndTime(cal);

        if (event.getEid() == null) {
            facebook.createEvent(eventUpdate);
        } else {
            facebook.editEvent(event.getEid(), eventUpdate);
        }

        event = new Event();

        try {
            loadEvents();
        } catch (JSONException e) {
            throw new FacebookException("Unable to load events.", e);
        }
    }

    public void deleteEvent(String eid) throws FacebookException {
        facebook.deleteEvent(eid);
        try {
            loadEvents();
        } catch (JSONException e) {
            throw new FacebookException("Unable to load events.", e);
        }
    }

    public void reset() {
        event = new Event();
    }

    public boolean isUserEvent(Event e) {
        for (Event event : userEvents) {
            if (event.getEid().equals(e.getEid())) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public List<String> complete(String query) throws FacebookException {
        List<String> results = new ArrayList<String>();

        ResponseList<Place> places = facebook.searchPlaces(query);
        results.addAll(CollectionUtils.collect(places, new Transformer() {
            @Override
            public Object transform(Object o) {
                return ((Place) o).getName();
            }
        }));

        return results;
    }

    public void loadEvent(String eid) throws FacebookException {
        facebook4j.Event e = facebook.getEvent(eid);
        event = new Event();
        event.setEid(eid);
        event.setName(e.getName());
        event.setDescription(e.getDescription());
        event.setLocation(e.getLocation());
        event.setStartTime(e.getStartTime());
        event.setEndTime(e.getEndTime());
        event.setPrivacyType(e.getPrivacy());
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}

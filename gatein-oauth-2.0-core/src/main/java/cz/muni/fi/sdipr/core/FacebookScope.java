package cz.muni.fi.sdipr.core;

/**
 * Created with IntelliJ IDEA.
 * User: matejkobza
 * Date: 26.11.2013
 * Time: 10:58
 */
public enum FacebookScope {

    // Email Permissions
    EMAIL("email"),

    // Extended Permissions - read
    READ_FRIRENDLISTS("read_friendlists"),
    READ_INSIGHTS("read_insights"),
    READ_MAILBOX("read_mailbox"),
    READ_REQUESTS("read_requests"),
    READ_STREAM("read_stream"),
    XMPP_LOGIN("xmpp_login"),
    USER_ONLINE_PRESENCE("user_online_presence"),
    FRIENDS_ONLINE_PRESENCE("friends_online_presence"),

    // Extended Permissions - publish
    ADS_MANAGEMENT("ads_management"),
    CREATE_EVENT("create_event"),
    MANAGE_FRIENDLISTS("manage_friendlists"),
    MANAGE_NOTIFICATIONS("manage_notifications"),
    PUBLISH_ACTIONS("publish_actions"),
    PUBLISH_STREAM("publish_stream"),
    RSVP_EVENT("rsvp_event"),

    // Extended Profile Properties - USER
    USER_ABOUT_ME("user_about_me"),
    USER_ACTIVITIES("user_activities"),
    USER_BIRTHDAY("user_birthday"),
    USER_CHECKING("user_checkins"),
    USER_EDUCATION_HISTORY("user_education_history"),
    USER_EVENTS("user_events"),
    USER_GROUPS("user_groups"),
    USER_HOMETOWN("user_hometown"),
    USER_INTERESTS("user_interests"),
    USER_LIKES("user_likes"),
    USER_LOCATION("user_location"),
    USER_NOTES("user_notes"),
    USER_PHOTOS("user_photos"),
    USER_QUESTIONS("user_questions"),
    USER_RELATIONSHIPS("user_relationships"),
    USER_RELATIONSHIP_DETAILS("user_relationship_details"),
    USER_RELIGION_POLITICS("user_religion_politics"),
    USER_STATUS("user_status"),
    USER_SUBSCRIPTIONS("user_subscriptions"),
    USER_VIDEOS("user_videos"),
    USER_WEBSITE("user_website"),
    USER_WORK_HISTORY("user_work_history"),

    // Extended Profile Properties - FRIENDS
    FRIENDS_ABOUT_ME("friends_about_me"),
    FRIENDS_ACTIVITIES("friends_activities"),
    FRIENDS_BIRTHDAY("friends_birthday"),
    FRIENDS_CHECKINS("friends_checkins"),
    FRIENDS_EDUCATION_HISTORY("friends_education_history"),
    FRIENDS_EVENTS("friends_events"),
    FRIENDS_GROUPS("friends_groups"),
    FRIENDS_HOMETOWN("friends_hometown"),
    FRIENDS_INTERESTS("friends_interests"),
    FRIENDS_LIKES("friends_likes"),
    FRIENDS_LOCATION("friends_location"),
    FRIENDS_NOTES("friends_notes"),
    FRIENDS_PHOTOS("friends_photos"),
    FRIENDS_QUESTIONS("friends_questions"),
    FRIENDS_RELATIONSHIPS("friends_relationships"),
    FRIENDS_RELATIONSHIP_DETAILS("friends_relationship_details"),
    FRIENDS_RELIGION_POLITICS("friends_religion_politics"),
    FRIENDS_STATUS("friends_status"),
    FRIENDS_SUBSCRUPTIONS("friends_subscriptions"),
    FRIENDS_VIDEOS("friends_videos"),
    FRIENDS_WEBSITE("friends_website"),
    FRIENDS_WORK_HISTORY("friends_work_history");

    private String value;

    private FacebookScope(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

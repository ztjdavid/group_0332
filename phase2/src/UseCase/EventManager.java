package UseCase;
import Entity.*;
import UseCase.IGateWay.IEventGateWay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * EventManager class contains talks and methods that modify talks
 * Level in Clean Architecture: Use case class.
 */

public class EventManager {
    protected static int totalTalkCount = 0;
    protected HashMap<Integer, Event> eventList;
    protected static int currentTalkID = 0; // Track which Event this program is working on now.
    private IEventGateWay gateWay;
    public EventManager(IEventGateWay g) {
        this.gateWay = g;
        this.eventList = new HashMap<>();
    }

    /**
     * Set the currentTalkID.
     * @param talkID The current talk Id.
     */

    public void setCurrentEventID(int talkID){
        currentTalkID = talkID;
    }

    /**
     * Getter for the ID of the talk currently working with.
     * @return int currentTalkID.
     */

    public Event getCurrentEventWithID(){
        return this.eventList.get(currentTalkID);
    }

    /**
     * Get the speaker of this talk.
     * @param talkId The id of a talk.
     * @return An integer representing the id of the speaker in given talk.
     */
    public ArrayList<Integer> getSpeakerIDIn(int talkId){
        return new ArrayList<>(this.eventList.get(talkId).getSpeakerList());
    }

    /**
     * Add the attendee to the current Event.
     * @param attendee The new attendee.
     */

    public void addAttendee(Attendee attendee){
        this.eventList.get(currentTalkID).addAttendee(attendee.getUserId());
    }

    /**
     * Add the given Attendee's ID to the Event's attendee list given the talk ID.
     * @param talkid ID of the talk.
     * @param attendee Attendee who want to attend the talk.
     */

    public void addAttendeev2(int talkid, Attendee attendee){
        this.eventList.get(talkid).addAttendee(attendee.getUserId());
        try{
            gateWay.updateAttendeeList(talkid, eventList.get(talkid).getAttendeeId());/////ADD HERE?
        }catch (IOException ignored){};
    }
    public void addAttendeev2(int talkid, VIP vip){
        this.eventList.get(talkid).addAttendee(vip.getUserId());
        try{
            gateWay.updateAttendeeList(talkid, eventList.get(talkid).getAttendeeId());
        }catch (IOException ignored){};
    }

    /**
     * Remove the attendee from the Event.
     * @param attendee The unwanted attendee.
     */

    public void removeAttendee(Attendee attendee){
        this.eventList.get(currentTalkID).removeAttendee(attendee.getUserId());
    }

    /**
     * Remove the given Attendee's ID from the Event's attendee list given the talk ID.
     * @param talkid ID of the talk.
     * @param attendee Attendee who need to be removed from the talk.
     */

    public void removeAttendeev2(int talkid, Attendee attendee){
        this.eventList.get(talkid).removeAttendee(attendee.getUserId());
        try{
            gateWay.updateAttendeeList(talkid, eventList.get(talkid).getAttendeeId());
        }catch (IOException ignored){};
    }
    public void removeAttendeev2(int talkid, VIP vip){
        this.eventList.get(talkid).removeAttendee(vip.getUserId());
        try{
            gateWay.updateAttendeeList(talkid, eventList.get(talkid).getAttendeeId());
        }catch (IOException ignored){};
    }

    /**
     * Change the time of the Event.
     * @param time The new time (in 24-hour format) to be updated.
     * @return true iff the new time is valid (ie. between 9 and 17)
     */

    public boolean changeEventTime(int time) {
        if (time > 9 && time <= 17) {
            this.eventList.get(currentTalkID).setStartTime(time);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Change the speaker of the Event.
     * @param speakerID The new ID of the new speaker to be updated.
     */

    public void changeEventSpeaker(int speakerID){
        this.eventList.get(currentTalkID).setSpeaker(speakerID);
    }

    /**
     * Gets the number of remaining seats.
     * @return the number of remaining seats.
     */

    public int getRemainingSeats(){
        return this.eventList.get(currentTalkID).getRemainingSeat();
    }

    /**
     * Return the Event given the talkID
     * @param talkID The ID of the talk.
     * @return the Event with the given talkID.
     */

    public Event getEventWithId(int talkID){
        return this.eventList.get(talkID);
    }

    /**
     * Return an ArrayList of all the Talks.
     * @return An ArrayList containing all the Talks.
     */

    public ArrayList<Integer> getListOfEventsByType(int type){
        ArrayList<Event> events = new ArrayList<>(this.eventList.values());
        ArrayList<Integer> talks = new ArrayList<>();
        for(Event item:events){
            if(item.getEventType()==type){
                talks.add(item.getTalkId());
            }
        }
        return talks;
    }

    public ArrayList<Integer> getAllEvents(){
        ArrayList<Integer> events = new ArrayList<>();
        for(Event item : this.eventList.values()){
            events.add(item.getTalkId());
        }
        return events;
    }

    /**
     * Creates a talk and updates the talkList.
     * @param talkTitle the title of the talk.
     * @param startTime the time (between 9 and 17 in 24-hour format) of the talk.
     * @param roomId the roomId of the talk.
     * @param speakerID the ID of the speaker of the talk.
     * @return the ID of the talk iff the talk is successfully created.
     */

    public int createEvent(String talkTitle, int startTime, int roomId, ArrayList<Integer> speakerID,
                           int eventCapacity, int duration, boolean isVip){
        int talkId = totalTalkCount;
        Event newEvent = new Event(talkId, talkTitle,startTime, roomId, speakerID, eventCapacity, duration, isVip);
        this.eventList.put(talkId, newEvent);

        try{
            this.gateWay.writeNewEvent(totalTalkCount, talkTitle, startTime,
                    roomId, speakerID, eventCapacity, duration, isVip);
        }catch (IOException ignored){}

        totalTalkCount += 1;
        return talkId;
    }

    /**
     * Return a string information of the Event given the talk ID.
     * @param talkid The ID of the Event.
     * @return the string information of the Event with the given talk ID.(Including talk title, start time, room ID, and number of Attendee.)
     */

    public String getEventinfo(int talkid){
        String a = "\n-------------------------";
        Event event = getEventWithId(talkid);
        String talktitle = event.getTalkTitle();
        int talktime = event.getStartTime();
        int talkroom = event.getRoomId();
        int numatt = event.getAttendeeId().size();
        a = a + "\n Event Title:" + talktitle + "\n Event ID:" + talkid + "\n This event starts at " + talktime +
                "\n This event holds in roomID " + talkroom +"\n There are " + numatt + " attendees";
        return a;
    }

    /**
     * Return a string information of the Event given the talk ID and room name.
     * @param talkid The ID of the Event.
     * @param roomName The name of the room this talk is hold in.
     * @return the string information of the Event with the given talk ID.(Including talk title, start time, room ID, and number of Attendee.)
     */

    public String getEventinfoWithName(int talkid, String roomName){
        String a = "\n-------------------------";
        Event event = getEventWithId(talkid);
        String talktitle = event.getTalkTitle();
        int talktime = event.getStartTime();
        int talkroom = event.getRoomId();
        int numatt = event.getAttendeeId().size();
        a = a + "\n Event ID: "+ talkid +"\n Event Title:" + talktitle + "\n This event starts at " + talktime +
                "\n This event holds in roomID " + talkroom + "(" + roomName + ")" +"\n There are " + numatt + " attendees";
        return a;
    }

    /**
     * Return a simplified string information of the Event given the talk ID.
     * @param talkid The ID of the Event.
     * @return the simplified string information of the Event with the given talk ID.(Including talk title and talk ID.)
     */

    public String getEventinfosimp(int talkid){
        String a = "";
        Event event = getEventWithId(talkid);
        String talktitle = event.getTalkTitle();
        a = a + "\n Event Title:" + talktitle + "\n The id of this event is  " + talkid;
        return a;
    }

    /**
     * Return an arraylist of all the Attendee of the Talks in the given talk list.
     * @param talklist Arraylist of all ID of the Event.
     * @return Arraylist of all Attendee of all the Talks in the talk list.
     */

    public ArrayList<Integer> getallattendee(ArrayList<Integer> talklist){
        Set<Integer> att = new HashSet<>();
        for(Integer t:talklist){

            Event event = getEventWithId(t);
            att.addAll(event.getAttendeeId());
        }
        return new ArrayList<>(att);

    }


    /**
     * Return an arraylist of all the Attendee of the Talks in the given talk list.
     * @param eventList Arraylist of all ID of the Event.
     * @return Arraylist of all Attendee of all the Talks in the talk list.
     */

    public ArrayList<Integer> getAllSpeakers(ArrayList<Integer> eventList){
        ArrayList<Integer> speakers = new ArrayList<>();
        for(Integer t:eventList){
            Event event = getEventWithId(t);
            speakers.addAll(event.getSpeakerList());
        }
        return speakers;

    }

    /**
     * Return the total number of Talks
     * @return the int value of the total number of Talks.
     */

    public int getTotalTalkCount(){
        return totalTalkCount;
    }

    /**
     * Return the start time of the Event given the talk ID.
     * @param talkID The ID of the Event.
     * @return the start time of the Event with the given talk ID.
     */

    public int getStartTime(int talkID){
        Event event = getEventWithId(talkID);
        return event.getStartTime();
    }

    /**
     * Set the Event with the given talk ID with the Speaker with the given speaker ID.
     * @param speakerID The ID of the Speaker
     * @param talkID The ID of the Event.
     */

    public void setSpeakerTo(int speakerID, int talkID){
        Event event = getEventWithId(talkID);
        event.setSpeaker(speakerID);
    }

    /**
     * Remove the Event given the talkID
     * @param talkID The ID of the talk.
     */

    public void removeEvent(int talkID){
        this.eventList.remove(talkID);
    }

    /**
     * Get the title of the Event given the talk ID.
     * @param talkID The ID of the talk.
     * @return a String representation of the Event title.
     */

    public String getTitle(int talkID){
        Event event = this.eventList.get(talkID);
        return event.getTalkTitle();
    }

    /**
     * Get the room ID of the Event given the talk ID.
     * @param talkID The ID of the talk.
     * @return an int representation of the room ID
     */

    public int getRoomIdWithId(int talkID){
        Event event = this.eventList.get(talkID);
        return event.getRoomId();
    }

    public int getEventTypeWithID(int talkID){
        Event event = this.eventList.get(talkID);
        return event.getEventType();
    }

    public ArrayList<Integer> getSpeakerOfEvent(int eventID){
        Event event = this.eventList.get(eventID);
        return new ArrayList<>(event.getSpeakerList());
    }

    public ArrayList<Integer> getAttendeeOfEvent(int eventID){
        Event event = this.eventList.get(eventID);
        return new ArrayList<>(event.getAttendeeId());
    }

    public boolean checkEventExists(int eventID){
        return this.eventList.containsKey(eventID);
    }

    public int getDuration(int eventID){
        Event event = this.eventList.get(eventID);
        return event.getDuration();
    }

    public boolean checkVIP(int eventID){
        Event event = this.eventList.get(eventID);
        return event.getVIP();
    }

    public void setEventInfo(int id, ArrayList<Integer> attendeeList, int remainingSeat, int seatsOccupied){
        Event e = getEventWithId(id);
        e.setAttendeeList(attendeeList);
        e.setRemainingSeat(remainingSeat);
        e.setSeatsOccupied(seatsOccupied);
    }


}




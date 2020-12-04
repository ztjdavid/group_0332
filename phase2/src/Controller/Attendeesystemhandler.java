package Controller;

import Presenters.AttendeeUI;
import UseCase.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Attendeesystemhandler {
    protected AccountManager accM;
    protected EventManager eventManager;
    protected MessageManager MsgM;
    protected AttendeeUI attUI;
    protected StrategyManager strategyM;
    protected AttendeeManager attM;
    protected RoomManager roomM;

    public Attendeesystemhandler(AccountManager accM, EventManager TalkM, MessageManager MsgM, AttendeeUI attUI,
                            StrategyManager StrategyManager, AttendeeManager attM, RoomManager roomM) {
        this.accM = accM;
        this.eventManager = TalkM;
        this.MsgM = MsgM;
        this.attUI = attUI;
        this.strategyM = StrategyManager;
        this.attM = attM;
        this.roomM = roomM;

    }

    ///////////CHOOSING METHOD/////////

    public int chooseMode1(){    //For Main Dashboard.
        ArrayList<Integer> validChoices = new ArrayList<>(Arrays.asList(1, 2, 3));
        String userInput;
        int mode = -1;
        boolean valid = false;
        while(!valid){
            userInput = attUI.getrequest(1);
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else {
                valid = true;
                mode = Integer.parseInt(userInput);}
        }
        return mode;
    }

    public int chooseMode2(){    //For MsgDashboard.
        ArrayList<Integer> validChoices = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        String userInput;
        int mode = -1;
        boolean valid = false;
        while(!valid){
            userInput = attUI.getrequest(1);
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else {
                valid = true;
                mode = Integer.parseInt(userInput);}
        }
        return mode;
    }

    public int chooseMode3(){    //For SignupDashboard.
        ArrayList<Integer> validChoices = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        String userInput;
        int mode = -1;
        boolean valid = false;
        while(!valid){
            userInput = attUI.getrequest(1);
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else {
                valid = true;
                mode = Integer.parseInt(userInput);}
        }
        return mode;
    }

    public int chooseMode4(){    //For EventDashboard.
        ArrayList<Integer> validChoices = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        String userInput;
        int mode = -1;
        boolean valid = false;
        while(!valid){
            userInput = attUI.getrequest(1);
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else {
                valid = true;
                mode = Integer.parseInt(userInput);}
        }
        return mode;
    }

    ///////////////HELPER FOR SIGN UP EVENT////////////////
    public void readAllAvailableTalks(int type){
        StringBuilder a = new StringBuilder("Available Event: ");
        ArrayList<Integer> availableTalksId = getAllAvailableTalks(type);
        for(Integer t:availableTalksId){
            String roomName = roomM.getRoomName(eventManager.getRoomIdWithId(t));
            a.append(eventManager.gettalkinfoWithName(t, roomName));
        }
        attUI.show(a.toString());}



    /**
     * Get all available events that this account can attend.
     * @return A tlkList containing all events this account can attend.
     */
    private ArrayList<Integer> getAllAvailableTalks(int a){
        ArrayList<Integer> myTalksId = attM.getAllMyTalksId();
        ArrayList<Integer> allTalksId = eventManager.getListOfEventsByType(a);
        ArrayList<Integer> result = new ArrayList<>();
        for(Integer t:allTalksId){
            if (!myTalksId.contains(t) && (eventManager.getRemainingSeats() > 0 )) result.add(t);
            // Boolean is_VIP = false;
            // if (accM.isVIPAcc(accM.getCurrAccountId())) is_VIP = true;
            // TODO: add a checker for VIP event, waiting for event flag var update.
            // if event t is vip, remove t from result if cur_acc is not a VIP.
        }
        return result;
    }

    public int targetTalksSignUp(int type){
        ArrayList<Integer> validChoices = getAllAvailableTalks(type);
        validChoices.add(-1);
        String userInput;
        int mode = -1;
        boolean valid = false;
        while(!valid){
            userInput = attUI.getrequest(3);
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else {
                valid = true;
                mode = Integer.parseInt(userInput);}
        }
        return mode;}

    //////////////CANCEL TALK////////////////

    public void readAllMyTalks(){
        StringBuilder a = new StringBuilder("My signed up talks:");
        ArrayList<Integer> allTalks = attM.getAllMyTalksId();
        for(Integer t:allTalks){
            String roomName = roomM.getRoomName(eventManager.getRoomIdWithId(t));
            a.append(eventManager.gettalkinfoWithName(t, roomName));}
        attUI.show(a.toString());
    }

    public int targetTalksCancel(){
        ArrayList<Integer> validChoices = attM.getAllMyTalksId();
        validChoices.add(-1);
        String userInput;
        int mode = -1;
        boolean valid = false;
        while(!valid){
            userInput = attUI.getrequest(4);
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else {
                valid = true;
                mode = Integer.parseInt(userInput);}
        }
        return mode;}

    ///////////////MESSAGING SYSTEM////////////////

    public void readAllAttendees(){
        ArrayList<Integer> att = getAllAttendees();
        StringBuilder a = new StringBuilder("These are the attendees who attend your signed up talks. Choose an id to message:\n");
        for(Integer i : att) {
            a.append(accM.getinfoacc(i));
        }
        attUI.show(a.toString());
    }

    public void readAllSpeakers(){
        ArrayList<Integer> allTalks = attM.getAllMyTalksId();
        StringBuilder a = new StringBuilder("These are the speakers in talks you attend. Choose an id to message:\n");
        for (Integer t: allTalks){
            ArrayList<Integer> spkLst = new ArrayList<>(eventManager.getSpeakerIDIn(t));
            for(int speaker:spkLst){
                String each = "(" + eventManager.getTitle(t) + ")" + accM.getinfoacc(speaker);
                a.append(each);
            }
        }
        attUI.show(a.toString());
    }


    public int targetgetter(){
        ArrayList<Integer> validChoices = getAllAttendees();
        validChoices.addAll(getAllSpeakers());
        validChoices.add(-1);
        String userInput;
        boolean valid = false;
        do{
            userInput = attUI.getrequest(2);
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else { valid = true; }
        }while(!valid);
        return Integer.parseInt(userInput);
    }


    private ArrayList<Integer> getAllAttendees() {
        ArrayList<Integer> talkList = attM.getAllMyTalksId();
        ArrayList<Integer> result = eventManager.getallattendee(talkList);
        int currAcc = accM.getCurrAccountId();
        if (result.contains(currAcc)) result.remove(Integer.valueOf(currAcc));
        return result;


    }

    private ArrayList<Integer> getAllSpeakers() {
        ArrayList<Integer> talkList = attM.getAllMyTalksId();
        return eventManager.getAllSpeakers(talkList);
    }

    public String enterTxt(){
        StringBuilder a = new StringBuilder();
        boolean exit = false;
        attUI.informEnteringText();
        do{
            String line = attUI.getLineTxt();
            if (line.equals("end")) exit = true;
            else{
                a.append(line);
                a.append("\n");
            }
        } while(!exit);
        return a.toString();
    }

    ////////////////////MODIFY LATER///////////
    public int targetmsg(){
        ArrayList<Integer> validChoices = attM.getInbox();
        validChoices.add(-1);
        String userInput;
        boolean valid = false;
        do{
            userInput = attUI.getrequest(2);
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else { valid = true; }
        }while(!valid);
        return Integer.parseInt(userInput);
    }
}
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
    protected RequestManager reM;

    public Attendeesystemhandler(AccountManager accM, EventManager TalkM, MessageManager MsgM, AttendeeUI attUI,
                            StrategyManager StrategyManager, AttendeeManager attM, RoomManager roomM, RequestManager reM) {
        this.accM = accM;
        this.eventManager = TalkM;
        this.MsgM = MsgM;
        this.attUI = attUI;
        this.strategyM = StrategyManager;
        this.attM = attM;
        this.roomM = roomM;
        this.reM = reM;

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
        ArrayList<Integer> validChoices = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
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
            a.append(eventManager.getEventinfoWithName(t, roomName));
        }
        attUI.show(a.toString());}


    public void readalltalkssimp(){
        StringBuilder a = new StringBuilder("Event Information with id:");
        ArrayList<Integer> alltalks = attM.getAllMyTalksId();
        for(Integer t:alltalks){
            a.append(eventManager.getEventinfosimp(t));}
        attUI.show(a.toString());
    }

    public int targettalks(){
        ArrayList<Integer> validChoices = attM.getAllMyTalksId();
        validChoices.add(-1);
        boolean valid = false;
        String userInput;
        do{
            userInput = attUI.getrequest2();
            if (!strategyM.isValidChoice(userInput, validChoices))
                attUI.informinvalidchoice();
            else { valid = true; }
        } while(!valid);
        return Integer.parseInt(userInput);
    }

    /**
     * Get all available events that this account can attend.
     * @return A tlkList containing all events this account can attend.
     */
    private ArrayList<Integer> getAllAvailableTalks(int a) {
        boolean is_VIP = false;
        if (accM.isVIPAcc(accM.getCurrAccountId())) is_VIP = true;
        ArrayList<Integer> myTalksId = attM.getAllMyTalksId();
        ArrayList<Integer> allTalksId = eventManager.getListOfEventsByType(a);
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer t : allTalksId) {
            if (!myTalksId.contains(t) && (eventManager.getRemainingSeats() > 0)) result.add(t);

        }

        if (!is_VIP) {
            for (Integer i : result) {
                if (eventManager.checkVIP(i)) result.remove(Integer.valueOf(i));
            }
        }return result;
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

    public void requestfortalk(String a, int b) {
        if (b != -1) {reM.createRequest(a, accM.getCurrAccountId(), b);}
        attUI.stoprequest();
    }

    //////////////CANCEL TALK////////////////

    public void readAllMyTalks(){
        StringBuilder a = new StringBuilder("My signed up events:");
        ArrayList<Integer> allTalks = attM.getAllMyTalksId();
        for(Integer t:allTalks){
            String roomName = roomM.getRoomName(eventManager.getRoomIdWithId(t));
            a.append(eventManager.getEventinfoWithName(t, roomName));}
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


    public void messageToSp(String a, int speakerId) {
        int msg = MsgM.createmessage(accM.getCurrAccountName(), accM.getCurrAccountId(), speakerId, a);
        accM.addinbox(speakerId, msg);
        accM.addsend(accM.getCurrAccountId(), msg);
        attUI.messagesend();
    }

    public void messageToAtt(String a, int getterId) {

        int msg = MsgM.createmessage(accM.getCurrAccountName(), accM.getCurrAccountId(), getterId, a);
        accM.addinbox(getterId, msg);
        accM.addsend(accM.getCurrAccountId(), msg);
        attUI.messagesend();
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
///////Grey modify
    private ArrayList<Integer> getAllUnread() {
        ArrayList<Integer> inbox = attM.getInbox();
        ArrayList<Integer> unread = new ArrayList<>();
        for (Integer i : inbox) {
            boolean is_Read = MsgM.checkMessageStatus(i);
            if (!is_Read) {
            unread.add(i);
            accM.addUnread(attM.getCurrAccountId(), i);
        }
    }

    return unread;
}



    protected void readAllUnreadMsg(){
        readAllUnread();
        attUI.annouceUnread();

    }

    private void readAllUnread(){

        String all = MsgM.formatAllUnread(getAllUnread());
        attUI.show(all);
    }

    protected int targetunread(){
        ArrayList<Integer> validChoices = attM.getUnreadInbox();
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

    protected ArrayList<Integer> getChoiceList(int size){
        int i = 0;
        int j = 1;
        ArrayList<Integer> choiceList = new ArrayList<>();
        while (i!=size){
            choiceList.add(j);
            j++;
            i++;
        }
        return choiceList;
    }

    protected void askToAchieve(int msgId){
        int userInput = attUI.chooseOption(getChoiceList(3), "Would you like to:" +
                "\n1 -> Mark as Unread" +
                "\n2 -> Move to Archive" +
                "\n3 -> Delete Message", "Invalid Chooice, Please Try Again:");
        if(userInput == 1){
            attUI.annouceMarkUnread();
        } else if(userInput == 2){
            accM.removeMessage(msgId);
            accM.archiveMessage(msgId);
            attUI.archiveMsg();
        }else if(userInput == 3){
            accM.removeMessage(msgId);
            MsgM.removeMessage(msgId);
            attUI.deleteMsg();
        }
        attUI.askForBack();}

    protected void signUpMyNewTalks(int a){
        int input;
        do{
            attUI.signUpTalk(a);
            readAllAvailableTalks(a);
            input = targetTalksSignUp(a);
            if (input != -1){
                if(eventManager.checkVIP(input)){
                    attUI.signUpVipTalk();
                    if(attM.getCurrAttendee().getUserType() == 3){
                        attM.enrol(input);
                        eventManager.addAttendeev2(input, attM.getCurrAttendee());
                        attUI.signUpSuc();
                    }else{attUI.informNotVip();}
                }else if(!eventManager.checkVIP(input)){
                    attM.enrol(input);
                    eventManager.addAttendeev2(input, attM.getCurrAttendee());
                    attUI.signUpSuc();
                }
            }
        }while(input != -1);
    }

    protected void readAllMsg() {

        int messageID;
        ArrayList<Integer> inbox = attM.getInbox();

        if (inbox.size() != 0) {
            do {
                String a = MsgM.formatmsgget(attM.getInbox());
                attUI.show(a);
                messageID = targetmsg();
                if (messageID != -1) {
                    attUI.show(MsgM.getString(messageID));
                    askToAchieve(messageID);
                }
            } while (messageID != -1);
        } else {
            attUI.announceEmptyInbox();
            attUI.askForBack();
        }
    }


    protected void allUnreadMsg(){
        int tmsgid;
        do{
            readAllUnreadMsg();
            tmsgid = targetunread();
            if(tmsgid != -1){
                MsgM.readMessage(tmsgid);
                attM.deleteUnreadInbox(tmsgid);
                attUI.unreadSuccess(tmsgid);
                attUI.askForBack();
            }

        }while(tmsgid != -1);
    }
}
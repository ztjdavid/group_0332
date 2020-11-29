package Gateways;

import UseCase.*;
import org.ini4j.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class UserFileGateway {
    Ini iniFile;
    AccountManager accM;
    AttendeeManager attM = new AttendeeManager();
    SpeakerManager spkM = new SpeakerManager();
    OrganizerManager orgM = new OrganizerManager();
    public UserFileGateway(String pathname, AccountManager accM) throws IOException{
        this.iniFile = new Ini(new File(pathname));
        this.accM = accM;
    }

    public void writeData() throws IOException{
        iniFile.put("0", "Name", "Eric");
        iniFile.put("0", "Password", "111111");
        iniFile.store();
    }

    public void loadData() throws Exception{
        Set<String> idSet = iniFile.keySet(); // get id of all accounts
        for (String id: idSet){
            int userType = iniFile.get(id, "Type", int.class);
            String userName = iniFile.get(id, "Username");
            String passW = iniFile.get(id, "Password");
            ArrayList<Integer> inBox = listDecoder(iniFile.get(id, "Inbox"));
            ArrayList<Integer> sentBox = listDecoder(iniFile.get(id, "SentBox"));
            switch (userType){
                case 1:
                    loadAttendee(id, userName, passW, inBox, sentBox);
                    break;
                case 2:
                    loadSpeaker(id, userName, passW, inBox, sentBox);
                    break;
                case 0:
                    loadOrganizer(id, userName, passW, inBox, sentBox);
                    break;
                default:
                    break;
            }
        }
    }

    private ArrayList<Integer> listDecoder(String str) throws NumberFormatException{
        ArrayList<Integer> result = new ArrayList<>();
        if (str == null) return result;

        String[] strArray = str.split(",");
        for (String s: strArray) result.add(Integer.parseInt(s));
        return result;
    }

    private void loadAttendee(String id, String userName, String passW, ArrayList<Integer> inbox,
                              ArrayList<Integer> sentBox) throws NumberFormatException{
        int ID = Integer.parseInt(id);
        ArrayList<Integer> eventList = listDecoder(iniFile.get(id, "EventList"));
        accM.createAccount(userName, passW, 1);
        attM.setAccInfo(ID, inbox, sentBox, eventList);
    }

    private void loadSpeaker(String id, String userName, String passW, ArrayList<Integer> inbox,
                              ArrayList<Integer> sentBox) throws NumberFormatException{
        int ID = Integer.parseInt(id);
        ArrayList<Integer> eventList = listDecoder(iniFile.get(id, "EventList"));
        accM.createAccount(userName, passW, 2);
        spkM.setAccInfo(ID, inbox, sentBox, eventList);
    }

    private void loadOrganizer(String id, String userName, String passW, ArrayList<Integer> inbox,
                              ArrayList<Integer> sentBox) throws NumberFormatException{
        int ID = Integer.parseInt(id);
        accM.createAccount(userName, passW, 0);
        orgM.setAccInfo(ID, inbox, sentBox);
    }




}

package UseCase.IGateWay;

import java.io.IOException;
import java.util.ArrayList;

public interface IUserGateWay {
    public void writeNewAcc(int id, String username, String password, int userType)throws IOException;
    public void updateInbox(int id, ArrayList<Integer> inbox)throws IOException;
    public void updateSentBox(int id, ArrayList<Integer> sentBox)throws IOException;
    public void updateEventList(int id, ArrayList<Integer> eList)throws IOException;
}
package UseCase;
import Entity.*;

/**
 * The SpeakerManager class implements all functionalities of a speaker.
 */
public class SpeakerManager extends AccountManager{
    public SpeakerManager(){
        super();
    }

    /**
     * Create a Speaker account.
     * @param username The username of the new account.
     * @param password The password of the new account.
     * @return True iff the new account is successfully created.
     */
    @Override
    public boolean createAccount(String username, String password) {
        if (duplicatedUsername(username)) return false;
        Speaker newAcc = new Speaker(username, password, 2);
        this.accountList.put(TotalNumOfAccount, newAcc);
        TotalNumOfAccount += 1;
        return true;
    }

    /**
     * Check if the current login account can message a given account.
     * @param other Another account that the current login account is going to message.
     * @return True iff the current login account can message the given account.
     */
    @Override
    public boolean messageable(Account other){
        return other.getUserType() == 1;
    }

}
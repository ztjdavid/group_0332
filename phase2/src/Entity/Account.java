package Entity;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The Account class is a class as the superclass of all account classes.<br>
 * It implements methods and stores basic info that all account classes have in common.
 */
public abstract class Account {
    protected String username;
    protected String password;
    protected final int userId;
    protected ArrayList<Integer> sentMessage;
    protected ArrayList<Integer> inbox;
    protected ArrayList<Integer> unreadInbox;
    protected Integer application;


    public Account(String username, String password, int userId){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.sentMessage = new ArrayList<>();
        this.inbox = new ArrayList<>();
        this.unreadInbox = new ArrayList<>();
        this.application = -1;
    }

    public void setApplication(int application){this.application = application;}

    public int getapplication(){return this.application;}


    /**
     * Set the username for this account.
     * @param username The new username.
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Get the username of this account.
     * @return The username of this account.
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Set the password for this account.
     * @param password Set password for this account.
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * Get the password of this account.
     * @return The password of this account.
     */
    public String getPassword(){
        return this.password;
    }

    /**
     * Get the UID of this account.
     * @return the userid of this account.
     */
    public int getUserId(){
        return this.userId;
    }

    /**
     * Get the user type of this account
     * @return an integer indicating the user type.<br>
     *   0 - Organizer <br>
     *   1 - Attendee <br>
     *   2 - Speaker <br>
     */
    public abstract int getUserType();

    /**
     * Get all message IDs sent by this account.
     * @return A copy of SentMessage containing message IDs.
     */
    public ArrayList<Integer> getSentMessage(){
        return new ArrayList<>(this.sentMessage);
    }

    /**
     * Add new message sent by this account.
     * <p>
     * <b>NOTICE: This method is not responsible for checking correctness of the input.</b>
     * @param mesID ID of new message sent by this account.
     */
    public void addSentMessage(int mesID){
        this.sentMessage.add(mesID);
    }

    /**
     * Get all message IDs received by this account.
     * @return A copy of inbox containing message IDs.
     */
    public ArrayList<Integer> getInbox(){
        return new ArrayList<>(this.inbox);
    }

    /**
     * Add new message received by this account.
     * <p>
     * <b>NOTICE: This method is not responsible for checking correctness of the input.</b>
     * @param mesID ID of new message received by this account.
     */
    public void addInbox(int mesID){
        this.inbox.add(mesID);
    }

    public void setInbox(ArrayList<Integer> inbox){ this.inbox = inbox;}

    public void setSentBox(ArrayList<Integer> sentBox) { this.sentMessage = sentBox;}

    ///// Louisa Added
    public ArrayList<Integer> getUnreadInbox(){return new ArrayList<>(this.unreadInbox);}

    public void setUnreadInbox(ArrayList<Integer> unreadInbox){this.unreadInbox = unreadInbox;}

    public void addUnreadInbox(int unreadId){this.unreadInbox.add(unreadId);}
    /////

    public void removeMessage(int messageID){
        this.unreadInbox.remove(messageID);
    }

    public void archiveMessage(int messageID){
        this.inbox.add(messageID);
    }

    public ArrayList<Integer> archiveMessage(){
        return new ArrayList<>(this.inbox);
    }

}

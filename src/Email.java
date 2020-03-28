
public class Email {


    private boolean isNew = true;  // λογικη μεταβλητη για το αν ειναι καινουριο μαιλ
    private String sender;  // αποστολεας
    private String receiver; // παραληπτης
    private String subject; // θεμα
    private String mainbody; // περιεχομενο

    /** Κονστρακτορας νεου Μειλ
     * @param sender αποστολεας
     * @param receiver παραληπτης
     * @param subject θεμα
     * @param mainbody περιεχομενο
     */
    public Email (String sender , String receiver , String subject , String mainbody){
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.mainbody = mainbody;
    }

    // getters για τα πεδια του

    public String getMainbody() {
        return mainbody;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }
    public boolean isNew(){
        return isNew;
    }

    // συνάρτηση που ενημερώνει το email για το αν έχει ήδη διαβαστεί από τον χρήστη
    public void hasBeenRead(){
        isNew =false;
    }
}

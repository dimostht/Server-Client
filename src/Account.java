import java.util.ArrayList;
import java.util.List;

//κλάση που αντιπροσωπεύει έναν χρήστη του συστήματος
public class Account {
    // ονομα χρήστη , κωδικος, λιστα με τα μαιλς του
    private String username;
    private String password;
    private List<Email> mailbox = new ArrayList<>();

    /** Κονστρακτορας νεου χρήστη
     * @param username ονομα χρηστη
     * @param password κωδικος
     * @param mailbox λιστα με τα μαιλς του
     */
    public Account(String username, String password,List<Email> mailbox) {
        this.mailbox = mailbox;
        this.username = username;
        this.password = password;
    }

    //getters για τα πεδια του
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Email> getMailbox() {
        return mailbox;
    }

}

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// Κλάση για τη διαχείρηση του πελάτη
// @author Dimosthenis Tsormpatzoudis / Δημοσθένης Τσορμπατζούδης ΑΕΜ : 3099

class ClientHandler extends Thread
{
    // socket και ρευματα δεδομενων προς και απο τον client-server
    final Socket s;
    DataInputStream in;
    DataOutputStream out;


    /** Κονστρακτορας για την δημιουργεία νέου διαχειριστή πελάτη
     * @param s socket
     * @param in ρευμα εισερχόμενω δεδομένων
     * @param out ρευμα εξερχομενων δεδομένων
     */
    public ClientHandler(Socket s,DataInputStream in, DataOutputStream out)
    {
        this.s = s;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        // ο αριθμος χρηστών ειναι 2
        int n = 2;
        // λιστα με τις λιστες των emails για καθε χρήστη
        List<Email>[] emails = new ArrayList[n];

        for(int i=0;i<n;i++){
            emails[i] = new ArrayList<Email>();
        }

        //Λιστα emails του χρήστη thomas@csd.auth.gr
        emails[0].add(new Email("dimos@csd.auth.gr","thomas@csd.auth.gr","καφές","Θα πάμε για κανα καφέ;"));
        emails[0].add(new Email("mask@csd.auth.gr","thomas@csd.auth.gr","εργασία","πως παει η εργασία στα δίκτυα;"));
        emails[0].add(new Email("kattis@gmail.gr","thomas@csd.auth.gr","εισητηρια","εκλεισες εισητηρια για Κρήτη;"));

        //Λιστα emails του χρήστη mask@csd.auth.gr
        emails[1].add(new Email("dimos@csd.auth.gr","mask@csd.auth.gr","βιβλιοθηκη","στις 8 παμε βιβλιοθηκη"));
        emails[1].add(new Email("thomas@csd.auth.gr","mask@csd.auth.gr","σημερα","σημερα τι θα κανεις;"));
        emails[1].add(new Email("kattis@gmail.gr","mask@csd.auth.gr","κρητη","τελικα θα ερθεις στις αποκριες κρητη;"));



        // Λίστα χρηστών
        List<Account> users = new ArrayList<>();
        users.add(new Account("thomas@csd.auth.gr","1234",emails[0]));
        users.add(new Account("mask@csd.auth.gr","6584",emails[1]));

        // εισερχόμενα δεδομένα
        String received;

        // ετερμων βρόχος επικοινωνίας ωσπου να τερματιστει χειροκίνητα
        while (true){

            try{
                //εμφανιση αρχικού μενου
                out.writeUTF("----------\nMailServer:\n----------\nHello, you connected as a guest.\n"
                + "==========\n> LogIn \n> SignIn \n> Exit \n==========");

                // απαντηση client
                received = in.readUTF();

                // εξοδος απο το συστημα
                if(received.equals("exit") || received.equals("Exit")){
                    System.out.println("Client "+this.s+" exits");
                    this.s.close();
                    break;
                }
                // συνδεση χρήστη
                else if(received.equals("login") || received.equals("LogIn")){

                    // λογικη μεταβλητη για το αν συνδεθηκε ο χρήστης
                    boolean access = false;

                    // ζηταμε απο τον χρήστη τα στοιχεία του , αμα δεν ολοκληρωθει η συνδεση ξανα δοκιμαζουμε
                    do {
                        out.writeUTF("Type your username");
                        String username = in.readUTF();
                        out.writeUTF("Type your password");
                        String password = in.readUTF();

                        // ελεγχουμε για το αν αντιστοιχεί σε καποιον χρήστη απο αυτούς που ειναι αποθηκευμένοι
                        for (Account user : users) {
                            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                                access = true;
                                break;
                            }
                        }

                        // εμφανιζουμε κατάλληλο μύνημα για την καθε περίπτωση
                        if (access) {
                            runLogin(username,users,emails);
                        } else {
                            out.writeUTF("Wrong username or password");
                        }
                        // σε περιπτωση που δεν συνδεθει τοτε ξανα δοκιμαζει
                    }while (!access);


                }
                else if(received.equals("signin") || received.equals("SignIn")) {

                    // λογικη μεταβλητη για το αν υπαρχει ξανα το ονομα χρήστη που επελεξε ο χρήστης
                    boolean exist = false;

                    do {
                        // μήνυμα για να δώσει το όνομα χρήστη
                        out.writeUTF("Type your username");
                        String username = in.readUTF();
                        String password ="";
                        // μήνυμα για να δώσει το κωδικο χρήστη
                        out.writeUTF("Type your password");
                        password = in.readUTF();


                        //ελεγχουμε για το αν το ονομα χρήστη υπαρχει ξανά
                        for (Account user : users) {
                            if (user.getUsername().equals(username)) {
                                exist = true;
                                break;
                            }
                        }
                        if(exist) {
                            // σε περιπτωση που το ονομα χρήστη ειναι πιασμένο ενημερώνουμε τον χρήστη να επιλέξει καποιο αλλο
                            out.writeUTF("This username already exists, Please chose an another one");
                        }else{
                            //προσθηκη του χρήστη στη λιστα των χρηστων, προφανως ένας νεος χρήστης δεν εχει αλληλογραφία
                            users.add(new Account(username,password,null));
                            runLogin(username,users,emails);
                        }

                    }while (exist);
                }

            }catch (IOException e){
                e.printStackTrace();
                break;
            }
        }
    }

    /** Συνάρτηση που εμφανίζει το εσωτερικο μενου του προγραμματος, γίνεται σε διαφορετική συνάρτηση για να μπορεί να κληθεί
     *  απο διαφορετικα σημεία όπως όταν ένας χρήστης κανει login αλλα και στη δημιουργία νέο χρήστη
     *
     * @param username όνομα χρήστη
     * @param users λίστα με τα ονόματα των χρήστων
     * @param emails λίστα με τις λίστες των emails
     */
    public void runLogin(String username,List<Account> users, List<Email>[] emails) {

        // εισερχόμενα δεδομένα
        String received;
        // ετερμων βρόχος επικοινωνίας ωσπου να τερματιστει χειροκίνητα
        while (true){

            try{
                //εμφανιση εσωτερικου μενου
                out.writeUTF("Welcome\n----------\n> New Email:\n----------\n> Show Emails\n----------\n> Read Emails\n"
                        + "----------\n> Delete Emails\n----------\n> LogOut \n----------\n> Exit");

                // απαντηση client
                received = in.readUTF();

                // συγγραφη νέου email από τον χρήστη
                if(received.equals("New Email") || received.equals("new email")){

                    boolean validReceiver = false;
                    String receiver;

                    // ζηταμε από τον χρήστη να το όνομα του παραλήπτη
                    // σε περιπτωση που δεν ειναι εγκυρο τοτε επαναλαμάνεται η διαδικάσια μεχρι να δωθει εγκυρο
                    do {
                        out.writeUTF("Type the username of the  receiver");
                        receiver = in.readUTF();

                        for(Account user : users){
                            if(receiver.equals(user)){
                                validReceiver = true;
                            }
                        }
                        if(!validReceiver){
                            out.writeUTF("Not valid username of the receiver, Please try again");
                        }
                    }while (!validReceiver);

                    out.writeUTF("Type the subject");
                    String subject = in.readUTF();
                    out.writeUTF("Type the mainbody");
                    String mainbody = in.readUTF();

                    Email email = new Email(username,receiver,subject,mainbody);

                    //λίστα με τα emails
                    for( List<Email> emails2 : emails){
                        // email απο την κάθε λιστα
                        for( Email email2 : emails2){
                            // ελέχγουμε για το αν ο παραληπτης είναι το όνομα που έδωσε ο χρήστης
                            // αν ειναι προσθέτουμε το email στην αλληλογραφλια του
                            if (email2.getReceiver().equals(receiver)){
                                emails2.add(email);
                            }
                        }
                    }
                    out.writeUTF("Email sent!");

                    break;
                }
                // εμφανιση emails χρήστη
                else if(received.equals("Show Emails") || received.equals("show emails")){

                    String output = "";

                    //λίστα με τα emails
                    for( List<Email> emails2 : emails){
                        // email απο την κάθε λιστα
                        for( Email email : emails2){
                            // ελέχγουμε για το αν ο παραληπτης είναι ο χρήστης που ειναι συνδεδεμένος
                            // αν ειναι εμφανιζουμε το μυνημα οτι έχει email και τον αποστολέα του
                            if (email.getReceiver().equals(username)){
                                //ενημέρωση τον χρήστη για το αν έχει ανοιχθεί το συγκεκριμένο email
                                if (email.isNew()){
                                    output = output + "[ New ] ";
                                }
                                output = output+"You have an email from "+email.getSender()+"\n";
                            }
                        }
                    }
                    out.writeUTF(output);
                    break;
                }
                // διαβασμα emails
                else if(received.equals("Read Emails") || received.equals("read emails")) {

                    String output = "";
                    //λίστα με τα emails
                    for( List<Email> emails2 : emails){
                        // email απο την κάθε λιστα
                        for( Email email : emails2){
                            // ελέχγουμε για το αν ο παραληπτης είναι ο χρήστης που ειναι συνδεδεμένος
                            // αν ειναι εμφανίζουμε το email με όλα του τα στοιχεία
                            if (email.getReceiver().equals(username)){
                                output = output+"Sender :  "+email.getSender()+"\n";
                                output = output + "Subject : "+email.getSubject()+"\n";
                                output = output + email.getMainbody()+"\n"+"----------\n";
                                // ενημερώνουμε τα στοιχεία του email ότι έχει διαβαστεί από τον χρήστη
                                email.hasBeenRead();
                            }
                        }
                    }
                    out.writeUTF(output);
                    break;

                }
                // διαγραφη email
                else if(received.equals("Delete Emails") || received.equals("delete emails")) {

                    String output = "";
                    // μετρητης για τα emails που δέχτηκε ο χρήστης
                    int count = 1;

                    //λίστα με τα emails
                    for( List<Email> emails2 : emails){
                        // email απο την κάθε λιστα
                        for( Email email : emails2){
                            // ελέχγουμε για το αν ο παραληπτης είναι ο χρήστης που ειναι συνδεδεμένος
                            // αν ειναι εμφανίζουμε το email μαζι με τον αριθμό του
                            if (email.getReceiver().equals(username)){
                                output = output + "Email number "+count+"\n";
                                output = output+"Sender :  "+email.getSender()+"\n";
                                output = output + "Subject : "+email.getSubject()+"\n";
                                output = output + email.getMainbody()+"\n"+"----------\n";

                                //αυξηση μετρητη
                                count++;
                            }
                        }
                    }
                    int number =1;
                    // βρογχος εως να δωσει ο χρήστης δεκτη απαντηση
                    do{
                        output = output + "Which email do you want to delete? insert it's number \n";
                        out.writeUTF(output);
                        number = Integer.parseInt( in.readUTF());
                    }while (number < 1 || number >= count);

                    // μεταβλητη για το email που θελουμε να διαγραψουμε
                    Email emailToDelete = null;

                    //λίστα με τα emails
                    for( List<Email> emails2 : emails){
                        // email απο την κάθε λιστα
                        for( Email email : emails2){
                            // ελέχγουμε για το αν ο παραληπτης είναι ο χρήστης που ειναι συνδεδεμένος
                            if (email.getReceiver().equals(username)){

                                if(count == number){
                                    // αποθηκευουμε το email που θελουμε να διγραψουμε σε μια μεταβλητη
                                    emailToDelete = email;
                                    break;
                                }
                                //αυξηση μετρητη
                                count++;
                            }
                        }
                    }
                    //λίστα με τα emails
                    for( List<Email> emails2 : emails) {
                        // αφαιρουμε το email που επελεξε ο χρήστης απο την λίστα που βρίσκεται
                        emails2.remove(emailToDelete);
                    }
                    out.writeUTF("Email deleted");
                    break;

                }
                // σε περιπτωση που ο χρήστης θελει να αποσυνδεθει τοτε απλα τρέχουμε την εντολη run() για να
                // επιστρεψει στο αρχικο μενου
                else if(received.equals("LogOut") || received.equals("logout")) {
                    run();
                }
                // εξοδος του χρήστη απο το σύστημα
                else if(received.equals("Exit") || received.equals("exit")) {
                    System.out.println("Client "+this.s+" exits");
                    this.s.close();
                    break;
                }

            }catch (IOException e){
                e.printStackTrace();
                break;
            }
        }
    }
}



















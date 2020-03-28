import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


// Κλάση για τον server
// @author Dimosthenis Tsormpatzoudis / Δημοσθένης Τσορμπατζούδης ΑΕΜ : 3099

public class MailServer
{
    public static void main(String[] args) throws IOException
    {

        // ο server περιμένει αιτημα στην θύρα 5056
        ServerSocket ss = new ServerSocket(5056);

        // τρέχουμε τον ατέρμων βρόχο περιμένοντας το αίτημα του client
        while (true)
        {
            Socket s = null;

            try
            {
                // το socket για να δεχτεί το αιτημα του client
                s = ss.accept();

                // τα ρευματα μεταφοράς δεδομέων
                DataInputStream in = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());

                // επιλέγουμε ένα thread για να εξηπηρετήσει τον τρέχον πελάτη
                Thread t = new ClientHandler(s,in,out);

                // και το ξεκινάμε
                t.start();

            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }
}


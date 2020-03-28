import java.io.*;
import java.net.*;
import java.util.Scanner;

// Κλάση για τον client
// @author Dimosthenis Tsormpatzoudis / Δημοσθένης Τσορμπατζούδης ΑΕΜ : 3099
public class MailClient
{
    public static void main(String[] args) throws IOException
    {
        try
        {

            Scanner scn = new Scanner(System.in);

            // η τοπική IP
            InetAddress ip = InetAddress.getByName("localhost");

            // θέτουμε την θύρα επικοινωνίας με 5056
            Socket s = new Socket(ip, 5056);

            // οι ροες δεδομένων In kai Out
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            // βροχος για την ανταλλαγη πληροφοριων του client με τον server
            while (true)
            {
                // διαβασε τον μύνημα του server
                System.out.println(in.readUTF());
                // αποθηκευσε το
                String tosend = scn.nextLine();
                // εμφανισε το
                out.writeUTF(tosend);



                // ο client μπορει να επιλεξει να βγει, αλλιως συνεχιζεται η επικοινωνια
                if(tosend.equals("Exit") || tosend.equals("exit"))
                {
                    System.out.println("Closing this connection : " + s);
                    s.close();
                    System.out.println("Connection closed");
                    break;
                }

            }

            // κλεισιμο συνδεσεων
            scn.close();
            in.close();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
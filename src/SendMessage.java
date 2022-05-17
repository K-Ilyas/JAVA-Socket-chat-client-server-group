import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SendMessage implements Runnable {

    Socket soc = null;
    UserInformation user = null;
    DataOutputStream out = null;
    ObjectOutputStream bos = null;

    public SendMessage(DataOutputStream out, ObjectOutputStream bos, UserInformation user) {
        this.user = user;
        this.out = out;
        this.bos = bos;
    }

    public void run() {

        String pseudo = "", name = "";
        Scanner sc = new Scanner(System.in);
        try {

            do {

                System.out.println("Entre le pseudo de l'utilisateur");
                pseudo = sc.nextLine();
                out.writeUTF(pseudo.toString());
                if (!pseudo.toUpperCase().equals("N")) {
                    System.out.println("Entre le message  ");

                    name = sc.nextLine();
                    out.writeUTF(name.toString());
                }
                out.flush();

                bos.writeObject(user);
                bos.flush();
                bos.flush();
                
            } while (!pseudo.toUpperCase().equals("N"));

            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

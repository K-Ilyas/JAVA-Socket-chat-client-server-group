import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ReciveMessage implements Runnable {

    Socket soc = null;
    UserInformation user = null;

    public ReciveMessage(Socket soc, UserInformation user) {
        this.soc = soc;
        this.user = user;
    }

    public void run() {
        String sender = "", time = "", data = "";
        int header = 1;

        try

        {
            InputStream in = this.soc.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            do {

                System.out.print("[" + user.getPseudo() + "] : Waiting....");
                header = dis.readInt();

                if (header == 1) {
                    sender = dis.readUTF();
                    time = dis.readUTF();
                    data = dis.readUTF();
                    System.out.println("\n\n{ TIME : [" + time + "] FROM : [" + sender + "]  }");
                    System.out.println("{ MESSAGE: [" + data + "]  }\n\n");
                } else if (header == 2) {
                    System.out.println(dis.readUTF());

                } else {
                    System.out.println(dis.readUTF());
                }

            } while (header != 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

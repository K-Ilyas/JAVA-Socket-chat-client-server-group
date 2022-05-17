
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.concurrent.ExecutorService;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.Map.Entry;

public class SocketServer {

    final static int PORT_CONN = 4040;
    private ServerSocket server = null;
    private boolean isConnected = false;
    private Hashtable<UserInformation, Socket> con_table = null;

    public SocketServer() {
        this.server = null;
        try {
            this.server = new ServerSocket(PORT_CONN);
            this.isConnected = true;
            System.out.println("SERVER : CONNECTED");

            this.con_table = new Hashtable<UserInformation, Socket>();

            // this.con_table.put(new UserInformation("ilyas", "ilyas.99"), new Socket());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean foundLogIn(UserInformation user, Socket soc) {

        if (!this.con_table.isEmpty()) {

            Enumeration<UserInformation> list = this.con_table.keys();

            while (list.hasMoreElements()) {

                if (list.nextElement().compareTo(user) == 0) {
                    this.con_table.put(user, soc);
                    return true;
                }

            }

            return false;
        } else
            return false;
    }

    public Socket foundUser(String pseudo) {

        if (!this.con_table.isEmpty()) {

            Set<Entry<UserInformation, Socket>> list = this.con_table.entrySet();
            Iterator<Entry<UserInformation, Socket>> iterator = list.iterator();
            
            while (iterator.hasNext()) {
                Entry<UserInformation, Socket> user = iterator.next();
                if (user.getKey().getPseudo().equals(pseudo))
                    return user.getValue();
            }
        }
        return null;
    }
    
    public boolean registration(UserInformation user) {

        if (!this.con_table.isEmpty()) {

            Enumeration<UserInformation> list = this.con_table.keys();

            while (list.hasMoreElements()) {

                if (list.nextElement().compareTo(user) == 0) {
                    return true;
                }
            }
            return false;
        } else
            return false;
    }

    public boolean addUser(UserInformation user, Socket soc) {
        if (!registration(user)) {
            this.con_table.put(user, soc);

            System.out.println(" NEW SING IN :  " + "[" + user + "] \t" + soc);

            return true;
        }
        return false;
    }

    public boolean logOut(UserInformation user) {

        if (!this.con_table.isEmpty()) {

            Enumeration<UserInformation> list = this.con_table.keys();

            while (list.hasMoreElements()) {

                if (list.nextElement().compareTo(user) == 0) {
                    this.con_table.put(user, (new Socket()));
                    return true;
                }
            }
            return false;
        } else
            return false;

    }

    public void startConversation() {

        ExecutorService executorService = Executors.newCachedThreadPool();

        if (this.isConnected) {

            Socket soc = null;

            for (;;) {
                try {

                    soc = this.server.accept();
                    executorService.submit(new TraitementServerThread(soc, this));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } else
            System.out.println("THE SERVER IS NOT CONNECTED !!!");

    }

    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        server.startConversation();
    }
}

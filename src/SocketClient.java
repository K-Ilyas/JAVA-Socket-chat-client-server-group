import java.net.InetAddress;
import java.net.Socket;
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

public class SocketClient {

    final static int PORT_CON = 4040;
    private Socket socket = null;
    private Boolean isConnected = false;
    private Scanner scanf = new Scanner(System.in);
    private Boolean isLoged = false;
    private Boolean isOut = false;
    private UserInformation userInformation = null;
    private Thread recive = null;
    private Thread send = null;

    public SocketClient() {

        this.socket = null;
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), PORT_CON);
            this.isConnected = true;
            System.out.println("client : CONNECTED");

            // this.recive.start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void choix() {
        System.out.println("!!-----------------------------!!");
        System.out.println("!!------- POSIBLE CHOICE ------!!");
        System.out.println("!!------- 1 : LOG IN   --------!!");
        System.out.println("!!------- 2 : SING IN  --------!!");

    }

    public int decision() {

        int choix = 0;

        do {
            this.choix();
            choix = scanf.nextInt();
            scanf.nextLine();
            if (choix < 1 || choix > 2)
                System.out.println("Your choice is Rong please retrait");

        } while (choix < 1 || choix > 2);
        return choix;
    }

    public void startConversation() {

        if (this.isConnected) {

            try {
                OutputStream out = this.socket.getOutputStream();
                InputStream in = this.socket.getInputStream();
                // BufferedInputStream bis = new BufferedInputStream(in);
                // BufferedOutputStream bos = new BufferedOutputStream(out);
                DataInputStream dis = new DataInputStream(in);
                DataOutputStream dos = new DataOutputStream(out);
                ObjectOutputStream bos = new ObjectOutputStream(out);

                int choix = 0;
                String stay = "";
                char n = 0;
                String pseudo = "", password = "", passwordConfirme = "";
                int i = 0;
                boolean isTrue = true;

                while (isTrue) {

                    choix = this.decision();

                    switch (choix) {
                        case 1:
                            if (!this.isLoged) {
                                int j = 0;
                                do {
                                    dos.writeInt(1);
                                    dos.flush();
                                    System.out.print("Entre your pseudo :");
                                    pseudo = scanf.nextLine();
                                    System.out.print("Entre your password :");
                                    password = scanf.nextLine();
                                    this.userInformation = new UserInformation(pseudo, password);
                                    bos.writeObject(this.userInformation);
                                    bos.flush();

                                    i = dis.readInt();
                                    System.out.println(dis.readUTF());

                                    if (i == 0)
                                        System.out.println("PLEASE RETRAIT");
                                    else {
                                        this.isLoged = true;
                                        isTrue = false;
                                    }
                                    if (j == 2) {
                                        System.out.println("sorry your expired your time");
                                    }
                                    j++;

                                } while (i == 0 && j < 3);
                            } else
                                System.err.println("Soory you elaready LOG IN");

                            break;

                        case 2:
                            if (!this.isLoged) {

                                do {
                                    dos.writeInt(2);
                                    dos.writeUTF("scascasca");
                                    dos.flush();
                                    System.out.print("Entre your pseudo :");
                                    pseudo = scanf.nextLine();

                                    do {
                                        System.out.print("Entre your password :");
                                        password = scanf.nextLine();
                                        System.out.print("confirme your password :");
                                        passwordConfirme = scanf.nextLine();

                                        if (!password.equals(passwordConfirme))
                                            System.out.println("PASSWORD NOT LIKE PASSWORD CONFIRME PLEASE RETRAIT");
                                        if (password.length() < 5)
                                            System.out.println("sorry your password length must be greather than 5");

                                    } while (password != passwordConfirme && password.length() < 5);

                                    this.userInformation = new UserInformation(pseudo, password);
                                    bos.writeObject(this.userInformation);
                                    bos.flush();
                                    dos.writeUTF("scascasca");
                                    dos.flush();

                                    i = dis.readInt();
                                    System.out.println(dis.readUTF());

                                    if (i == 0)
                                        System.out.println("PLEASE RETRAIT");
                                    else {
                                        this.isLoged = true;
                                        isTrue = false;
                                    }

                                } while (i == 0);
                            } else
                                System.err.println("Soory you elaready LOG IN");

                            break;
                        default:
                            System.err.println("your choice is rong !!");
                            break;
                    }

                }
                this.recive = new Thread(new ReciveMessage(this.socket, this.userInformation));
                this.send = new Thread(new SendMessage(dos,bos,this.userInformation));
                this.send.start();
                this.recive.start();

                // Scanner sc=new Scanner(System.in);
                // dos = new DataOutputStream(out);
                // dos.writeUTF(sc.nextLine());
                // dos.writeUTF(sc.nextLine());
                // dos.flush();
                // bos.writeObject(userInformation);
                // bos.flush();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else
            System.out.println("THE CLIENT IS NOT CONNECTED");

    }

    public static void main(String[] args) {

        SocketClient client = new SocketClient();
        client.startConversation();

    }
}

// break;

// case 3:
// if (this.isLoged) {

// } else
// System.err.println("Sorry you have to log IN FIRST ");

// break;

// default:
// System.err.println("your choice is rong !!");
// break;

// }
// do {
// System.out.println("THE YOU WHANT TO CONTINIOU Y (YES) OR LOG OUT N (NO)
// ??");
// stay = scanf.nextLine().toLowerCase();
// n = stay.charAt(0);
// if (n != 'n' && n != 'y')
// System.out.println("Your choice is Rong please retrait");

// } while (n != 'n' && n != 'y');
// if (n == 'n') {

// if (this.isLoged) {
// i = 0;
// do {
// dos.writeInt(4);
// dos.flush();
// bos.writeObject(this.userInformation);
// bos.flush();
// i = dis.readInt();
// System.out.println(dis.readUTF());
// } while (i == 0);

// if (i != 0) {
// System.out.println("you log out succesfly");
// }

// } else
// System.err.println("SORRY YOU ARE NOT LOG IN ");

// isTrue = false;

// }

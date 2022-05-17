import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class TraitementServerThread implements Runnable {

    private Socket client = null;
    private SocketServer socketServer = null;

    public TraitementServerThread(Socket client, SocketServer socketServer) {
        this.client = client;
        this.socketServer = socketServer;
    }

    public void run() {
        try (OutputStream out = this.client.getOutputStream();
                InputStream in = this.client.getInputStream();

                DataInputStream dis = new DataInputStream(in);
                DataOutputStream dos = new DataOutputStream(out);
                ObjectInputStream ois = new ObjectInputStream(in);) {
            int i = 0;
            boolean isTrue = true;

            UserInformation userInformation = null;

            while (isTrue) {

                i = dis.readInt();

                switch (i) {
                    case 1:
                        System.out.println("SERVER : YOU CAN LOG IN");

                        userInformation = null;
                        try {
                            userInformation = (UserInformation) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (userInformation != null) {

                            if (this.socketServer.foundLogIn(userInformation, this.client)) {
                                dos.writeInt(1);
                                dos.writeUTF("SERVER : YOUR CONNEXION HAS ESTABLISHED SUCCESFULLY");
                                dos.flush();
                                isTrue = false;
                            } else {
                                dos.writeInt(0);
                                dos.writeUTF("SERVER :YOUR PSEUDO OR PASSWORD AR RONG PLEASE RETAIT");
                                dos.flush();
                            }
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR OBJECT SEND IS NULL !!!");
                            dos.flush();
                            System.out.println("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");

                        }

                        break;
                    case 2:

                        System.out.println("SERVER : YOU CAN SING IN");
                        System.out.println(dis.readUTF());
                        userInformation = null;
                        try {
                            userInformation = (UserInformation) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println(dis.readUTF());

                        if (userInformation != null) {

                            if (this.socketServer.addUser(userInformation, this.client)) {
                                dos.writeInt(1);
                                dos.writeUTF("SERVER : YOUR REGISTARTION HAS ESTABLISHED SUCCESFULY");
                                dos.flush();
                                isTrue = false;

                            } else {
                                dos.writeInt(0);
                                dos.writeUTF(
                                        "SERVER :YOUR PSEUDO IS NOT SUPORTABLE OR PASSWORD NOT FORT PLEASE RETAIT");
                                dos.flush();
                            }
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR OBJECT SEND IS NULL !!!");
                            dos.flush();
                            System.out.println("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");

                        }

                        break;

                    default:
                        System.out.println("You have an error in your cmmande please retrait");
                        break;

                }

            }

            isTrue = true;
            String header = "", data = "";

            while (isTrue) {
                int n = 0;
                // BufferedInputStream bif = new BufferedInputStream(in);

                // while ((n = bif.read()) != 10) {
                // System.out.print((char) n);
                // }

                header = dis.readUTF();

                System.out.println("ascasc");

                if (header.toUpperCase().equals("N")) {
                    isTrue = false;
                    userInformation = null;
                    try {
                        userInformation = (UserInformation) ois.readObject();
                    } catch (

                    ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (userInformation != null) {

                        if (this.socketServer.logOut(userInformation)) {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOUR LOG OUT SUCCESFLY");
                            dos.flush();
                            System.out.println("SERVER : bye bye" + userInformation + " !!!!");
                            isTrue = false;
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR PSEUDO IS NOT SUPORTABLE OR PASSWORD NOT FORT PLEASE RETAIT");
                            dos.flush();
                        }
                    } else {
                        dos.writeInt(0);
                        dos.writeUTF("SERVER :YOUR OBJECT SEND IS NULL !!!");
                        dos.flush();
                        System.out.println("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");

                    }

                } else {

                    data = dis.readUTF();
                    try {
                        userInformation = (UserInformation) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    Socket reciver = this.socketServer.foundUser(header);
                    System.out.println(reciver);
                    if (reciver != null) {

                        try {
                            OutputStream outRecive = reciver.getOutputStream();
                            DataOutputStream dosRecive = new DataOutputStream(outRecive);
                            dosRecive.writeInt(1);
                            dosRecive.writeUTF(userInformation.getPseudo());
                            dosRecive.writeUTF(LocalDateTime.now().toString());
                            dosRecive.writeUTF(data);
                            dosRecive.flush();
                            isTrue = true;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        dos.writeInt(2);
                        dos.writeUTF("SERVER:SORRY THE DESTINATIOn NOT FOUND");
                        dos.flush();
                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.client != null) {
                    this.client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
/*  Jasmine Stapleton-Hart
    Continuation of CSC645 socket programming assignment for personal project
    April 24, 2023
    CLIENT FILE (v2.TCPClient.java)
    Start TCPServer.java first      */

package v2;
import java.io.*;
import java.net.*;
import java.util.Objects;

import static java.lang.System.exit;

class TCPClient
{
    public static void main(String argv[]) throws Exception
    {
        String input;
        String modifiedSentence;
        boolean connectionActive = false;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        // TCP connection setup

        Socket clientSocket = new Socket("127.0.0.1", 6789);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
        connectionActive = true;

        do
        {
            if( ! connectionActive)
            {
                clientSocket = new Socket("127.0.0.1", 6789);
                outToServer = new DataOutputStream(clientSocket.getOutputStream());
                inFromServer = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
                connectionActive = true;
            }
            System.out.println("0. Log in (Connect to server) \n" +
                    "1. Get the user list \n" +
                    "2. Send a message\n" +
                    "3. Get my messages\n" +
                    "4. Exit\n" +
                    "5. Create account");
            input = inFromUser.readLine();
            switch (input)
            {
                case "0":
                {
                    outToServer.writeBytes("0\n");
                    String response = inFromServer.readLine();
                    if (response.equals("A user is already logged in."))
                    {
                        System.out.println("You are already logged in!");
                        continue;
                    }
                    System.out.println("Enter username: ");
                    String username = inFromUser.readLine();
                    System.out.println("Enter password: ");
                    String password = inFromUser.readLine();
                    outToServer.writeBytes(username + "\n");
                    outToServer.writeBytes(password + "\n");
                    response = inFromServer.readLine();
                    System.out.println(response);
                    break;
                }


                case "1":
                {
                    outToServer.writeBytes("1\n");
                    System.out.println("CURRENT USER LIST:");
                    System.out.println(inFromServer.readLine());
                    break;
                }

                case "2":
                {
                    outToServer.writeBytes("2\n");
                    System.out.println("Enter username of recipient:");
                    String recipient = inFromUser.readLine();
                    outToServer.writeBytes(recipient + "\n");
                    String response = inFromServer.readLine();
                    System.out.println(response);
                    if (response.equals("Enter message:"))
                    {
                        String message = inFromUser.readLine();
                        outToServer.writeBytes(message + "\n");
                        System.out.println("MESSAGE SENT TO SERVER");
                    }
                    break;
                }


                case "3":
                {
                    outToServer.writeBytes("3\n");
                    System.out.println("CURRENT MESSAGES:");
                    String response;
                    while (!(response = inFromServer.readLine()).equals("END"))
                    {
                        System.out.println(response);
                    }
                    System.out.println("_________________");
                    break;
                }


                case "4":
                {
                    outToServer.writeBytes("4\n");
                    clientSocket.close();
                    connectionActive = false;
                    System.out.println("Logged out.");
                    break;
                }

                case "5":
                {
                    outToServer.writeBytes("5\n");
                    System.out.println("Creating new account.\nChoose a username: ");
                    String username = inFromUser.readLine();
                    outToServer.writeBytes(username + "\n");
                    String response = inFromServer.readLine();
                    while ( response.equals("Username taken."))
                    {
                        System.out.println("Username '" + username + "' is taken. Choose a different username:");
                        username = inFromUser.readLine();
                        outToServer.writeBytes(username + "\n");
                        response = inFromServer.readLine();
                    }
                    System.out.println("Choose a password: ");
                    String password = inFromUser.readLine();
                    outToServer.writeBytes(password + "\n");
                    System.out.println("Account created for " + username + ".");
                    break;
                }
            }
        } while(!Objects.equals(input, "4"));
    }
}
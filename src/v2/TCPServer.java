/*  Jasmine Stapleton-Hart
    Continuation of CSC645 socket programming assignment for personal project
    April 24, 2023
    SERVER FILE (v2.TCPServer.java)
    Start before TCPClient.java       */

package v2;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

class TCPServer
{
    public static void main(String argv[]) throws Exception
    {
        String clientInput;
        HashMap<String, String> accounts = new HashMap<String, String>();
        accounts.put("test", "pw");
        accounts.put("test2", "pw");
        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("SERVER is running ... ");
        boolean connectionActive = false;

        // create & populate a hashmap to store users' messages
        HashMap<String, ArrayList<String>> messages = new HashMap<>();
        for (String key : accounts.keySet())
        {
            messages.put(key, new ArrayList<String>());
        }


        while (true)
        {
            boolean breakout = false;
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            // waiting for incoming connection request
            clientInput = inFromClient.readLine();

            if (clientInput.equals("5"))
            {
                String username = inFromClient.readLine();
                while (accounts.containsKey(username))
                {
                    outToClient.writeBytes("Username taken.\n");
                    username = inFromClient.readLine();
                }
                outToClient.writeBytes("Account created");
                String password = inFromClient.readLine();
                accounts.put(username, password);
            }
            if (clientInput.equals("0"))
            {
                System.out.println("attempting login connection");
                outToClient.writeBytes("attempting login\n");
                String username = inFromClient.readLine();
                String password = inFromClient.readLine();
                if (accounts.containsKey(username) && accounts.get(username).equals(password))
                {
                    outToClient.writeBytes("Access Granted\n");
                    System.out.println("login successful!");
                    while (true)
                    {
                        String userInput = inFromClient.readLine();
                        switch (userInput)
                        {
                            case "0":
                                outToClient.writeBytes("A user is already logged in.\n");
                                continue;
                            case "1":
                            {
                                System.out.println("USER LIST:");
                                boolean commaNeeded = false;
                                for (String keys : accounts.keySet())
                                {
                                    if (commaNeeded)
                                    {
                                        outToClient.writeBytes(", ");
                                    } else
                                    {
                                        commaNeeded = true;
                                    }
                                    outToClient.writeBytes(keys);
                                }
                                outToClient.writeBytes("\n ----END----");
                                break;
                            }

                            case "2":
                            {
                                System.out.println("Sending a message");
                                String recipient = inFromClient.readLine();
                                if (accounts.containsKey(recipient))
                                {
                                    outToClient.writeBytes("Enter message:\n");
                                    String message = inFromClient.readLine();
                                    ArrayList<String> userMessageList = messages.get(recipient);
                                    message = message + "  (From: " + username + ")\n";
                                    userMessageList.add(message);

                                } else
                                {
                                    outToClient.writeBytes("No such user exists.\n");
                                }
                                break;
                            }

                            case "3":
                            {
                                System.out.println("Getting messages");
                                for (String message : messages.get(username))
                                {
                                    outToClient.writeBytes(message);
                                }
                                outToClient.writeBytes("END\n");
                                break;
                            }

                            case "4":
                                breakout = true;
                                System.out.println(username + " has logged out");
                                break;

                            case "5":
                                username = inFromClient.readLine();
                                System.out.println(username);
                        }
                        if (breakout)
                        {
                            break;
                        }
                    }
                } else
                {
                    outToClient.writeBytes("Access Denied - Username/Password Incorrect\n");
                }
            } else
            {
                outToClient.writeBytes("please enter sign in credentials\n");
            }
        }
    }
}
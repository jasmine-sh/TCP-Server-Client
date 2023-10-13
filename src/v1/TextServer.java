/*  Jasmine Stapleton-Hart
    CSC645 Computer Networks Spring 2023
    Socket Programming Assignment
    April 24, 2023
    SERVER FILE (v1.TextServer.java)    */

package v1;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class TextServer
{
    public static void main(String[] argv) throws Exception
    {
        String clientInput;
        HashMap<String, String> accounts = new HashMap<String, String>();
        accounts.put("Alice", "1234");
        accounts.put("Bob", "5678");
        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("SERVER is running ... ");
        boolean breakout = false;

        // create & populate a hashmap to store users' messages
        HashMap<String, ArrayList<String>> messages = new HashMap<>();
        for (String key : accounts.keySet())
        {
            messages.put(key, new ArrayList<String>());
        }

        Socket connectionSocket = welcomeSocket.accept();

        while (true)
        {
            if (breakout)           // allows user to log in if first username/pw input was incorrect
            {
                connectionSocket = welcomeSocket.accept();
                breakout = false;
            }
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            // waiting for incoming connection request
            clientInput = inFromClient.readLine();

            if (clientInput.equals("0"))
            {
                System.out.println("attempting connection");
                String userInput = inFromClient.readLine();

                if (userInput.equals("0"))
                {
                    String username = inFromClient.readLine();
                    while (accounts.containsKey(username))
                    {
                        System.out.println("Username taken. Requesting new account name from user.");
                        outToClient.writeBytes("Username taken\n");
                        username = inFromClient.readLine();
                    }
                    outToClient.writeBytes("Success \n");
                    String password = inFromClient.readLine();
                    accounts.put(username, password);
                    System.out.println("Account created for user " + username);
                }
                else if (userInput.equals("1"))
                {
                    String username = inFromClient.readLine();
                    //outToClient.writeBytes("attempting login\n");

                    String password = inFromClient.readLine();
                    // check hashmap for a matching username/password

                    if (accounts.containsKey(username) && accounts.get(username).equals(password))
                    {

                        outToClient.writeBytes("Access Granted\n");           // CONNECT TO CLIENT (log in user)
                        System.out.println("login successful!");
                        while (true)
                        {
                            userInput = inFromClient.readLine();
                            switch(userInput)
                            {
                                case "0":
                                {
                                    outToClient.writeBytes("A user is already logged in.\n");
                                    continue;
                                }

                                case "1":         // GET THE USER LIST
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
                                    outToClient.writeBytes("\n");
                                    break;
                                }

                                case "2":         // SEND A MESSAGE
                                {
                                    System.out.println("sending a message");
                                    String recipient = inFromClient.readLine();
                                    if (accounts.containsKey(recipient))
                                    {
                                        outToClient.writeBytes("Enter message:\n");
                                        String message = inFromClient.readLine();
                                        ArrayList<String> userMessageList = messages.get(recipient);
                                        message = ("[From: " + username + "]   " + message + "\n");
                                        userMessageList.add(message);

                                    }
                                    else
                                    {
                                        outToClient.writeBytes("No such user exists.\n");
                                    }
                                    break;
                                }

                                case "3":         // GET MY MESSAGES
                                {
                                    System.out.println("getting messages");
                                    List<String> userMessages = messages.get(username);
                                    if (userMessages.isEmpty())
                                    {
                                        outToClient.writeBytes("No messages \n");
                                    }
                                    else
                                    {
                                        for (String message : userMessages)
                                        {
                                            outToClient.writeBytes(message);
                                        }
                                    }
                                    outToClient.writeBytes("END\n");


                                    break;
                                }

                                case "4":         // EXIT
                                    breakout = true;
                                    System.out.println(username + " has logged out");
                                    break;
                            }
                            if (breakout)
                            {
                                break;
                            }
                        }
                    }
                    else
                    {
                        outToClient.writeBytes("Access Denied - Username/Password Incorrect\n");
                    }
                } else
                {
                    outToClient.writeBytes("You must sign in first.\n");
                }
                }


        }
    }
}
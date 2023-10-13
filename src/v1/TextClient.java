/*  Jasmine Stapleton-Hart
    CSC645 Computer Networks Spring 2023
    Socket Programming Assignment
    April 24, 2023
    CLIENT FILE (v1.TextClient.java)   */

package v1;
import java.io.*;
import java.net.*;
import java.util.Objects;

class TextClient
{
   public static void main(String[] argv) throws Exception
   {
      String input;
      BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
      boolean isLoggedIn = false;
      boolean breakout = false;
      // TCP connection setup
      Socket clientSocket = new Socket("127.0.0.1", 6789);
      DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
      BufferedReader inFromServer = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));

      do
      {
         System.out.println("0. Connect to server \n" +
                 "1. Get the user list \n" +
                 "2. Send a message\n" +
                 "3. Get my messages\n" +
                 "4. Exit");
         input = inFromUser.readLine();

         switch (input)
         {
            case "0":         // CONNECT TO SERVER (log  in)
            {
               outToServer.writeBytes("0\n");

               System.out.println("0. Create account \n1. Log in");
               input = inFromUser.readLine();

               if (input.equals("0"))
               {
                  outToServer.writeBytes("0\n");

                  System.out.println("Enter a username for your account");
                  String username = inFromUser.readLine();
                  outToServer.writeBytes(username + "\n");
                  String response = inFromServer.readLine();
                  while (response.equals("Username taken"))
                  {

                     System.out.println("Username '" + username + "' is taken. Please try a different name.");
                     username = inFromUser.readLine();
                     outToServer.writeBytes(username + "\n");
                     response = inFromServer.readLine();
                  }
                  System.out.println("Enter a password for your account.");
                  String password = inFromUser.readLine();
                  outToServer.writeBytes(password + "\n");
                  System.out.println("Account created for user " + username);
                  break;
               }

               else if (input.equals("1"))
               {
                  outToServer.writeBytes("1\n");
                  System.out.println("Enter username: ");
                  String username = inFromUser.readLine();
                  System.out.println("Enter password: ");
                  String password = inFromUser.readLine();
                  outToServer.writeBytes(username + "\n");
                  outToServer.writeBytes(password + "\n");
                  String response = inFromServer.readLine();

                  if ( response.equals("Access Granted") )
                  {
                     System.out.println(response);
                     isLoggedIn = true;
                  }
                  else
                  {
                     System.out.println("Error: " + response);
                  }
               }
               break;
            }

            case "1":         // GET THE USER LIST
            {
               if (isLoggedIn)
               {
                  outToServer.writeBytes("1\n");
                  System.out.println("_________________");
                  System.out.println("CURRENT USER LIST:");
                  System.out.println(inFromServer.readLine());
                  System.out.println("_________________");
               }
               else
               {
                  System.out.println("Please log in to view user list.");
               }
               break;
            }

            case "2":         // SEND A MESSAGE
            {
               if (isLoggedIn)
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
               }
               else
               {
                  System.out.println("Please log in to send messages.");
               }
               break;
            }


            case "3":         // GET MY MESSAGES
            {
               if (isLoggedIn)
               {
                  outToServer.writeBytes("3\n");
                  System.out.println("_________________");
                  System.out.println("CURRENT MESSAGES:");
                  String response = inFromServer.readLine();
                  while (!response.equals("END"))
                  {
                     System.out.println(response);
                     response = inFromServer.readLine();
                  }
                  System.out.println("_________________");
                  break;
               }
               else
               {
                  System.out.println("Please log in to view messages.");
               }
            }

            case "4":         // EXIT
            {
               if (isLoggedIn)
               {
                  outToServer.writeBytes("4\n");
                  clientSocket.close();
                  System.out.println("Logged out.");
                  isLoggedIn = false;
               }
               break;
            }
         }
      } while(!Objects.equals(input, "4"));
   } 
}

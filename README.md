# Socket Programming Project
Originally created for Computer Network Class CSC645 at San Francisco State University in Spring 2023.

## Purpose
The learning objective of this assignment was to understand TCP connections, sockets, client/server interaction, and the function of the welcome socket. We were allowed to use any language.
The purpose of the program is for users to send and recieve messages by connecting to a server where the messages and accounts are stored.

## Instructions
After building, run the server program first (***TextServer*** or ***TCPServer***) to initiate the server side. Then run the client program (***TextClient*** or ***TCPClient***) to act as a user on the server. You can change users by logging out and reconnecting as a different user. All information is lost when the server program is terminated.

### Hardcoded Accounts for Testing
These accounts are hardcoded to be present in the list of accounts on the server for testing purposes and as directed in original assignment instructions. Note that V1 (***TextClient*** and ***TextServer***) have different accounts than V2 (***TCPClient*** and ***TCPServer***).  
  
***V1***  
> **Username:** Alice  
> **Password:** 1234  

> **Username:** Bob  
> **Password:** 5678  

***V2***  
> **Username:** test  
> **Password:** pw

> **Username:** test2  
> **Password:** pw

## Functionality
### Required Functionality
Users can run the client program and connect to a running server where the user has the options to create an account and log in. Once logged in, the user can get the list of all other users on the network, send a message to another registered account, see their messages, and log out. Users can only see the messages sent to their account, and can only see them when logged in.

### Additional Functionality
Although not required for the original assignment, the ability for a user to create an account is necessary for the functionality of the messaging program. This function is present in V1, and was moved to the initial user prompt in V2.


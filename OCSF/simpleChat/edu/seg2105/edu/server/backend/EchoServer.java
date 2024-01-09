package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  String loginKey = "";
  String loginID;
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String msgString = (String) msg;
    if(msgString.startsWith("#login")){
      String[] loginWords = msgString.split(" ");
      loginID = loginWords[1];

      if (client.getInfo(loginID) == null){ 
        System.out.println("Message received: " + msg + " from " + loginKey);
        client.setInfo(loginID, loginID); //key and id are same so key is unique
        System.out.println(client.getInfo(loginID) + " has logged on.");
      } 
      else{
        System.out.println(client.getInfo(loginID) + " has logged on.");
      }
    }
    else{
      System.out.println("Message received: " + msg + " from " + client.getInfo(loginID));
      this.sendToAllClients(client.getInfo(loginID) + "> " + msg);
    }
  }

  /**
   * This method handles any messages received from the client.
   * 
   * @param message The message received from the server
   */
  public void handleMessageFromServerConsole(String message){
    try{
      if (message.startsWith("#")){
        handleCommand(message);
      }
      else{
        sendToAllClients("SERVER MSG> " + message);
      }
    } catch(Exception e){
      System.out.println("Could not send message to client");
    }
  }

  /**
   * This method handles command prompts starting with #
   * 
   * @param command
   */
  public void handleCommand(String command){
    if (command.equals("#quit")){
      try {
        close();
        System.out.println("Server has shut down");
      } catch (IOException e) {
        System.exit(1);
      }
      System.exit(0);
    }

    else if (command.equals("#stop")){
      stopListening();
      System.out.println("Server has stopped accepting new clients");
    }

    else if (command.equals("#close")){
      try {
        close();
      } catch (IOException e){
        System.out.println("Error while trying to close connections");
      }
    }

    else if (command.startsWith("#setport")){
      if (!isListening() && getNumberOfClients() == 0){
        String[] word = command.split(" ");
        int port = Integer.parseInt(word[1]);
        setPort(port);
        System.out.println("Port is set to " + port);
      }
      else{
        System.out.println("Error: Cannot set port when server and clients are connected");
      }
    }

    else if (command.equals("#start")){
      try{
        listen();
      } catch(IOException e){
        System.out.println("Error: Server is already open");
      }
    }

    else if (command.equals("#getport")){
     System.out.println("Port is " + getPort());
    }

    else{
      System.out.println("Not a command");
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("Client connected. Welcome! :)");
  }
  
  @Override
  synchronized protected void clientDisconnected( ConnectionToClient client) {
    System.out.println("Client disconnected. Thanks, goodbye :)");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  /*public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }*/
}
//End of EchoServer class

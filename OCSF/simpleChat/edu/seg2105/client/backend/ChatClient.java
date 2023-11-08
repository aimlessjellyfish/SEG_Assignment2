// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      if (message.startsWith("#")){
        handleCommand(message);
      }
      else{
        sendToServer(message);
      }
    } catch(IOException e){
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  /**
   * This method handles command prompts starting with #
   * @param command
   */

  private void handleCommand(String command){
    if (command.equals("#quit")){
      quit();
      clientUI.display("You have quit");
    }

    else if (command.equals("#logoff")){
      try{
        closeConnection();
        clientUI.display("You have logged off");
      } catch(IOException e){
        clientUI.display("There was an error logging off");
      }
    }

    else if (command.startsWith("#sethost")){
      String[] word = command.split(" ");
      String host = word[1];
      setHost(host);
      clientUI.display("Host is set to " + host);
    }

    else if (command.startsWith("#setport")){
      String[] word = command.split(" ");
      int port = Integer.parseInt(word[1]);
      setPort(port);
      clientUI.display("Port is set to " + port);
    }

    else if (command.equals("#login")){
      if (isConnected()){
        clientUI.display("Error: Already connected");
      }
      else{
        try{
          openConnection();
        } catch(IOException e){
          clientUI.display("Failed to connect");
        }
      } 
    }

    else if (command.equals("#gethost")){
      String host = getHost();
      clientUI.display("Host is " + host);
    }

    else if (command.equals("#getport")){
      int port = getPort();
      clientUI.display("Port is " + port);
    }

    else{
      clientUI.display("Not a command");
    }
  }

  @Override
  protected void connectionClosed() {
    clientUI.display("Connection Closed");
	}

  @Override
  protected void connectionException(Exception exception) {
    clientUI.display("Server has shutdown");
    quit();
	}
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    } catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class

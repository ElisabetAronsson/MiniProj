package Controller;

import Model.Message;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Messager {
    /**
     * Lazy draft for the direct messaging controller class
     */
    private static int PORT = 8080; //Is this needed?
    private ArrayList<Message> messages;

    public Messager(){
        messages = new ArrayList<>();
    }


    public void sendMessage(String sender, String receiver, String messageToSend) {
        Message message = new Message(sender, receiver, messageToSend);
        messages.add(message);
    }
    public void sendMessage(){

    }
    public ArrayList<Message> getMessages(String currentUser, String otherUser) {
        ArrayList<Message> messageList = new ArrayList<>();

        for (Message message : messages) {
            if (message.getSender().equals(currentUser) && message.getReceiver().equals(otherUser)
                    || message.getSender().equals(otherUser) && message.getReceiver().equals(currentUser)) {
                messageList.add(message);
            }
        }
        return messageList;
    }
}

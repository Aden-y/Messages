
import java.util.ArrayList;


/**
 *
 * @author Your Name Here
 */
public class Message {
    //Private Attributes
    private int id; //Hold the message ID
    private int pid;// Hold Parent ID to wich this message is a reply
    private String message;//The message body
    private ArrayList<Message> children=new ArrayList<Message>();
    
    //Constructor
     public Message(int id, int pid, String message,Message parent){
        this.message=message;
        this.id=id;
        this.pid=pid;
        if(pid!=0){
            parent.children.add(this);
        }
   
    }
     //This constructor Will be Used To Create New Messages Read From a File
    public Message(int id, int pid, String message){
        this.message=message;
        this.id=id;
        this.pid=pid;   
    } 
    //Returns The message Text
    public String getText(){
        return this.message;
    }
    //Returns the ID of the message
    public int getID(){
        return this.id;
    }
    //Returns id of the message
     public int getPId(){       
         return this.pid;
     }
     //Returns replys For a specific message
     public ArrayList<Message> getReplys(){
         return this.children;
     }
     //Replys to a message
    public void reply(Message message){
        this.children.add(message);
    }
    //This is the function tha desplays each message and all its children in hierarchical way. This method is recursive
    public void display(int indent){
        for (int i=0; i<=indent; i++){
            System.out.print(" ");
        }
        System.out.println(this.id+"        "+this.message);
        indent+=2;
        for(int i=0; i<this.children.size(); i++){
            Message m=this.children.get(i);
            m.display(indent);
        }
    }
    public void setChildren(ArrayList<Message> list){
        this.children=list;
    }
}

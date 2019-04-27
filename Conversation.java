
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

//This Class represents one single conversation
public class Conversation {
    private  final String DATAFILE="msg.data";//File for Storing messages
    public ArrayList<Message> mesages=new ArrayList<Message>();//List of messsages
    public int messageID=1;//ID for new message
    
    
    //Saves The message to the msg.data file
    public void saveMessages(){
        File msgFile=new File(this.DATAFILE);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(msgFile));
            for (int i=0; i<this.mesages.size();i++){
           Message m=this.mesages.get(i);
           String line="";//Create a line that will hold each message
           if(i+1==this.mesages.size()){
               line+=m.getID()+"|"+m.getPId()+"|"+m.getText();
           }else{
               line+=m.getID()+"|"+m.getPId()+"|"+m.getText()+"\n";
           }
           writer.write(line);//Write The Line To The Output File
        
           
        }
               writer.close();//Close The File After Writing
        } catch (IOException ex) {
            ex.printStackTrace();
        }
           
    }
    
    
    public ArrayList<Message> getChildren(Message m, ArrayList<Message> list){
        ArrayList<Message> ret= new ArrayList<Message>();
        for( int i =0; i < list.size(); i++){
            Message mn=list.get(i);
            if(mn.getPId()== m.getID()){
                ret.add(mn);
            }
        }
        
        return ret;
    }
    //Reads messages from the msg.data File
    public void readMessages(){
        ArrayList<Message> incomplete= new ArrayList<Message>();//Contains messages before we finnally link their Children
            try{
            File file=new File(this.DATAFILE);
            Scanner in=new Scanner(file);
            //While The End Of The File Has Not Been Reached
            while(in.hasNextLine()){
             //Read Each Line
                String line = in.nextLine();
                String[] lineArray=line.split("\\|");//Split The line to obtain id , pid and Text
                int id=Integer.parseInt(lineArray[0]);
                int pid=Integer.parseInt(lineArray[1]);
                String text=lineArray[2];
                Message m= new Message(id, pid, text);
                incomplete.add(m);
            }
           in.close();
            
        }catch(FileNotFoundException e){
            System.err.println("Could not find the file "+this.DATAFILE);
        }catch(NoSuchElementException e){}
            //We are now getting children of ech message and eventually adding hem to the conversation list
        for(int i=0; i < incomplete.size(); i++){
            Message m=incomplete.get(i);
            m.setChildren(getChildren(m, incomplete));
            this.mesages.add(m);
            this.messageID++;//Increment Id of the next new message
        }
            
    }//This method Displays The messages with The recursive indents
    public void displayMessages(){
        for(int i=0; i < this.mesages.size(); i++){
            Message m= this.mesages.get(i);
            if(m.getPId()==0){
                m.display(0);
            }
        }
    }
    
    //Searches the conversation for a message with the given id and then returns it
    public Message getMessageById(int id){
        for (int i =0; i < this.mesages.size(); i++){
            Message m=this.mesages.get(i);
            if (m.getID()==id){
                return m;
                
            }
        }
        return null;
    }
    
    //Prompts the user for a choice of which message to reply, or  to exit program or to create a new conversation
    public int getUserResponse(){
        System.out.println("Which message would you like to reply to:(Reply with message ID)");
        this.displayMessages();
        System.out.println("Use 0 to Start new Topic and -1 to exit program");
        try{
            Scanner in=new Scanner(System.in);
            return Integer.parseInt(in.nextLine());
        }catch(Exception e){ 
            System.err.println("Invalid response");
            return -2;
        }
    }
    
    //The  main metot that now runs the program
    public static void main(String [] args){
        Scanner scanner = new Scanner (System.in);
        Conversation con = new Conversation();
        con.readMessages();
        boolean exit=false;
        while(!exit){
            int input=con.getUserResponse();
            if(input==-1){
                exit = true;
                con.saveMessages();
                break;
            }
            if(input!=-2){
                if(input == 0){
                    System.out.print("Type the message :");
                        String text=scanner.nextLine();
                        Message mn= new Message(con.messageID,0,text,null);
                        con.mesages.add(mn);
                }else{
                    if(con.getMessageById(input)==null){
                        System.err.println("No such message ID");
                    }else{
                        Message m = con.getMessageById(input);
                        System.out.print("Type the message :");
                        String text=scanner.nextLine();
                        Message mn= new Message(con.messageID,m.getID(),text,m);
                        con.mesages.add(mn);
                    }
                }
            }
            
           con.messageID++; 
        }
    }
}

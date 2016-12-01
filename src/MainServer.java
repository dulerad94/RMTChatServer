import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class MainServer {
	public static LinkedList<ChatUser> listOfUsers=new LinkedList<>();
	private static ServerSocket serverSocket;
	//spomeni im za ubacivanje broja porta preko args-a
	public static void main(String[] args) {
		try {
			int port=Integer.parseInt(args[0]);
			serverSocket = new ServerSocket(port);
			while(true){
				Socket userSocket=serverSocket.accept();
				ChatUser user=new ChatUser(userSocket);
				listOfUsers.add(user);			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sendMessage(String message){
		for (ChatUser chatUser : listOfUsers) {
			chatUser.send(message);
		}
	}
	
	public static void sendMessage(ChatUser user, String messageToAll, String messageToUser ){
		for (ChatUser chatUser : listOfUsers) {
			if(chatUser==user) continue;
			chatUser.send(messageToAll);
		}
		user.send(messageToUser);
	}
	public static void removeUser(ChatUser user){
		listOfUsers.remove(user);
	}
}

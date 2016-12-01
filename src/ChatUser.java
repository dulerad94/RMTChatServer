import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;

public class ChatUser implements Runnable{
	private Socket socket;
	private PrintStream outputStream;
	private BufferedReader inputStream;
	private Thread thread;
	private String name;
	
	public String getName(){
		return name;
	}
	
	public ChatUser(Socket socket){		
		try {
			this.socket=socket;
			outputStream=new PrintStream(socket.getOutputStream());
			inputStream=new BufferedReader(new InputStreamReader(socket.getInputStream()));		
			thread=new Thread(this);
			thread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	@Override
	public void run() {
		try {
			name=inputStream.readLine();
			//notifying about new user
			MainServer.sendMessage(this,"New user:"+name+" has arived", "Welcome");
			while(true){
				String message=inputStream.readLine();
				if(message.equals("/quit")) break;
				MainServer.sendMessage(name+":"+message);
			}
			//notifying about leaving
			MainServer.sendMessage(this,name+" has disconnected", "Goodbye");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		MainServer.removeUser(this);
	}
	public void send(String message){
		outputStream.println(message);
	}
	
}

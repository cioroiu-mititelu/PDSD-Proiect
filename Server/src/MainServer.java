import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer implements Runnable {
	 
	private final static String TAG = "RequestsServer";
	
	private volatile boolean isRunning = false; 

	private ServerSocket serverSocket;

	public void setRunningState(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	public boolean getRunningState() {
		return isRunning;
	}
	
	
	public MainServer() throws IOException {
		
		int port = 9000;
		while(!SocketUtils.available(port)){
			port ++;
		}
		serverSocket = new ServerSocket(port);
	}

	@Override
	public void run() {

		while(true) {
			Socket clientSocket = null;

			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				Log.e(TAG, "Error while accepting connection");
			}
			if(clientSocket != null){
				RoomServer roomServer = new RoomServer(clientSocket);
				Thread pheasantThread = new Thread(roomServer);
				pheasantThread.start();
			}
		}
		
		//try {
		//	serverSocket.close();
		//} catch (IOException exception) {
		//	Log.d(TAG, "Error closing socket.");
		//}

	}
}
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

public class ReaderServer implements Runnable {
	private final static String TAG = "ServiceThread";
	
	Vector<Room> rooms;
	Socket socket;
	
	public ReaderServer(Socket socket, Vector<Room> rooms) {
		this.socket = socket;
		this.rooms = rooms;
	}
	
	@Override
	public void run() {
		// When accept() returns a new request was received.
		// We use the incomingRequest socket for I/O
		Log.d(TAG, "New request from: " + socket.getInetAddress());

		// Get its associated OutputStream for writing.
		OutputStream responseStream = null;
		try {
			responseStream = socket.getOutputStream();
		} catch (IOException e) {
			Log.e(TAG, "Cannot get outputstream.");
		}

		// Wrap it with a PrinStream for convenience.
		PrintStream writer = new PrintStream(responseStream);
		writer.print(JsonRooms.roomsToJson(rooms).toString());

		

		// Make sure data is sent and allocated resources are cleared.
		try {
			socket.close();
		} catch (IOException e) {
			Log.e(TAG, "Error finishing request.");
		}
		Log.d(TAG, "Sent greeting.");
	}
	
}
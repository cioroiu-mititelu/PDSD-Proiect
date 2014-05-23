

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;


public class RoomServer implements Runnable {
	
	private final static String TAG = "RoomServer";
	private final static String ROOM_CREATE = "0";
	private final static String ROOM_REMOVE = "1";
	private final static String ROOM_UPDATE = "2";
	private final static String LOGIN = "-1";
	private final static String OK = "OK";
	private final static String BAD = "BAD";
	private final static String ASK_ROOM = "5";
	private final static String POST_MESSAGE = "6";
	
	private static Vector<Room> serverRooms = new Vector<Room>(10);
	private static HashMap<String, String> userNames = new HashMap<String, String>();
	
	private Socket clientSocket;
	
	public RoomServer(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		BufferedReader input = null;
		PrintStream output = null;
		boolean isRunning = true;
		
		try {
			input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			output = new PrintStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.out.println("Cannot get inputstream / outputstream");
			e.printStackTrace();
		}
		
		while(isRunning) {
			try {
				// get action
				String clientWord = input.readLine();
				System.out.println("server got " + clientWord);
				
				// create action
				if (clientWord.equals(ROOM_CREATE)) {
					int ok = 1;
					
					// get room name
					clientWord = input.readLine();
					System.out.println("server got " + clientWord);

					// check for existing room
					for(int i = 0; i < serverRooms.size(); i++) {
						if (serverRooms.get(i).name.equals(clientWord)) {
							ok = 0;
							break;
						}
					}
					
					// add room
					if (ok == 1) {
						serverRooms.add(new Room(clientWord, 0, new Vector<Message>()));
					}
				}
				
				// remove action
				else if (clientWord.equals(ROOM_REMOVE)) {
					// get room name
					clientWord = input.readLine();
					System.out.println("server got " + clientWord);
					
					// check for existing room
					for(int i = 0; i < serverRooms.size(); i++) {
						if (serverRooms.get(i).name.equals(clientWord)) {
							// remove room
							serverRooms.remove(i);
						}
					}
				}
				
				// update action
				else if (clientWord.equals(ROOM_UPDATE)) {
					// server send JSON formatted string
					output.println(JsonRooms.roomsToJson(serverRooms));
				}
				
				else if (clientWord.equals(LOGIN)){
					String[] cred = input.readLine().split(";");
					String username = cred[0];
					String password = cred[1];
					
					if(userNames.containsKey(username)){
						if(userNames.get(username).equals(password)){
							output.println(OK);
						} else {
							output.println(BAD);
						}
					} else {
						userNames.put(username, password);
						output.println(OK);
					}
				} else if(clientWord.equals(ASK_ROOM)){
					
					String room = input.readLine();
					System.out.println(room);
					for(int i=0; i<serverRooms.size(); i++){
						if(serverRooms.get(i).name.equals(room)){
							System.out.println(serverRooms.get(i).name);
							JSONObject js = JsonRooms.roomToJson(serverRooms.get(i));
							System.out.println(js.toString());
							output.println(js);
							break;
						}
					}
				} else if(clientWord.equals(POST_MESSAGE)){
					String messageJson = input.readLine();
					System.out.println(messageJson);
					Message newM = JsonMessages.messageFromJson(new JSONObject(messageJson));
					
					String room = newM.room;
					for(int i=0; i<serverRooms.size(); i++){
						if(serverRooms.get(i).name.equals(room)){
							serverRooms.get(i).messages.add(newM);
							break;
						}
					}
				}
				
				isRunning = false;
			} catch(IOException exception) {
				Log.e(TAG, "Exception while reading from / writing to client");				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "Error finishing request.");
		}
	}
	
}
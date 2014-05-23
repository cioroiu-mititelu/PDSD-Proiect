package com.example.pdsd_roomchatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.*;

import android.util.Log;

public class RoomServer implements Runnable {
	
	private final static String TAG = "RoomServer";
	private final static String ROOM_CREATE = "0";
	private final static String ROOM_REMOVE = "1";
	private final static String ROOM_UPDATE = "2";
	
	private static Vector<Room> serverRooms = new Vector<Room>(10);
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
						serverRooms.add(new Room(clientWord, 0, null));
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
				
				isRunning = false;
			} catch(IOException exception) {
				Log.e(TAG, "Exception while reading from / writing to client");				
			}
		}
		
		try {
			clientSocket.close();
		} catch (IOException e) {
			Log.e(TAG, "Error finishing request.");
		}
	}
	
}
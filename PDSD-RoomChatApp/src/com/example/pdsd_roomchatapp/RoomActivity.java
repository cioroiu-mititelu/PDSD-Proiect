package com.example.pdsd_roomchatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class RoomActivity extends Activity {

	private static Vector<Room> clientRooms = new Vector<Room>();
	private static List<String> clientList = new ArrayList<String>();
	public ArrayAdapter<String> adapter;
	private static String TAG = "ROOM";
	private String title;
	private final static String ASK_ROOM = "5";
	private final static String POST_MESSAGE = "6";
	public static Room thisRoom;
	public Thread t;
	public boolean Active = false;
	private ListView listView;
	private boolean closed = true;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);
		Thread T = new Thread(new UpdateRoom());
		T.start();
		Active = true;
		Bundle b = getIntent().getExtras();
		String value = b.getString("title");
		TextView t = (TextView) findViewById(R.id.textView1);
		t.setText(value);
		title = value;

		closed = false;
		listView = (ListView) findViewById(R.id.listView1);
		listView.setVisibility(View.VISIBLE);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, clientList);
		listView.setAdapter(adapter);

		new Thread(new Runnable() {
			@Override
			public void run() {

				String serverWord = null;
				String clientWord;
				final String serverIP = MainActivity.serverip;
				final String serverPort = MainActivity.serverport;
				Socket clientSocket = null;
				BufferedReader input = null;
				PrintStream output = null;
				try {
					clientSocket = new Socket(serverIP, Integer
							.parseInt(serverPort));
					input = new BufferedReader(new InputStreamReader(
							clientSocket.getInputStream()));
					output = new PrintStream(clientSocket.getOutputStream());
				} catch (UnknownHostException e) {
					Log.e(TAG, "Unknown host");
				} catch (IOException e) {
					Log.e(TAG, "Error opening client socket:" + e.getMessage());
				}

				// send to server
				clientWord = ASK_ROOM;
				output.println(clientWord);

				clientWord = title;
				output.println(clientWord);
				String response = null;

				try {
					serverWord = input.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				parseRoom(serverWord);
			}
		}).start();
		
		final ListView lv = listView;
		Button sendMessage = (Button) findViewById(R.id.buttonsend);
		sendMessage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						String serverWord = null;
						String clientWord;
						final String serverIP = MainActivity.serverip;
						final String serverPort = MainActivity.serverport;
						Socket clientSocket = null;
						BufferedReader input = null;
						PrintStream output = null;
						try {
							clientSocket = new Socket(serverIP, Integer
									.parseInt(serverPort));
							input = new BufferedReader(new InputStreamReader(
									clientSocket.getInputStream()));
							output = new PrintStream(clientSocket
									.getOutputStream());
						} catch (UnknownHostException e) {
							Log.e(TAG, "Unknown host");
						} catch (IOException e) {
							Log.e(TAG,
									"Error opening client socket:"
											+ e.getMessage());
						}

						// send to server
						clientWord = POST_MESSAGE;
						output.println(clientWord);

						final EditText mEdit = (EditText) findViewById(R.id.editTextmessage);
						
						Message M = new Message(title, mEdit.getText()
								.toString());
						M.message = MainActivity.myUsername+": "+M.message;
						String spacer = new String();
						for(int i=0; i< MainActivity.myUsername.length() + 1; i++){
							spacer += "  ";
						}
						spacer+=" ";
						M.message = M.message.replace("\n", "\n"+ spacer);
						M.message = M.message.replace("\r", "\r"+ spacer);
						JSONObject clientWordJSON = JsonMessages
								.messageToJson(M);
						output.println(clientWordJSON);
						String response = null;
						//lv.setSelection(lv.getCount() - 1);
						RoomActivity.this.runOnUiThread(new Runnable(){
						    public void run(){
						    	mEdit.setText("");
						    	//lv.setSelection(lv.getCount() - 1);
						    }
						});
					}
				}).start();
			}
		});
	}
	
	@Override
	protected void onPause()
	{
	    closed = true;
	    super.onPause();
	}

	@Override
	protected void onResume()
	{
	    closed = false;
	    super.onResume();
	}
	
	private void parseRoom(String json) {
		RoomActivity.clientRooms.clear();
		//adapter = new ArrayAdapter<String>(this,
		//		android.R.layout.simple_list_item_1, clientList);
		try {
			RoomActivity.thisRoom = JsonRooms
					.roomFromJson(new JSONObject(json));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		RoomActivity.clientList.clear();
		if (RoomActivity.thisRoom.messages != null)
			for (int i = 0; i < RoomActivity.thisRoom.messages.size(); i++) {
				// System.out.println(this.thisRoom.messages.get(i).name + " " +
				// this.clientRooms.get(i).nrUsers);
				RoomActivity.clientList
						.add(this.thisRoom.messages.get(i).message);
			}

		
		RoomActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				adapter.notifyDataSetChanged();
				listView.setSelection(listView.getCount() - 1);
			}
		});
	}
	
	class UpdateRoom implements Runnable {

		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(!closed){
					String serverWord = null;
					String clientWord;
					final String serverIP = MainActivity.serverip;
					final String serverPort = MainActivity.serverport;
					Socket clientSocket = null;
					BufferedReader input = null;
					PrintStream output = null;
					try {
						clientSocket = new Socket(serverIP,
								Integer.parseInt(serverPort));
						input = new BufferedReader(new InputStreamReader(
								clientSocket.getInputStream()));
						output = new PrintStream(clientSocket.getOutputStream());
					} catch (UnknownHostException e) {
						Log.e("Updater:", "Unknown host");
					} catch (IOException e) {
						Log.e("Updater",
								"Error opening client socket:" + e.getMessage());
					}
	
					// send to server
					clientWord = ASK_ROOM;
					output.println(clientWord);
	
					clientWord = title;
					output.println(clientWord);
					String response = null;
	
					try {
						serverWord = input.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					parseRoom(serverWord);
				}
			}

		}
	}

}

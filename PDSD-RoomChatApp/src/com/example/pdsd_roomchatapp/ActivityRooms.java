package com.example.pdsd_roomchatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityRooms extends Activity {

	//private MainServer mainServer;
	private Vector<Room> clientRooms = new Vector<Room>();
    private List<String> clientList = new ArrayList<String>();
	private final static String ROOM_CREATE = "0";
	private final static String ROOM_REMOVE = "1";
	private final static String ROOM_UPDATE = "2";
	private final static String GET_ROOMS = "3";
	private final static String GET_CONVERSATION = "4";
	
	private ListView listView;
	public ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView t = (TextView)findViewById(R.id.textViewname);
		t.setText(MainActivity.myUsername);
		
		ActivityRooms.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(
						ActivityRooms.this);
				builder1.setMessage("Welcome " + MainActivity.myUsername + "!");
				builder1.setCancelable(true);
				builder1.setPositiveButton(
						"OK",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						});

				AlertDialog alert11 = builder1.create();
				alert11.show();
			}
		});
		
		listView = (ListView)findViewById(R.id.roomList);
		listView.setVisibility(View.VISIBLE);
		//listView.setTextFilterEnabled(true);
		clientList.add("Test");
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clientList);
		listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id)
             {
 			    // When clicked, show a toast with the TextView text
            	 Toast.makeText(getApplicationContext(),
            			 ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
            	 Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
            	 Bundle b = new Bundle();
            	 b.putString("title", ((TextView) view).getText().toString());
            	 intent.putExtras(b);
            	 view.getContext().startActivity(intent);
             }
        });

        
		Button createRoom = (Button)findViewById(R.id.create);
		createRoom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String TAG = "Client TAG";
				final String roomName = ((EditText)findViewById(R.id.room)).getText().toString();

				
				// Create then start the "worker" thread.
				new Thread(new Runnable() {
					@Override
					public void run() {
						String serverWord;
						String clientWord;
						final String serverIP = MainActivity.serverip;
						final String serverPort = MainActivity.serverport;
						Socket clientSocket = null;
						BufferedReader input = null;
						PrintStream output = null;
						try {
							clientSocket = new Socket(serverIP, Integer.parseInt(serverPort));	
							input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							output = new PrintStream(clientSocket.getOutputStream());
						} catch (UnknownHostException e) {
							Log.e(TAG, "Unknown host");
						} catch (IOException e) {
							Log.e(TAG, "Error opening client socket:" + e.getMessage());
						}
						
						// send to server
						clientWord = ROOM_CREATE;
						output.println(clientWord);
						
						clientWord = roomName;
						output.println(clientWord);
						//clientList.add(roomName);
						ActivityRooms.this.runOnUiThread(new Runnable(){
						    public void run(){
						    	Button upd = (Button)findViewById(R.id.update);
						    	upd.callOnClick();
						    }
						});
						
						
						
					}
				}).start();
				
			}
			
		});
		
		
		
		Button removeRoom = (Button)findViewById(R.id.remove);
		removeRoom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String TAG = "Client TAG";
				final String roomName = ((EditText)findViewById(R.id.room)).getText().toString();
				// Create then start the "worker" thread.
				new Thread(new Runnable() {
					@Override
					public void run() {
						String serverWord;
						String clientWord;
						final String serverIP = MainActivity.serverip;
						final String serverPort = MainActivity.serverport;
						Socket clientSocket = null;
						BufferedReader input = null;
						PrintStream output = null;
						try {
							clientSocket = new Socket(serverIP, Integer.parseInt(serverPort));	
							input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							output = new PrintStream(clientSocket.getOutputStream());
						} catch (UnknownHostException e) {
							Log.e(TAG, "Unknown host");
						} catch (IOException e) {
							Log.e(TAG, "Error opening client socket."+ e.getMessage());
						}
						
						// send to server
						clientWord = ROOM_REMOVE;
						output.println(clientWord);
						
						clientWord = roomName;
						output.println(roomName);
						
						ActivityRooms.this.runOnUiThread(new Runnable(){
						    public void run(){
						    	Button upd = (Button)findViewById(R.id.update);
						    	upd.callOnClick();
						    }
						});
						
					}
				}).start();
			}	
		});
		
		
		
		Button updateRooms = (Button)findViewById(R.id.update);
		updateRooms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String TAG = "Client TAG";
				final String roomName = ((EditText)findViewById(R.id.room)).getText().toString();
				// Create then start the "worker" thread.
				new Thread(new Runnable() {
					@Override
					public void run() {
						String serverWord;
						String clientWord;
						final String serverIP = MainActivity.serverip;
						final String serverPort = MainActivity.serverport;
						Socket clientSocket = null;
						BufferedReader input = null;
						PrintStream output = null;
						try {
							clientSocket = new Socket(serverIP, Integer.parseInt(serverPort));	
							input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							output = new PrintStream(clientSocket.getOutputStream());
						} catch (UnknownHostException e) {
							Log.e(TAG, "Unknown host");
						} catch (IOException e) {
							Log.e(TAG, "Error opening client socket."+ e.getMessage());
						}
						
						// send to server
						clientWord = ROOM_UPDATE;
						output.println(clientWord);
						
						try {
							serverWord = input.readLine();
							System.out.println("client got " + serverWord);
							parseRooms(serverWord);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();

				adapter.notifyDataSetChanged();
			}
		});
		ActivityRooms.this.runOnUiThread(new Runnable(){
		    public void run(){
		    	Button upd = (Button)findViewById(R.id.update);
		    	upd.callOnClick();
		    }
		});
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void parseRooms(String json) {	
		this.clientRooms.clear();
		
		try {
			this.clientRooms = JsonRooms.roomsFromJson(new JSONObject(json));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		this.clientList.clear();
		if(this.clientRooms != null)
		for(int i = 0 ; i < this.clientRooms.size(); i++) {			
			System.out.println(this.clientRooms.get(i).name + " " + this.clientRooms.get(i).nrUsers);
			this.clientList.add(this.clientRooms.get(i).name);
		}
		
		ActivityRooms.this.runOnUiThread(new Runnable(){
		    public void run(){
		    	adapter.notifyDataSetChanged();
		    }
		});
	}
		
}

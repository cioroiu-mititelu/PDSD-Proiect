package com.example.pdsd_roomchatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String LOGIN = "-1";
	private static final String OK = "OK";
	private static final String BAD = "BAD";
	public static String myUsername;
	public static String serverip = "86.120.117.36";
	public static String serverport = "9015";;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Button createRoom = (Button) findViewById(R.id.button1);
		createRoom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String TAG = "Client TAG";
				final String usernameName = ((EditText) findViewById(R.id.editText1))
						.getText().toString();
				final String pass = ((EditText) findViewById(R.id.editText2))
						.getText().toString();
				final View vv = v;
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
						clientWord = LOGIN;
						output.println(clientWord);

						clientWord = usernameName+";"+pass;
						output.println(clientWord);
						String response = null;
						try {
							response = input.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.w("LOGIN", response);
						if (response.equals(OK)) {
							myUsername = usernameName;
							
							Intent intent = new Intent(getApplicationContext(),
									ActivityRooms.class);
							vv.getContext().startActivity(intent);

						} else {

							MainActivity.this.runOnUiThread(new Runnable() {
								public void run() {
									AlertDialog.Builder builder1 = new AlertDialog.Builder(
											MainActivity.this);
									builder1.setMessage("Bad credentials! Bad human! Pay attention!");
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

						}

					}
				}).start();

			}

		});
	}
}

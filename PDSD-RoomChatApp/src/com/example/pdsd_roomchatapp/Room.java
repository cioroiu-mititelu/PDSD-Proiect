package com.example.pdsd_roomchatapp;

import java.util.Vector;

public class Room {
	public String name;
	public int nrUsers;
	public Vector<Message> messages;
	
	Room(String name, int nrUsers, Vector<Message> messages){
		this.name = name;
		this.nrUsers = nrUsers;
		this.messages = messages;
	}
}

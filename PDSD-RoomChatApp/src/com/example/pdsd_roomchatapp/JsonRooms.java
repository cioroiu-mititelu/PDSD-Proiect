package com.example.pdsd_roomchatapp;

import java.util.*;

import org.json.*;

import android.util.Log;

public class JsonRooms {
	
	/* Single object */
	public static JSONObject roomToJson(Room room) {
        JSONObject jsonObj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		Message message = null;
        try {
        	jsonObj.put("name", room.name);
        	jsonObj.put("nrUsers", room.nrUsers);
        	
		}
		catch(JSONException ex) {
			 ex.printStackTrace();
		}
        return jsonObj;
	}
	
	public static Room roomFromJson(JSONObject jsonObj) {
        Room room = null;
       // Log.e("JSON", jsonObj.toString());
		try {
			room = new Room(jsonObj.getString("name"), jsonObj.getInt("nrUsers"), JsonMessages.messagesFromJson(jsonObj));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return room;
	}
	

	/* Many objects (vector) */
	public static JSONObject roomsToJson(Vector<Room> rooms) {
		Room room;
		JSONArray jsonArr = new JSONArray(); 
        JSONObject jsonObj = new JSONObject(); 
		try {
			for (int i = 0 ; i < rooms.size(); i++) {
				room = rooms.get(i);
			    jsonArr.put(roomToJson(room));
			}
	        jsonObj.put("rooms", jsonArr);

		 }
		 catch(JSONException ex) {
			 ex.printStackTrace();
		 }
		 return jsonObj;
	}
	
	
	public static Vector<Room> roomsFromJson(JSONObject jsonObj) {
		Vector<Room> rooms = new Vector<Room>(10);
		try {
			JSONArray jArr = jsonObj.getJSONArray("rooms");
			for (int i=0; i < jArr.length(); i++) {
			    JSONObject obj = jArr.getJSONObject(i);
			    rooms.add(roomFromJson(obj));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rooms;
	}
}

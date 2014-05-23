package com.example.pdsd_roomchatapp;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonMessages {
	/* Single object */
	public static JSONObject messageToJson(Message message) {
        JSONObject jsonObj = new JSONObject();
        try {
        	jsonObj.put("message", message.message);
        	jsonObj.put("room", message.room);
		}
		catch(JSONException ex) {
			 ex.printStackTrace();
		}
        return jsonObj;
	}
	
	public static Message messageFromJson(JSONObject jsonObj) {
        Message message = null;
		try {
			message = new Message(jsonObj.getString("room"), jsonObj.getString("message"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	
	
	/* Many objects (vector) */
	public static JSONObject messagesToJson(Vector<Message> messages) {
		Message message;
		JSONArray jsonArr = new JSONArray(); 
        JSONObject jsonObj = new JSONObject(); 
		try {
			for (int i = 0 ; i < messages.size(); i++) {
				message = messages.get(i);
			    jsonArr.put(messageToJson(message));
			}
	        jsonObj.put("messages", jsonArr);

		 }
		 catch(JSONException ex) {
			 ex.printStackTrace();
		 }
		 return jsonObj;
	}
	
	
	public static Vector<Message> messagesFromJson(JSONObject jsonObj) {
		Vector<Message> messages = new Vector<Message>(10);
		try {
			JSONArray jArr = jsonObj.getJSONArray("messages");
			for (int i=0; i < jArr.length(); i++) {
			    JSONObject obj = jArr.getJSONObject(i);
			    messages.add(messageFromJson(obj));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(messages.size() == 0)
			return null;
		return messages;
	}
	
}

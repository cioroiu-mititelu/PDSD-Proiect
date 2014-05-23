import java.util.*;

import org.json.*;

public class JsonRooms {
	
	/* Single object */
	public static JSONObject roomToJson(Room room) {
        JSONObject jsonObj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		//JSONObject msg = JsonMessages.messagesToJson(room.messages);
        try {
        	jsonObj.put("name", room.name);
        	jsonObj.put("nrUsers", room.nrUsers);
        	//jsonObj.put("messages", room.messages);
        	for (int i = 0 ; i < room.messages.size(); i++) {
				Message m = room.messages.get(i);
			    jsonArr.put(JsonMessages.messageToJson(m));
			}
	        jsonObj.put("messages", jsonArr);
		}
		catch(JSONException ex) {
			 ex.printStackTrace();
		}
        return jsonObj;
	}
	
	public static Room roomFromJson(JSONObject jsonObj) {
        Room room = null;
		try {
			room = new Room(jsonObj.getString("name"), jsonObj.getInt("nrUsers"), null
					
					);
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

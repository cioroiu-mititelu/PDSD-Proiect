import java.util.Date;




public class Log {
	
	public static void e(String tag, String message){
		Date d = new Date();
		System.out.println(d.toString() + " " + tag + " : " + message);
	}
	
	public static void d(String tag, String message){
		Date d = new Date();
		System.out.println(d.toString() + " " + tag + " : " + message);
	}
}

package projetapi.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtilitaire {
	 static DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
	
	
public static boolean verifDate(String date) {
   
    format.setLenient(false); // Le format doit être strictement le même
    try {
        format.parse(date);
    } catch (ParseException e) {
    	return false;
    }
    return true;	    

}

public static Date convertDate(String date) throws ParseException {
	return format.parse(date);
}


}

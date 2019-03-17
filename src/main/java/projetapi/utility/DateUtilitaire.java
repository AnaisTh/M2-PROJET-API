package projetapi.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class DateUtilitaire {

	
	
public static boolean verifDate(String date) {
    DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    format.setLenient(false); // Le format doit être strictement le même
    try {
        format.parse(date);
    } catch (ParseException e) {
    	return false;
    }
    return true;	    

}

public static LocalDate convertDate(String date) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	return LocalDate.parse(date, formatter);
}


}

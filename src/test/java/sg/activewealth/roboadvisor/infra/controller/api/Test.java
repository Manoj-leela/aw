package sg.activewealth.roboadvisor.infra.controller.api;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class Test {
	
	public static void main(String args[]) {
		//convertTo("2018-02-16T20:00:00Z","America/New_York","yyyy-MM-dd hh:mm:ss a");
		
		
		System.out.println(BigDecimal.ZERO.compareTo(new BigDecimal(2)));
	}
	
	public static String convertTo(String isoDateTime, String timeZone,String dateFormat) {
		ZonedDateTime fromIsoDate = ZonedDateTime.parse(isoDateTime);
		ZoneId zoneId = ZoneId.of(timeZone);
		ZonedDateTime zonedDateTime = fromIsoDate.withZoneSameInstant(zoneId);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
		return dateTimeFormatter.format(zonedDateTime);		
	}
}

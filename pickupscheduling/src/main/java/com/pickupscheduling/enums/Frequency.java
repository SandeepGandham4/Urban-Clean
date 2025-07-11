package com.pickupscheduling.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Frequency {
	DAILY,
	WEEKLY_SUNDAY,
	WEEKLY_MONDAY,
	WEEKLY_TUESDAY,
	WEEKLY_WEDNESDAY,
	WEEKLY_THURSDAY,
	WEEKLY_FRIDAY,
	WEEKLY_SATURDAY;
//	 @JsonCreator
//	    public static Frequency from(String value) {
//	        return Frequency.valueOf(value.toUpperCase());
//	    }
//
//	    @JsonValue
//	    public String getValue() {
//	        return this.name();
//	    }
	
}

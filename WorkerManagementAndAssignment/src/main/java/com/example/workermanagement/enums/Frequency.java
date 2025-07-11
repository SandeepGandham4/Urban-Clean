package com.example.workermanagement.enums;

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

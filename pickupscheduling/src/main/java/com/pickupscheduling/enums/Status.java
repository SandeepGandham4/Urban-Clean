package com.pickupscheduling.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
	 SCHEDULED,
	 COMPLETED;
//	 @JsonCreator
//	    public static Status from(String value) {
//	        return Status.valueOf(value.toUpperCase());
//	    }
//
//	    @JsonValue
//	    public String getValue() {
//	        return this.name();
//	    }
}

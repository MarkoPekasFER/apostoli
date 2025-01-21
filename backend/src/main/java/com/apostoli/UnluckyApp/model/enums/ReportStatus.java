package com.apostoli.UnluckyApp.model.enums;

public enum ReportStatus {
    FALSE, //the report is fake
    PENDING, //report is submitted - we do not know if it is a false report
    RESOLVED, //the disaster is no longer a threat
    ACTIVE //the disaster is active and is a threat
}

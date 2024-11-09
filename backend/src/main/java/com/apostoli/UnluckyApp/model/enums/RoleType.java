package com.apostoli.UnluckyApp.model.enums;

public enum RoleType {
    USER,
    RESPONDER, //rules over users
    ADMIN, //rules over responders and users
    SUPER_USER //rules over everyone
}

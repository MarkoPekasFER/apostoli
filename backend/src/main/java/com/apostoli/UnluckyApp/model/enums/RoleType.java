package com.apostoli.UnluckyApp.model.enums;

public enum RoleType {
    USER,
    ORGANISATION, //rules over its users
    RESPONDER, //rules over organisation
    ADMIN, //rules over responders and users
    SUPER_ADMIN //rules over everyone
}

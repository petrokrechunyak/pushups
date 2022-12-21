package com.alphabetas.caller.model.enums;

public enum ComandNameEnum {
    START("/start"),
    HELP("/help"),
    ADDNAME("/add_name"),
    SHOW("/show"),
    DELETE("/delete"),
    CANCEL("/cancel"),
    CONFIG("/config");

    private final String comandName;

    ComandNameEnum(String comandName) {
        this.comandName = comandName;
    }

    public String getComandName() {
        return comandName;
    }
}

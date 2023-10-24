package com.example.rest.common;

public enum BotState {
    START_STATE(null),
    CURRENCY_STATE(START_STATE),
    DATE_STATE(CURRENCY_STATE),
    CALENDAR_STATE(DATE_STATE),
    SEARCH_BY_PRODUCT(CURRENCY_STATE);


    private BotState parentState;

    BotState(BotState parentState){
        this.parentState = parentState;
    }

    public BotState getParentState(){
        return parentState;
    }
}

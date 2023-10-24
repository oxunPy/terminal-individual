package com.example.rest.constant;

import java.util.Locale;

public enum InvoiceType {
    PRIXOD_BAZA("COMING_BASE", "ПРИХОД_БАЗА", "PRIXOD_BAZA"),
    VOZVRAT_BAZA("RETURN_BASE", "ВОЗВРАТ_БАЗА", "VOZVRAT_BAZA"),
    RASXOD_KLIENT("CONSUMPTION_CLIENT", "РАСХОД_КЛИЕНТ", "RASXOD_KLIENT"),
    VOZVRAT_KLIENT("RETURN_CLIENT", "ВОЗВРАТ_КЛИЕНТ", "VOZVRAT_KLIENT"),
    ACTUAL_BALANCE("ACTUAL_BALANCE", "АКТУАЛЬНЫЙ_БАЛАНС", "AKTUALNIY BALANS"),
    GODOWN("GO_DOWN", "GO_DOWN", "GO_DOWN"),
    ITEM_GROUP_CHANGE("ITEM_GROUP_CHANGE", "ITEM_GROUP_CHANGE", "ITEM_GROUP_CHANGE"),
    BRAK("BRAK", "BRAK", "BRAK");

    private final String englishText;
    private final String russianText;
    private final String uzbekText;

    InvoiceType(String englishText, String russianText, String uzbekText){
        this.englishText = englishText;
        this.russianText = russianText;
        this.uzbekText = uzbekText;
    }

    public String getLocaleValue(Locale locale){
        switch (locale.getLanguage()){
            case "en": return englishText;
            case "ru": return russianText;
            case "uz": return uzbekText;
        }
        return russianText;
    }

}

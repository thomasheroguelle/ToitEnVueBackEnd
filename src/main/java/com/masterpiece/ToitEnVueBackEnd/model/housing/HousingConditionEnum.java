package com.masterpiece.ToitEnVueBackEnd.model.housing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum HousingConditionEnum {
    NEW("NEW"),
    PERFECT_CONDITION("PERFECT_CONDITION"),
    GOOD_CONDITION("GOOD_CONDITION"),
    NECESSARY_RENOVATION("NECESSARY_RENOVATION");

    private final String value;

    HousingConditionEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static HousingConditionEnum fromValue(String value) {
        for (HousingConditionEnum condition : values()) {

            if (condition.value.equals(value)) {
                return condition;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}

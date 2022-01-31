package jrbj.training.cookbook.ingredient.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum MeasurementUnit {
    STK("stk"),
    GRAM("g"),
    DECILITER("dl"),
    SPISESKE("spsk"),
    TESKE("tsk"),
    FED("fed");

    @Getter
    @JsonValue
    private final String shorthand;

    MeasurementUnit(String shorthand) {
        this.shorthand = shorthand;
    }
}

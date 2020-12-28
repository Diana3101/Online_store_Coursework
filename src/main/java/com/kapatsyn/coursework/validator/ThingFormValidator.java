package com.kapatsyn.coursework.validator;

import com.kapatsyn.coursework.dto.ThingDTO;
import com.kapatsyn.coursework.entity.Thing;
import com.kapatsyn.coursework.form.ThingForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ThingFormValidator implements Validator {
    @Autowired
    private ThingDTO thingDTO;

    // This validator only checks for the ThingForm.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ThingForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ThingForm thingForm = (ThingForm) target;

        // Check the fields of ThingForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.thingForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.thingForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.thingForm.price");

        String code = thingForm.getCode();
        if (code != null && code.length() > 0) {
            if (code.matches("\\s+")) {
                errors.rejectValue("code", "Pattern.thingForm.code");
            } else if (thingForm.isNewThing()) {
                Thing thing = thingDTO.findThing(code);
                if (thing != null) {
                    errors.rejectValue("code", "Duplicate.thingForm.code");
                }
            }
        }
    }

}

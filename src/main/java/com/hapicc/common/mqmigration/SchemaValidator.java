package com.hapicc.common.mqmigration;

import com.hapicc.common.json.JsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SchemaValidator<T> {

    @Autowired
    private Validator validator;

    public void validate(T[] array) throws Exception {
        for (T element : array) {
            Set<ConstraintViolation<T>> constraintViolations = validator.validate(element);

            List<String> messageStack = new ArrayList<>();

            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                messageStack.add(String.format("'%s' %s", constraintViolation.getPropertyPath(), constraintViolation.getMessage()));
            }

            if (messageStack.size() > 0) {
                String message = String.join("; ", messageStack);
                throw new Exception(String.format("for %s , below errors detected: %s", JsonHelper.gson().format(element), message));
            }
        }
    }
}

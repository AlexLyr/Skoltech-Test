package ru.scoltech.measurement.routers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.web.server.ServerWebInputException;
import ru.scoltech.measurement.model.MeasurementDto;

import java.util.stream.Collectors;

@Component
@Slf4j
public class MeasurementValidator extends CustomValidatorBean {

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);
    }

    public void validateBody(MeasurementDto measurement) {
        Errors errors = new BeanPropertyBindingResult(measurement, "measurement");
        validate(measurement, errors);
        if (errors.hasErrors()) {
            String messages = errors.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(";\n"));
            throw new ServerWebInputException(messages);
        }
    }
}
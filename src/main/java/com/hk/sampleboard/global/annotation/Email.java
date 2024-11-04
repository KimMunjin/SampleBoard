package com.hk.sampleboard.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.util.StringUtils;

import java.lang.annotation.*;
import java.util.regex.Pattern;

@Documented
@Constraint(validatedBy = Email.EmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {

    String message() default "이메일이 양식에 맞지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EmailValidator implements ConstraintValidator<Email, String> {

        private final String REGEX_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        public Pattern email = Pattern.compile(REGEX_EMAIL);

        @Override
        public boolean isValid(String s,
                               ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isEmpty(s)) {
                return true;
            } else {
                return email.matcher(s).matches();
            }
        }
    }

}

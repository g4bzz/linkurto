package com.g4bzz.linkurto.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;


@Pattern.List({
        //A regex pattern for a valid URL
        //Explanation:
        // ^                    = Start of the string
        // https?               = Matches http or https
        // :\/\/                = Matches :// (escaped /)
        // ([a-zA-Z0-9\-._~%]+) = Matches the domain (e.g., example.com, supports punycode and subdomains)
        // (:[0-9]{1,5})?       = Optional port number (e.g., :8080)
        // (\/[^\s]*)?          = Optional path (supports /path/to/resource)
        // $                    = End of the string
        @Pattern(regexp = "^(https?:\\/\\/)([a-zA-Z0-9\\-._~%]+)(:[0-9]{1,5})?(\\/[^\\s]*)?$", message = "The URL must be valid")
})
@Constraint(validatedBy = {})
@Documented
@Target({ElementType.FIELD,
        ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUrl {
    String message() default "The URL must be valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

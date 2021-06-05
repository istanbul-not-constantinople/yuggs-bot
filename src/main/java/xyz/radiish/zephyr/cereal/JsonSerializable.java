package xyz.radiish.zephyr.cereal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonSerializable {
  CaseType caseType() default CaseType.SNAKE_CASE;
}

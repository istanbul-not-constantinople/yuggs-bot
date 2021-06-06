package xyz.radiish.zephyr.cereal;

public class AnnotationInfo {
  private final String propertyName;
  private final int priority;

  public AnnotationInfo(String propertyName, int priority) {
    this.priority = priority;
    this.propertyName = propertyName;
  }

  public static AnnotationInfo of(String value, String defaultValue, CaseType caseType, int priority) {
    String charSequence = value.equals("*") ? defaultValue : value;

    if(charSequence.startsWith("get") || charSequence.startsWith("set")) {
      charSequence = charSequence.substring(3);
    } else if(charSequence.startsWith("is")) {
      charSequence = charSequence.substring(2);
    }

    return new AnnotationInfo(caseType.encase(charSequence), priority);
  }

  public String getPropertyName() {
    return propertyName;
  }

  public int getPriority() {
    return priority;
  }
}

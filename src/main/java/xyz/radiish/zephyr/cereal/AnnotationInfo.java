package xyz.radiish.zephyr.cereal;

public class AnnotationInfo {
  private String propertyName;

  public AnnotationInfo(String propertyName) {
    setPropertyName(propertyName);
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }
}

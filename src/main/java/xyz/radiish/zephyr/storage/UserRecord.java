package xyz.radiish.zephyr.storage;

import xyz.radiish.zephyr.cereal.JsonField;
import xyz.radiish.zephyr.cereal.JsonSerializable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializable
public class UserRecord {
  @JsonField("_id")
  private long id;

  @JsonField
  private List<String> permissionKeys;

  @JsonField
  private int buttonClicks;

  public UserRecord() {
    setPermissionKeys(new ArrayList<>());
    setId(0L);
  }

  public List<String> getPermissionKeys() {
    return permissionKeys;
  }

  public void setPermissionKeys(List<String> permissionKeys) {
    this.permissionKeys = permissionKeys;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "UserRecord{" +
      "id=" + id +
      ", permissionKeys=" + permissionKeys +
      '}';
  }
}

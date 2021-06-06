package xyz.radiish.zephyr.storage;

import net.dv8tion.jda.api.entities.User;
import xyz.radiish.zephyr.cereal.JsonField;
import xyz.radiish.zephyr.cereal.JsonSerializable;

import java.util.ArrayList;
import java.util.List;

@JsonSerializable
public class ZephyrUserRecord extends ProvidedRecord {
  @JsonField
  private List<String> permissionKeys;

  public ZephyrUserRecord() {
    setPermissionKeys(new ArrayList<>());
  }

  public List<String> getPermissionKeys() {
    return permissionKeys;
  }

  public void setPermissionKeys(List<String> permissionKeys) {
    this.permissionKeys = permissionKeys;
  }
}

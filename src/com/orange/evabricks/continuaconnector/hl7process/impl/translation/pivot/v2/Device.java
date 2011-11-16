package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

import java.util.HashMap;
import java.util.Map;

/**
 * Device that has taken the measure.
 * Contains a map associating the type of device item and the value of device item.
 * @author tmdn5264
 */
public class Device {

  private EIValue id;
  private String name;
  private Map<CodedInfo, Value> deviceItems;

  public Device() {
    this.deviceItems = new HashMap<CodedInfo, Value>();
  }

  public Map<CodedInfo, Value> getDeviceItems() {
    return deviceItems;
  }

  public EIValue getId() {
    return id;
  }

  public void setId(EIValue id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
}

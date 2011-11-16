package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

/**
 * Value used in a single measure or in a device item.
 * @author tmdn5264
 */
public class StringValue extends Value  {

  private String value;

  public StringValue() {
  }

  public StringValue(String value) {
    this.value = value;
  }

   public StringValue(String value, CodedInfo unit) {
    this.value = value;
    this.unit = unit;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return this.value;
  }

  @Override
  public String getValueAsString() {
    return this.value;
  }


}

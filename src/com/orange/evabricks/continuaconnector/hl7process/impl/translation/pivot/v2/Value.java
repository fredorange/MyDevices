package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

/**
 * Represents the value of a measure or of the device item.
 * A value can be a CodedValue, a StringValue, a NumericValue, etc.
 * @author tmdn5264
 */
public abstract class Value {

  protected CodedInfo unit;

  public CodedInfo getUnit() {
    return unit;
  }

  public void setUnit(CodedInfo unit) {
    this.unit = unit;
  }

  public abstract String getValueAsString();

}

package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

/**
 * Value used in a single measure or in a device item.
 * @author tmdn5264
 */
public class NumericValue extends Value {

  private double value;

  public NumericValue() {
  }

  public NumericValue(double value) {
    this.value = value;
  }

  public NumericValue(double value, CodedInfo unit) {
    this.value = value;
    this.unit = unit;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  @Override
  public String getValueAsString() {
    return String.valueOf(this.value);
  }
}

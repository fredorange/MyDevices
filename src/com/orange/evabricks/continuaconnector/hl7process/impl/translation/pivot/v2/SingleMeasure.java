package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

import java.util.Date;

/**
 * A single measure is a kind of Measure (as an AggregatedMeasures).
 * @author tmdn5264
 */
public class SingleMeasure extends Measure {

  protected Value value;

  public SingleMeasure() {
  }

  public SingleMeasure(Value value, CodedInfo measureType) {
    this();
    this.value = value;
    this.measureType = measureType;
  }

  public SingleMeasure(Value value, CodedInfo measureType, Date date) {
    this(value, measureType);
    this.date = date;
  }

  public Value getValue() {
    return value;
  }

  public void setValue(Value value) {
    this.value = value;
  }
}

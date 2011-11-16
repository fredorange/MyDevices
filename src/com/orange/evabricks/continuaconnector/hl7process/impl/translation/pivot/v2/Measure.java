package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

import java.util.Date;

/**
 * A Measure can be a single measure (SingleMeasure) or an aggregate of measures (AggregatedMeasures).
 * @author tmdn5264
 */
public abstract class Measure {

  protected CodedInfo measureType;
  protected Date date;
  protected Device device;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }

  public CodedInfo getMeasureType() {
    return measureType;
  }

  public void setMeasureType(CodedInfo measureType) {
    this.measureType = measureType;
  }


}

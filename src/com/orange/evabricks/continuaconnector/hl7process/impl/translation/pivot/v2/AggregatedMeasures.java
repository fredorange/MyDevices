package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An aggregate of measures is a kind of Measure (as a SingleMeasure).
 * An aggregate of measures is composed of one ore more measures.
 * @author tmdn5264
 */
public class AggregatedMeasures extends Measure {

  private List<Measure> measures;

  public AggregatedMeasures() {
    this.measures = new ArrayList<Measure>();
  }

  public AggregatedMeasures(CodedInfo measureType) {
    this();
    this.measureType = measureType;
  }

  public AggregatedMeasures(CodedInfo measureType, Date date) {
    this();
    this.date = date;
    this.measureType = measureType;
  }

  public AggregatedMeasures(CodedInfo measureType, Device device) {
    this();
    this.device = device;
    this.measureType = measureType;
  }

  public AggregatedMeasures(CodedInfo measureType, Date date, Device device) {
    this();
    this.date = date;
    this.device = device;
    this.measureType = measureType;
  }

  public List<Measure> getMeasures() {
    return measures;
  }
}

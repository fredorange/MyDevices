package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class representing the OBR segment of the ORU message.  
 * (The Observation Request segment serves as the header to a group of observations)
 * @author tmdn5264
 */
public class Observation {

  private Date startDate;
  private Date endDate;
  private List<AggregatedMeasures> aggregatedMeasuresCollection;

  public Observation() {
    this.aggregatedMeasuresCollection = new ArrayList<AggregatedMeasures>();
  }

    public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public List<AggregatedMeasures> getAggregatedMeasuresCollection() {
    return aggregatedMeasuresCollection;
  }

}

package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.PivotObject;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.PivotObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author tmdn5264
 */
public class Observations extends PivotObject {

  private List<Observation> observations;
  private Date messageDate;

  public Observations(){
    this.observations = new ArrayList<Observation>();
  }

  public List<Observation> getObservations() {
    return observations;
  }

  public Date getMessageDate() {
    return messageDate;
  }

  public void setMessageDate(Date messageDate) {
    this.messageDate = messageDate;
  }
 
}

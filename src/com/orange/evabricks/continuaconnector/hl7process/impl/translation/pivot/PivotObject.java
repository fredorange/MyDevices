package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot;

/***
 * 
 * @author tmdn5264
 *
 * The class that each class resulting of a "translation" (HL7 message to pivot object) has to extend.
 */
public abstract class PivotObject {

  private String messageControlId;

  public String getMessageControlId() {
    return messageControlId;
  }

  public void setMessageControlId(String messageControlId) {
    this.messageControlId = messageControlId;
  }
}

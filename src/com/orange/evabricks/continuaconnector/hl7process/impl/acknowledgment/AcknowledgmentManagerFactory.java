package com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment;

public interface AcknowledgmentManagerFactory {

  /**
   * Get the AcknowledgmentManager corresponding to the HL7 message
   * @param hl7message The HL7 message that has been processed.
   * @return The AcknowledgmentManager corresponding to the HL7 message
   */
  public AcknowledgmentManager getAcknowlegmentManager(String hl7message);
}

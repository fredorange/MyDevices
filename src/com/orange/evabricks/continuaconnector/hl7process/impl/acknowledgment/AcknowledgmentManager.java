package com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;

/***
 * Manage the generation of the HL7 acknowledgment message.
 * @author tmdn5264
 *
 */
public interface AcknowledgmentManager {

  /***
   * Generates the acknowledgment message.
   * @param success True if the message has been correctly sent. False if an error occurred.
   * @param errors The error that occurred if message processing failed.
   * @return The HL7 acknowledgment message.
   * @throws ContinuaConnectorException If an exception is thrown, the acknowledgment failed. Else, it worked.
   */
  public String acknowledge(String initialHl7Message, boolean success, ContinuaConnectorException exception) throws ContinuaConnectorException;
}

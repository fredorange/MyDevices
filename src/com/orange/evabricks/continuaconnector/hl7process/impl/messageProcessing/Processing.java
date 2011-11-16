package com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;

/**
 *
 * @author tmdn5264
 */
public interface Processing {

  /**
   * Process the HL7 message.
   * @param ahd_idd The id used by the AHD to identify the indivo record.
   * @param hl7Message The HL7 message to send to the PHR.
   * @throws ContinuaConnectorException If there was an error, an exception is thrown. Else, it worked.
   */
  public void processMessage(String accessKey, String hl7Message) throws ContinuaConnectorException;

  /**
   * Process the HL7 message.
   * @param hl7Message The HL7 message to send to the PHR.
   * @throws ContinuaConnectorException If there was an error, an exception is thrown. Else, it worked.
   */
  public void processMessage(String hl7Message) throws ContinuaConnectorException;

  /**
   * Process the HL7 message.
   * @param hl7Message The HL7 message to send to the PHR.
   * @return A hl7 ack message, which indicates if processing message worked or if there were errors.
   */
  public String processMessageAndReturnHL7Ack(String accessKey, String hl7Message);

  /**
   * Process the HL7 message.
   * @param hl7Message The HL7 message to send to the PHR.
   * @return A hl7 ack message, which indicates if processing message worked or if there were errors.
   */
  public String processMessageAndReturnHL7Ack(String hl7Message);
}

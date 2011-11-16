package com.orange.evabricks.continuaconnector.hl7process.api;


/**
 * Interface for all HL7 message processor subsystems.<br>
 * An HL7 Message processor is a subsystem that take as input HL7 messages,
 * process them and produce an HL7 message response describing if the submitted
 * input was successfully processed or not.<br>
 * <u>Note</u>: Content of HL7 messages is application scoped.
 * @author alain
 */
public interface HL7MessageProcessor {

  /**
   * Process an HL7 message.
   * This message is related to the patient identified by the PId entry in the 
   * HL7 message
   * @param hl7Msg HL7 message to process
   * @return HL7 response for the processed message
   */
  String process(String hl7Msg);

  /**
   * Process an HL7 message on behalf of the specified patient.
   * Since the patient is specified the Pid entry in the HL7 message isn't 
   * relevant and hence ignored.
   * @param hl7Msg HL7 message to process
   * @param onUserBehalf This HL7 message is related to the specified 
   *                        user/patient.
   * @return HL7 response for the processed message
   */
  String process(String hl7Msg, String onUserBehalf);
}

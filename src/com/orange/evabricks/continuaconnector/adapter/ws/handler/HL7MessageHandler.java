package com.orange.evabricks.continuaconnector.adapter.ws.handler;

import javax.xml.ws.WebServiceContext;

/**
 * Contract for any implementer of HL7 message handlers.
 * An HL7 message handler is called upon an incoming web service request.
 * @author alain
 */
public interface HL7MessageHandler {

  /**
   * Handle an incoming HL7 message.
   * @param hl7Msg HL7 message
   * @param context Web service context tied to the HL7 message
   * @return HL7 message result describing the result for the processed
   * incoming message
   */
  String handle(String hl7Msg, WebServiceContext context);
}

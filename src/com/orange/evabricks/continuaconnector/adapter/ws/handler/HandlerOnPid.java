package com.orange.evabricks.continuaconnector.adapter.ws.handler;

import javax.xml.ws.WebServiceContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * HL7 message handler.
 * This handler use the Pid located in the HL7 message to identify the patient
 * @author alain
 */
@Service("handlerOnPid")
public class HandlerOnPid extends HandlerAbstract {

  private static Logger log = Logger.getLogger(HandlerOnPid.class);

  @Override
  public final String handle(String hl7Msg, WebServiceContext context) {
    log.debug("Handling hl7 message ...");
    String res = process(hl7Msg);
    log.debug("Hl7 message handled.");
    return res;
  }
}

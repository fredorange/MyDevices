package com.orange.evabricks.continuaconnector.adapter.tooling.hl7processor;

import com.orange.evabricks.continuaconnector.hl7process.api.HL7MessageProcessor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * A no-op hl7 processor. Just mirroring incoming HL7 messages
 * @author alain
 */

@Service("hl7Mirror")
public class HL7ProcessorMirrorImpl implements HL7MessageProcessor {

  private static Logger log = Logger.getLogger(HL7ProcessorMirrorImpl.class);

  @Override
  public  String process(String hl7Msg) {
    log.debug("Processing hl7Message: " + hl7Msg);
    return "[]" + hl7Msg;
  }

  @Override
  public final String process(String hl7Msg, String onUserBehalf) {
    log.debug("Processing hl7Message for user " + onUserBehalf + " : " + hl7Msg);
    return "[" + onUserBehalf + "]" + hl7Msg;
  }
}

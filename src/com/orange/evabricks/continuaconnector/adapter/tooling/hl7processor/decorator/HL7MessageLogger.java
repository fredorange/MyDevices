package com.orange.evabricks.continuaconnector.adapter.tooling.hl7processor.decorator;


import org.apache.log4j.Logger;

/**
 *
 * @author alain
 */
//@Service
public class HL7MessageLogger extends HL7MessageDecoratorAbstract {

  private static Logger log = Logger.getLogger(HL7MessageLogger.class);

  @Override
  public final String process(String hl7Msg) {
    log.debug(hl7Msg);
    return super.forward(hl7Msg);
  }

  @Override
  public final String process(String hl7Msg, String onUserBehalf) {
    log.debug(hl7Msg);
    return super.forward(hl7Msg, onUserBehalf);
  }
 
}

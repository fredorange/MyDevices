package com.orange.evabricks.continuaconnector.adapter.tooling.hl7processor.decorator;



import com.orange.evabricks.continuaconnector.adapter.tooling.hl7processor.util.MessageCorrector;
import org.apache.log4j.Logger;

/**
 * Transform on the fly HL7Messages in order to ad/remove \r and \n
 * Since altering message content in this way is definitively is bad practice 
 * its usage is a deprecated and this class is only provided during transitional
 * phase
 * @author alain
 * @deprecated 
 */
//@Service
public class HL7MessageTirlipoteur extends HL7MessageDecoratorAbstract {

  private static Logger log = Logger.getLogger(HL7MessageTirlipoteur.class);
  
   
  @Override
  public String process(String hl7Msg) {
    log.debug("Ensuring extended HL7 message compatibility...");
    String tirlipoted = MessageCorrector.correctWebserviceMessage2HL7Message(hl7Msg);
    return this.hl7Delegate.process(tirlipoted);
  }

  @Override
  public String process(String hl7Msg, String onUserBehalf) {
    log.debug("Ensuring extended HL7 message compatibility...");
    String tirlipoted = MessageCorrector.correctWebserviceMessage2HL7Message(hl7Msg);
    return this.hl7Delegate.process(tirlipoted);
  }
}

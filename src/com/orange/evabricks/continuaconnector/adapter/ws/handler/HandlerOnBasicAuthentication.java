package com.orange.evabricks.continuaconnector.adapter.ws.handler;

import javax.enterprise.inject.Alternative;
import javax.xml.ws.WebServiceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author alain
 */
@Alternative
public class HandlerOnBasicAuthentication extends HandlerAbstract {

  private static Logger log = Logger.getLogger(HandlerOnBasicAuthentication.class);

  @Override
  public final String handle(String hl7Msg, WebServiceContext context) {
    log.debug("Starting to handle hl7 message...");
    String accessKey = this.extractAccessKey(context);
    log.info("accessKey=" + accessKey);
    String res = process(hl7Msg, accessKey);
    //String res = "[hdlBA,"+accessKey+"]"+hl7Msg;
    log.debug("HL7 message processed");
    return res;
  }

  private String extractAccessKey(WebServiceContext context) {
    if(context.getUserPrincipal()==null){
      log.warn("User principal not defined for the web service context. Hence we're unable to guess the accessKey. Assigning a null value");
      return null;
    }
   String res  = context.getUserPrincipal().getName();
   if(res==null){
     log.warn("Name isn't available in the user principal. Hence we're unable to guess the accessKey. keeping the detected null value");
   }
   return res;
  }
}

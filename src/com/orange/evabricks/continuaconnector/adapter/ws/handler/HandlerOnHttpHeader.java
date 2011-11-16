package com.orange.evabricks.continuaconnector.adapter.ws.handler;


import com.sun.xml.ws.transport.Headers;
import javax.xml.ws.WebServiceContext;
import org.apache.log4j.Logger;

/**
 *
 * @author alain
 */
public class HandlerOnHttpHeader extends HandlerAbstract {

  /**
   * Access key uniquely identifying the patient expected under this HTTP header entry
   */
  public static final String ACCESS_KEY = "continuaConnector-accessKey";
  private static Logger log = Logger.getLogger(HandlerOnHttpHeader.class);

  @Override
  public final String handle(String hl7Msg, WebServiceContext context) {
    log.debug("Starting to handle hl7 message...");
    // Note: we do not assert that the accesskey is defined.
    // Instead we may pass null as accesskey to the HL7 processor if we're unabled to find it.
    // It is the responsability of the HL7 processor to handle this case.
    // (because le contrat du WS ne prevoit pas d'erreur/fault...)
    String accessKey = this.extractAccessKey(context);
    log.info("accessKey="+accessKey+" / HL7 message: " + hl7Msg);
    String res = process(hl7Msg, accessKey);
    log.debug("HL7 message processed");
    return res;
  }

  private String extractAccessKey(WebServiceContext context)  {
    final String KEY_HEADERS = "javax.xml.ws.http.request.headers";

    String res = null;
    Headers hdrs = (Headers) context.getMessageContext().get(KEY_HEADERS);
    if(hdrs.containsKey(ACCESS_KEY)) {
      res = hdrs.getFirst(ACCESS_KEY);
    } else {
      // no error, just return null;
      log.warn("hl7 connector access key cannot be found in HTTP header of the request: "+ACCESS_KEY);
    }
    return res;
  }
}

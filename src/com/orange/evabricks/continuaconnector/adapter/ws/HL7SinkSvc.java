package com.orange.evabricks.continuaconnector.adapter.ws;


import com.orange.evabricks.continuaconnector.adapter.ws.handler.HandlerAbstract;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * SEI for the HL7 connector Web Service.
 * @author alain
 */
//@WebService(serviceName = "DeviceObservationConsumer_Service", portName = "DeviceObservationConsumer_Port_Soap12",         endpointInterface = "ihe.pcd.dec._2010.DeviceObservationConsumerPortType",         targetNamespace = "urn:ihe:pcd:dec:2010",         wsdlLocation = "WEB-INF/wsdl/DeviceObservationConsumer.wsdl")
@WebService(serviceName = "DeviceObservationConsumer_Service",
portName = "DeviceObservationConsumer_Port_Soap12",
endpointInterface = "ihe.pcd.dec._2010.DeviceObservationConsumerPortType",
targetNamespace = "urn:ihe:pcd:dec:2010",
wsdlLocation = "WEB-INF/wsdl/DeviceObservationConsumer.wsdl")
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class HL7SinkSvc/* implements ihe.pcd.dec._2010.DeviceObservationConsumerPortType */ {

  private static Logger log = Logger.getLogger(HL7SinkSvc.class);
  @Resource
  private WebServiceContext context;

 /**
   * Continua contract
   * @param body incoming HL7 message
   * @return HL7 message as a result of the processed incoming HL7 message
   */
  public final java.lang.String communicatePCDData(final java.lang.String body) {
    log.debug("Asked to process this HL7 message:\n" + body);
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(
            (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT));
    HandlerAbstract hdl = wac.getBean("hl7Handler", HandlerAbstract.class);
    String res = hdl.handle(body, context);
    log.debug("Answer is: " + res);
    return res;
  }
}

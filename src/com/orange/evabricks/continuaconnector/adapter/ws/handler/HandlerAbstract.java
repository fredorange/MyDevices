package com.orange.evabricks.continuaconnector.adapter.ws.handler;

import com.orange.evabricks.continuaconnector.hl7process.api.HL7MessageProcessor;


/**
 * Base class of HL7 message handler implementations.
 * This class aggregates an HL7MessageProcessor who's defining how to process incoming HL7 messages.
 * @author alain
 */
public abstract class HandlerAbstract implements HL7MessageHandler {

  private HL7MessageProcessor processor;
  private String data;

  public final void setHl7MessageProcessor(HL7MessageProcessor processor) {
    this.processor = processor;
  }

  public final void setData(String data) {
    this.data = data;
  }
  public final String getData(){
    return this.data;
  }
  /**
   * Process an HL7 message.
   * The patient is identified by the PId located inside the HL7 message.<br>
   * Processing of the HL7 message is done accordingly to the defined HL7 processor
   * @param hl7Msg HL7 message to process
   * @return Response for the processed HL7 message. (also an HL7 message)
   */
  protected final String process(String hl7Msg) {
    assert processor!=null;
    return this.processor.process(hl7Msg);
  }

  /**
   * Process an HL7 message.
   * The patient is identified by the extra parameter <code>onBehalfOf</code>.
   * Hence the PId located inside the HL7 message is ignored.<br>
   * Processing of the HL7 message is done accordingly to the defined HL7 processor
   * @param hl7Msg HL7 message to process
   * @param onBehalfOf The hl7 message pertains to this patient
   * @return Response for the processed HL7 message. (also an HL7 message)
   */
  protected final String process(String hl7Msg, String onBehalfOf) {
    return this.processor.process(hl7Msg, onBehalfOf);
  }
}

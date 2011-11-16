package com.orange.evabricks.continuaconnector.hl7process.impl;

import com.orange.evabricks.continuaconnector.hl7process.api.HL7MessageProcessor;
import com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing.impl.ProcessingImpl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Adaptateur pour le code legacy deja produit.
 * @author alain
 */
@Service(value = "hl7MessageProcessorImpl")
public class HL7MessageProcessImpl implements HL7MessageProcessor {

  @Resource(name = "processing")
  private ProcessingImpl delegate;

  @Override
  public String process(String hl7Msg) {
    return this.delegate.processMessageAndReturnHL7Ack(hl7Msg);
  }

  @Override
  public String process(String hl7Msg, String onUserBehalf) {
    return this.delegate.processMessageAndReturnHL7Ack(onUserBehalf, hl7Msg);
  }
}

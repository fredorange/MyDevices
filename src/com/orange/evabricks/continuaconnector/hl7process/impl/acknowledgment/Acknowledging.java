package com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import javax.annotation.Resource;

@Component("acknowledging")
@Scope("singleton")
public class Acknowledging {

  @Resource(name = "acknowledgmentManagerFactory")
  private AcknowledgmentManagerFactory acknowledgmentManagerFactory;

  /**
   * Manages acknowledgement (selects the adequate acknowledgmentManager, generates and return ack)
   * @param initialHl7Message The initial message to acknowledge
   * @param success A boolean indicating if the processing of the message successed or not (determines the content of the ack)
   * @param exception The exception that has been thrown if message processing failed
   * @return A String representing the acknowledgment message
   * @throws ContinuaConnectorException If an exception is thrown, the acknowledgment failed. Else, it worked.
   */
  public String acknowledge(String initialHl7Message, boolean success, ContinuaConnectorException exception) throws ContinuaConnectorException {
    return acknowledgmentManagerFactory.getAcknowlegmentManager(initialHl7Message).acknowledge(initialHl7Message, success, exception);
  }
}

package com.orange.evabricks.continuaconnector.adapter.tooling.hl7processor.decorator;

import com.orange.evabricks.continuaconnector.hl7process.api.HL7MessageProcessor;
import org.springframework.stereotype.Service;

/**
 * Base class for all decorators related to HL7 message processing.
 * @author alain
 */
@Service
public abstract class HL7MessageDecoratorAbstract implements HL7MessageProcessor {

  /**
   * Target HL7 message processor
   */
  protected HL7MessageProcessor hl7Delegate;

  /**
   * Sets the HL7 message processor used by this decorator
   * @param delegate Target HL7 message processor
   */
  public final void setDelegate(final HL7MessageProcessor delegate) {
    this.hl7Delegate = delegate;
  }

  /**
   * Forward requested action to the target HL7 message processor
   * @param hl7Msg HL7 message to process
   * @return An HL7 message, result for the processed HL7 message
   */
  protected final String forward(final String hl7Msg) {
    return this.hl7Delegate.process(hl7Msg);
  }

  /**
   * Forward requested action to the target HL7 message processor
   * @param hl7Msg HL7 message to process
   * @param onUserBehalf Process the HL7 message with this user declared as sender
   * @return An HL7 message, result for the processed HL7 message
   */
  protected final String forward(final String hl7Msg, final String onUserBehalf) {
    return this.hl7Delegate.process(hl7Msg, onUserBehalf);
  }
}

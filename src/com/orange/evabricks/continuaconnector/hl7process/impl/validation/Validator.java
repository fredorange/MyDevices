package com.orange.evabricks.continuaconnector.hl7process.impl.validation;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;

/***
 * Verifying if the HL7 message is correct.
 * @author tmdn5264
 *
 */
public interface Validator {

  /***
   * Verify if the HL7 message is correct.
   * @param hl7Message HL7 message
   * @throws ContinuaConnectorException If an exception is thrown, the validation failed. Else, it worked.
   */
  public abstract void validate(String hl7Message) throws ContinuaConnectorException;
}

package com.orange.evabricks.continuaconnector.hl7process.impl.commons;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnknownKeyIdentifierException;

/**
 *
 * @author tmdn5264
 */
public interface QuickHl7FieldExtractor {

  /**
   * Extract pid from HL7 message
   * @param hl7Message
   * @return the pid
   * @throws UnknownKeyIdentifierException
   */
  public String getPidFromHL7Message(String hl7Message) throws ContinuaConnectorException;
}

package com.orange.evabricks.continuaconnector.hl7process.impl.translation;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.PivotObject;

/***
 * Parsing HL7 message into pivot object.
 * @author tmdn5264
 *
 */
public interface Translator {

  /***
   * Parse HL7 message into pivot object.
   * @param hl7Message
   * @return A pivot object
   * @throws ContinuaConnectorException If an exception is thrown, the translation failed. Else, it worked.
   */
  public abstract PivotObject translate(String hl7Message) throws ContinuaConnectorException;
}

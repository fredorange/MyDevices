package com.orange.evabricks.continuaconnector.hl7process.impl.integration;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.PivotObject;

/***
 * 
 * @author tmdn5264
 *
 * Sending data to the PHR.
 */
public interface Integrator {

  /***
   * Send data to the PHR.
   * @param accessKey The id used by the AHD to identify the indivo record.
   * @param pivotObject
   * @throws ContinuaConnectorException If  an exception is thrown, the integration failed. Else, it worked.
   */
  public abstract void integrate(String accessKey, PivotObject pivotObject) throws ContinuaConnectorException;
}

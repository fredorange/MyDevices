package com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.jlinx.Indivo;

public interface IndivoMgr {

  /**
   * Get Indivo object
   * @return Indivo object
   * @throws ContinuaConnectorException
   */
  public Indivo getIndivo() throws ContinuaConnectorException;
}

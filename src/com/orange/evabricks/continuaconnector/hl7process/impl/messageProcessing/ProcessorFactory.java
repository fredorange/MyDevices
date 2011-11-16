package com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;

/**
 *
 * @author tmdn5264
 */
public interface ProcessorFactory {

  /***
   * This method instantiates a Processor corresponding to the type of the HL7 message
   * @param hl7message
   * @return
   * @throws ContinuaConnectorException
   */
  public Processor getProcessor(String hl7message) throws ContinuaConnectorException;
}

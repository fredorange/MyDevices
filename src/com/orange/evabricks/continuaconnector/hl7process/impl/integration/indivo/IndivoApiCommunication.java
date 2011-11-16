package com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.model.MyIndivoGroupedVitalSignsAndEquipment;
import java.util.List;

/***
 * 
 * @author tmdn5264
 * 
 *         Using the indivo_api.
 */
public interface IndivoApiCommunication {

   public void postObservations(String accessKey, List<MyIndivoGroupedVitalSignsAndEquipment> observations) throws ContinuaConnectorException;

  public void sendNotification(String accessKey, String text) throws ContinuaConnectorException;

  public void archiveAllDocuments(String accessKey) throws ContinuaConnectorException;
}

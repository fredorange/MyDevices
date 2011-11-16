package com.orange.evabricks.continuaconnector.hl7process.impl.integration.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.IndivoApiCommunication;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.Integrator;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.impl.parsing.ParsingPivotToIndivoObject;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.PivotObject;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2.Observations;

/**
 *
 * @author tmdn5264
 */
@Component("oRU_R01Integrator")
public class ORU_R01Integrator implements Integrator {

  private static final Logger logger = Logger.getLogger(ORU_R01Integrator.class);
  @javax.annotation.Resource(name = "indivoApiCommunication")
  private IndivoApiCommunication indivoApiCommunication;
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;
  @Resource(name = "parsingPivotToIndivoObject")
  private ParsingPivotToIndivoObject parsingPivotToIndivoObject;

  @Override
  public void integrate(String accessKey, PivotObject pivotObject) throws ContinuaConnectorException {
    logger.debug("Start executing integrate ...");

    if (pivotObject == null) {
      logger.warn("Pivot object should not be null.");
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINTEGRATION_DevError_NullPivotObjectError());
    }

    if (!(pivotObject instanceof Observations)) {
      logger.warn("Pivot object shoud be an instance of DeviceObservation.");
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINTEGRATION_DevError_BadPivotObjectType().setValueComplement("Expected: " + Observations.class.getName() + " Found: " + pivotObject.getClass().getName()));
    }

    Observations observations = (Observations) pivotObject;


    processIntegration(accessKey, observations);
  }

  /**
   * Call indivoApiCommunication to post data in Indivo
   * @param accessKey
   * @param pivotObject
   * @throws ApplicationInternalErrorException
   */
  private void processIntegration(String accessKey, Observations pivotObject) throws ContinuaConnectorException {
    logger.debug("Start executing processIntegration ...");

    try {
      logger.info("Post DeviceObservation : " + pivotObject.toString() + " for accessKey " + accessKey);
      indivoApiCommunication.postObservations(accessKey, parsingPivotToIndivoObject.getVitalSignsFromObservations(pivotObject));
    } catch (ContinuaConnectorException e) {
      throw e;
    } catch (Exception e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINTEGRATION(), e);
    }
  }
}

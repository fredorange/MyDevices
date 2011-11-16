package com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing;

import org.apache.log4j.Logger;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.Integrator;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.Translator;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.PivotObject;
import com.orange.evabricks.continuaconnector.hl7process.impl.validation.Validator;

/***
 * 
 * @author tmdn5264
 *
 * This class manages all the treatments that will be done on the HL7 message :
 * validation (verifying id the message is a correct HL7 message)
 * translation (parsing HL7 message into pivot object)
 * integration (sending informations contained into the pivot object to the PHR) 
 */
public class Processor {

  private static final Logger logger = Logger.getLogger(Processor.class);
  private String hl7Message;
  protected Validator validator;
  protected Translator translator;
  protected Integrator integrator;

  public Processor(String hl7Message, Validator validator, Translator translator, Integrator integrator) {
    this.hl7Message = hl7Message;
    this.validator = validator;
    this.translator = translator;
    this.integrator = integrator;
  }

  /***
   * This method manages all the treatments that will be done on the HL7 message :
   * validation (verifying id the message is a correct HL7 message)
   * translation (parsing HL7 message into pivot object)
   * integration (sending informations contained into the pivot object to the PHR)
   * @param accessKey The id used by the AHD to identify the indivo record.
   * @throws ContinuaConnectorException If processing message failed, exceptions are thrown. Else, it means processing worked.
   */
  public void processMessage(String accessKey) throws ContinuaConnectorException {

    logger.info("Start to validate ...");
    validator.validate(hl7Message);
    logger.info("VALIDATION : DONE");

    logger.info("Start to translate ...");
    PivotObject pivotObject = translator.translate(hl7Message);
    logger.info("TRANSLATION : DONE");
    
    if (pivotObject != null) {
      logger.info("Start to integrate ...");
      integrator.integrate(accessKey, pivotObject);
      logger.info("INTEGRATION : DONE");
    }

  }
}

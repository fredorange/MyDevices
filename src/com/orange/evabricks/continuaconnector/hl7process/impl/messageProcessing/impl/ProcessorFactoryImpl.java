package com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing.impl;

import ca.uhn.hl7v2.model.Message;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.commons.HL7MessageParser;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedEventCodeException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedMessageTypeException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedVersionIdException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.Integrator;
import com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing.Processor;
import com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing.ProcessorFactory;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.Translator;
import com.orange.evabricks.continuaconnector.hl7process.impl.validation.Validator;

/***
 * 
 * @author tmdn5264
 *
 * The ProcessorFactory creates the Processor object corresponding to the type of the HL7 message received.
 */
@Component("processorFactory")
@Scope("singleton")
public class ProcessorFactoryImpl implements ProcessorFactory {

  private static final Logger logger = Logger.getLogger(ProcessorFactoryImpl.class);
  @Resource(name = "oRU_R01Validator")
  private Validator oRU_R01Validator;
  @Resource(name = "oRU_R01Translator")
  private Translator oRU_R01Translator;
  @Resource(name = "oRU_R01Integrator")
  private Integrator oRU_R01Integrator;
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;
  @Resource(name = "hl7MessageParser")
  private HL7MessageParser hl7MessageParser;

  public ProcessorFactoryImpl() {
  }

  @Override
  public Processor getProcessor(String hl7message) throws ContinuaConnectorException {

    logger.info("Start to get the right processor ...");

    if (hl7message == null) {
      logger.debug("Unable to get a processor. HL7 message is null.");
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMONPARSING_NullMessageError());
    }

    Message hapiMessage = hl7MessageParser.getHapiMessageFromHl7Message(hl7message);
    if (!"2.6".equals(hapiMessage.getVersion())) {
      throw new UnsupportedVersionIdException(applicationErrorCodes.getVALIDATION_UnsupportedHL7Version().setValueComplement(" Found: " + hapiMessage.getVersion() + " Expected: 2.6"));
    }

    if (hl7MessageParser.isORU_R01(hl7message)) {
      logger.debug("Processor choosen : ORU_R01.");
      return new Processor(hl7message, oRU_R01Validator, oRU_R01Translator, oRU_R01Integrator);
    }
    if (hl7MessageParser.isORU_notR01(hl7message)) {
      throw new UnsupportedEventCodeException(applicationErrorCodes.getCOMMONPARSING_UnsupportedEventCode());
    }
    logger.debug("Unable to get a processor : unsupported message type.");
    throw new UnsupportedMessageTypeException(applicationErrorCodes.getCOMMONPARSING_UnsupportedMessageType());
  }
}

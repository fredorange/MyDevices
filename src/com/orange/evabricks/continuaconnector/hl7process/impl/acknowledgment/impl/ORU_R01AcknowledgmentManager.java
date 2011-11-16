package com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ca.uhn.hl7v2.model.v26.message.ORU_R01;

import com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.AcknowledgmentManager;
import com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.util.AckGenerator;
import com.orange.evabricks.continuaconnector.hl7process.impl.commons.HL7MessageParser;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.Error;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.hl7ErrorCodes.HL7Table0357ErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import javax.annotation.Resource;

@Component("oRU_R01AcknowledgmentManager")
public class ORU_R01AcknowledgmentManager implements AcknowledgmentManager {

  private static final Logger logger = Logger.getLogger(ORU_R01AcknowledgmentManager.class);
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;
  @Resource(name = "hl7MessageParser")
  private HL7MessageParser hl7MessageParser;
  @Resource(name = "ackGenerator")
  private AckGenerator ackGenerator;

  @Override
  public String acknowledge(String initialHl7Message, boolean success, ContinuaConnectorException exception) throws ContinuaConnectorException {

    logger.debug("Start executing acknowledge ...");

    if (initialHl7Message == null) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getACK_DevError_NullMessageError());
    }

    ORU_R01 initialORUMessage = hl7MessageParser.getORU_R01(initialHl7Message);

    if (initialORUMessage == null) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getACK_DevError_BadInputMessageType().setValueComplement(ORU_R01.class.getName()));
    }

    String ackSuccessCode = ackGenerator.getAckCode(success, exception);

    Error error = null;
    if (exception != null) {
      ErrorCode hl7ErrorCode = HL7Table0357ErrorCodes.getErrorCodeByException(exception);
      logger.debug("hl7ErrorCode : " + hl7ErrorCode);
      String severity = "E";
      ErrorCode applicationErrorCode = exception.getApplicationErrorCode();
      logger.debug("applicationErrorCode : " + applicationErrorCode);
      String diagnosticInformation = exception.toString();
      logger.debug("diagnosticInformation : " + diagnosticInformation);
      error = new Error(hl7ErrorCode, severity, applicationErrorCode, diagnosticInformation);
    }


    String event = null;

    if (initialORUMessage != null && initialORUMessage.getMSH() != null && initialORUMessage.getMSH().getMsh9_MessageType() != null && initialORUMessage.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent() != null && initialORUMessage.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue() != null) {
      event = initialORUMessage.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue();
    }
    logger.debug("Trigger event : " + event);

    return ackGenerator.generateAck(initialORUMessage, event, ackSuccessCode, error);
  }
}

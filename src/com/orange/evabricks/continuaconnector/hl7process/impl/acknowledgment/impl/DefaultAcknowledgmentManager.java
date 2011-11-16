package com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.impl;


import ca.uhn.hl7v2.model.Message;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.AcknowledgmentManager;
import com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.util.AckGenerator;
import com.orange.evabricks.continuaconnector.hl7process.impl.commons.HL7MessageParser;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.Error;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.hl7ErrorCodes.HL7Table0357ErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import javax.annotation.Resource;

/**
 * This AcknowledgmentManager is use when the type of hl7 message is not managed by the others AcknowledgmentManagers.
 * @author tmdn5264
 *
 */
@Component("defaultAcknowledgmentManager")
public class DefaultAcknowledgmentManager implements AcknowledgmentManager {

  private static final Logger logger = Logger.getLogger(DefaultAcknowledgmentManager.class);
  @Resource(name = "ackGenerator")
  private AckGenerator ackGenerator;
    @Resource(name = "hl7MessageParser")
  private HL7MessageParser hl7MessageParser;

  @Override
  public String acknowledge(String initialHl7Message, boolean success, ContinuaConnectorException exception) {
    logger.debug("Start executing acknowledge ...");

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

    Message hapiMessage = null;
    try {
      hapiMessage = hl7MessageParser.getHapiMessageFromHl7Message(initialHl7Message);
    } catch (ContinuaConnectorException e) {
      logger.error("Failed to parse message into hapi message in acknowledgemnt module.", e);
    }

    return ackGenerator.generateAck(hapiMessage, null, ackSuccessCode, error);
  }
}

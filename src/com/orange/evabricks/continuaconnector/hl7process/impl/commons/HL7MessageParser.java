package com.orange.evabricks.continuaconnector.hl7process.impl.commons;

import org.apache.log4j.Logger;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.message.ORU_R30;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/***
 * 
 * @author tmdn5264
 *
 * Contains a method to parse an HL7 message into a Hapi message.
 * Contains methods to get the type of an HL7 message ('x : ORU_R01).
 * Contains methods to get a typed message (ex : ORU_R01) from a Hapi message
 */
@Component("hl7MessageParser")
@Scope("singleton")
public class HL7MessageParser {

  private static final Logger logger = Logger.getLogger(HL7MessageParser.class);
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;

  /***
   * Parse an HL7 message into a Hapi message
   * @param hl7Message The HL7 message (as String)
   * @return A Hapi message
   * @throws ContinuaConnectorException
   */
  public Message getHapiMessageFromHl7Message(String hl7Message) throws ContinuaConnectorException {

    if (hl7Message == null) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMONPARSING_NullMessageError());
    }

    Message hapiMessage = null;

    GenericParser parser = new GenericParser();
    parser.setValidationContext(new ca.uhn.hl7v2.validation.impl.NoValidation());
    try {
      hapiMessage = parser.parse(hl7Message);
    } catch (EncodingNotSupportedException e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMONPARSING_ParsingError(), e);
    } catch (HL7Exception e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMONPARSING_ParsingError(), e);
    }

    return hapiMessage;
  }

  /***
   * To know if the HL7 message is a ORU_R01 message or not.
   * @param hapiMessage Hapi message
   * @return True if the message is an ORU_R01. Else false.
   */
  public boolean isORU_R01(Message hapiMessage) {
    if (hapiMessage == null) {
      return false;
    }
    return (hapiMessage instanceof ORU_R01)
            && ((ORU_R01) (hapiMessage)).getMSH().getMessageType().getTriggerEvent() != null
            && ((ORU_R01) (hapiMessage)).getMSH().getMessageType().getTriggerEvent().getValue() != null
            && "R01".equalsIgnoreCase((((ORU_R01) (hapiMessage)).getMSH().getMessageType().getTriggerEvent().getValue()));
  }

  public boolean isORU_notR01(Message hapiMessage) {
    if (hapiMessage == null) {
      return false;
    }
    // example : ORU^R30^ORU_R30, ORU^R31^ORU_R30, ORU^R32^ORU_R30
    if (hapiMessage instanceof ORU_R30) {
      return true;
    }
    // example : ORU^R02^ORU_R01
    if ((hapiMessage instanceof ORU_R01)
            && ((ORU_R01) (hapiMessage)).getMSH().getMessageType().getTriggerEvent() != null
            && ((ORU_R01) (hapiMessage)).getMSH().getMessageType().getTriggerEvent().getValue() != null
            && !"R01".equalsIgnoreCase((((ORU_R01) (hapiMessage)).getMSH().getMessageType().getTriggerEvent().getValue()))) {
      return true;
    }
    return false;

  }

  /***
   * To know if the HL7 message is a ORU_R01 message or not.
   * @param hl7Message HL7 message
   * @return True if the message is an ORU_R01. Else false.
   */
  public boolean isORU_R01(String hl7Message) {
    if (hl7Message == null) {
      return false;
    }

    Message hapiMessage;
    try {
      hapiMessage = getHapiMessageFromHl7Message(hl7Message);
      return isORU_R01(hapiMessage);
    } catch (ContinuaConnectorException e) {
      logger.error("Parsing message failed.", e);
    }
    return false;

  }

  public boolean isORU_notR01(String hl7Message) {
    if (hl7Message == null) {
      return false;
    }

    Message hapiMessage;
    try {
      hapiMessage = getHapiMessageFromHl7Message(hl7Message);
      return isORU_notR01(hapiMessage);
    } catch (ContinuaConnectorException e) {
      logger.error("Parsing message failed.", e);
    }
    return false;

  }

  /***
   * Parse a generic Hapi message into a ORU_R01 message.
   * @param hapiMessage
   * @return A ORU_R01 object if the Hapi message is a ORU_R01. Else return null.
   */
  public ORU_R01 getORU_R01(Message hapiMessage) {
    if (hapiMessage == null) {
      return null;
    }
    if (isORU_R01(hapiMessage)) {
      return (ORU_R01) hapiMessage;
    } else {
      return null;
    }
  }

  /***
   * Parse a HL7 message into a ORU_R01 message.
   * @param hl7Message
   * @return A ORU_R01 object if the Hapi message is a ORU_R01. Else return null.
   */
  public ORU_R01 getORU_R01(String hl7Message) {
    if (hl7Message == null) {
      return null;
    }
    try {
      if (isORU_R01(hl7Message)) {
        return (ORU_R01) getHapiMessageFromHl7Message(hl7Message);
      }
    } catch (ContinuaConnectorException e) {
      logger.error("Parsing message failed.", e);
    }
    return null;
  }
}

package com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.model.v26.segment.ERR;
import ca.uhn.hl7v2.model.v26.segment.MSA;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import com.orange.evabricks.continuaconnector.hl7process.impl.config.AckConfig;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.Error;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationRecordLockedException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.DataTypeErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.RequiredFieldMissingException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.SegmentSequenceErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.TableValueNotFoundException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnknownKeyIdentifierException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedEventCodeException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedMessageTypeException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedProcessingIdException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedVersionIdException;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("ackGenerator")
@Scope("singleton")
public class AckGenerator {

  private static final Logger logger = Logger.getLogger(AckGenerator.class);
  private static SimpleDateFormat simpleDateHeureFormat = new SimpleDateFormat("yyyyMMddHHmm");
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;
  @Resource(name = "ackConfig")
  private AckConfig ackConfig;

  /**
   * Generates the ACK message.
   * @param initialHapiMessage The initial Hapi message
   * @param event The HL7 message event
   * @param ackSuccessCode The String indicating if processing message has failed or not.
   * @param errors Errors that occurred (if the message processing failed).
   * @return The ACK message
   */
  public String generateAck(Message initialHapiMessage, String event, String ackSuccessCode, Error error) {

    logger.debug("Start executing generateAck ...");

    ACK ackMsg = getBasicAck(initialHapiMessage);

    MSH mshSegment = ackMsg.getMSH();
    MSA msaSegment = ackMsg.getMSA();

    logger.debug("event : " + event);

    try {
      if (event != null) {
        mshSegment.getMsh9_MessageType().getMsg2_TriggerEvent().setValue(event);
      }

      msaSegment.getMsa1_AcknowledgmentCode().setValue(ackSuccessCode);

    } catch (DataTypeException e) {
      logger.error(applicationErrorCodes.getACK_ErrorCompletingAckMessageFields(), e);
    }

    if (ackConfig.getFILL_ERR_SEGMENT_IN_ACK()) {
      if (error != null) {
        ERR err = ackMsg.getERR();
        try {
          err.getErr3_HL7ErrorCode().getCwe1_Identifier().setValue(error.getHl7ErrorCode().getValue());
          err.getErr3_HL7ErrorCode().getCwe2_Text().setValue(error.getHl7ErrorCode().getDescription());
          err.getErr3_HL7ErrorCode().getCwe3_NameOfCodingSystem().setValue(error.getHl7ErrorCode().getNameOfCodingSystem());

          err.getErr4_Severity().setValue(error.getSeverity());

          err.getErr5_ApplicationErrorCode().getCwe1_Identifier().setValue(error.getApplicationErrorCode().getValue());
          err.getErr5_ApplicationErrorCode().getCwe2_Text().setValue(error.getApplicationErrorCode().getDescription());
          err.getErr5_ApplicationErrorCode().getCwe3_NameOfCodingSystem().setValue(error.getApplicationErrorCode().getNameOfCodingSystem());

          // err.getErr7_DiagnosticInformation().setValue(error.getDiagnosticInformation());
        } catch (DataTypeException e) {
          logger.error(applicationErrorCodes.getACK_ErrorCompletingAckMessageFields(), e);
        }
      }
    } else {
      logger.info("FILL_ERR_SEGMENT_IN_ACK property is set to false. ERR segment won't be filled.");
    }
    return parseACK(ackMsg);
  }

  /***
   * Generates an ACK message from the initial Hapi message if possible.
   * Else, creates a new ACK object from scratch.
   * @param initialHapiMessage The initial Hapi Message
   * @return The ACK object
   */
  private ACK getBasicAck(Message initialHapiMessage) {

    ACK ack = null;

    String messageControlId = null;
    String fieldSeparator = null;
    String encodingCharacters = null;
    String processingId = null;
    String version = null;
    String event = null;

    // Try to generate a ACK from the initial message


    if (initialHapiMessage != null) {
      try {
        Message ackMsg = initialHapiMessage.generateACK();
        if (ackMsg instanceof ACK) {
          // if ack is a v2.6 ack, parse to ca.uhn.hl7v2.model.v26.message.ACK
          ack = (ACK) ackMsg;
        } else if (ackMsg instanceof ca.uhn.hl7v2.model.v21.message.ACK) {
          ca.uhn.hl7v2.model.v21.message.ACK ackv21 = (ca.uhn.hl7v2.model.v21.message.ACK) ackMsg;
          messageControlId = ackv21.getMSA().getMsa2_MESSAGECONTROLID().getValue();
          fieldSeparator = ackv21.getFieldSeparatorValue().toString();
          encodingCharacters = ackv21.getEncodingCharactersValue();
          processingId = ackv21.getMSH().getMsh11_PROCESSINGID().getValue();
          version = ackv21.getVersion();
          event = ackv21.getMSH().getMsh9_MESSAGETYPE().getCm_msg2_TriggerEvent().getValue();
        } else if (ackMsg instanceof ca.uhn.hl7v2.model.v22.message.ACK) {
          ca.uhn.hl7v2.model.v22.message.ACK ackv22 = (ca.uhn.hl7v2.model.v22.message.ACK) ackMsg;
          messageControlId = ackv22.getMSA().getMsa2_MessageControlID().getValue();
          fieldSeparator = ackv22.getFieldSeparatorValue().toString();
          encodingCharacters = ackv22.getEncodingCharactersValue();
          processingId = ackv22.getMSH().getMsh11_ProcessingID().getValue();
          version = ackv22.getVersion();
          event = ackv22.getMSH().getMsh9_MessageType().getCm_msg2_TriggerEvent().getValue();
        } else if (ackMsg instanceof ca.uhn.hl7v2.model.v23.message.ACK) {
          ca.uhn.hl7v2.model.v23.message.ACK ackv23 = (ca.uhn.hl7v2.model.v23.message.ACK) ackMsg;
          messageControlId = ackv23.getMSA().getMsa2_MessageControlID().getValue();
          fieldSeparator = ackv23.getFieldSeparatorValue().toString();
          encodingCharacters = ackv23.getEncodingCharactersValue();
          processingId = ackv23.getMSH().getMsh11_ProcessingID().toString();
          version = ackv23.getVersion();
          event = ackv23.getMSH().getMsh9_MessageType().getCm_msg2_TriggerEvent().getValue();
        } else if (ackMsg instanceof ca.uhn.hl7v2.model.v231.message.ACK) {
          ca.uhn.hl7v2.model.v231.message.ACK ackv231 = (ca.uhn.hl7v2.model.v231.message.ACK) ackMsg;
          messageControlId = ackv231.getMSA().getMsa2_MessageControlID().getValue();
          fieldSeparator = ackv231.getFieldSeparatorValue().toString();
          encodingCharacters = ackv231.getEncodingCharactersValue();
          processingId = ackv231.getMSH().getMsh11_ProcessingID().toString();
          version = ackv231.getVersion();
          event = ackv231.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue();
        } else if (ackMsg instanceof ca.uhn.hl7v2.model.v24.message.ACK) {
          ca.uhn.hl7v2.model.v24.message.ACK ackv24 = (ca.uhn.hl7v2.model.v24.message.ACK) ackMsg;
          messageControlId = ackv24.getMSA().getMsa2_MessageControlID().getValue();
          fieldSeparator = ackv24.getFieldSeparatorValue().toString();
          encodingCharacters = ackv24.getEncodingCharactersValue();
          processingId = ackv24.getMSH().getMsh11_ProcessingID().toString();
          version = ackv24.getVersion();
          event = ackv24.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue();
        } else if (ackMsg instanceof ca.uhn.hl7v2.model.v25.message.ACK) {
          ca.uhn.hl7v2.model.v25.message.ACK ackv25 = (ca.uhn.hl7v2.model.v25.message.ACK) ackMsg;
          messageControlId = ackv25.getMSA().getMsa2_MessageControlID().getValue();
          fieldSeparator = ackv25.getFieldSeparatorValue().toString();
          encodingCharacters = ackv25.getEncodingCharactersValue();
          processingId = ackv25.getMSH().getMsh11_ProcessingID().toString();
          version = ackv25.getVersion();
          event = ackv25.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue();
        } else if (ackMsg instanceof ca.uhn.hl7v2.model.v251.message.ACK) {
          ca.uhn.hl7v2.model.v251.message.ACK ackv251 = (ca.uhn.hl7v2.model.v251.message.ACK) ackMsg;
          messageControlId = ackv251.getMSA().getMsa2_MessageControlID().getValue();
          fieldSeparator = ackv251.getFieldSeparatorValue().toString();
          encodingCharacters = ackv251.getEncodingCharactersValue();
          processingId = ackv251.getMSH().getMsh11_ProcessingID().toString();
          version = ackv251.getVersion();
          event = ackv251.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue();
        } else {
          String strAckMsg = ackMsg.encode();
          encodingCharacters = ackMsg.getEncodingCharactersValue();
          fieldSeparator = ackMsg.getFieldSeparatorValue().toString();
          String ackMsaSegment = strAckMsg.substring(strAckMsg.indexOf("MSA|"));
          String[] ackMsaSegmentSplitted = ackMsaSegment.split("\\|");
          if (ackMsaSegmentSplitted.length > 2) {
            messageControlId = ackMsaSegmentSplitted[2];
          }
        }


      } catch (Exception e) {
        logger.error("Unable to generate ACK from the initial message.", e);


      }
    }

    // If the ACK generation failed, we create an ACK object from scratch.
    if (ack == null) {
      ack = new ACK();


      try {
        ack.getMSH().getMsh1_FieldSeparator().setValue(fieldSeparator != null ? fieldSeparator : "|");
        ack.getMSH().getMsh2_EncodingCharacters().setValue(encodingCharacters != null ? encodingCharacters : "^~\\&");
        ack.getMSH().getMsh7_DateTimeOfMessage().setValue(simpleDateHeureFormat.format(new Date()));
        ack.getMSA().getMsa2_MessageControlID().setValue(messageControlId);
        ack.getMSH().getMsh9_MessageType().getMsg1_MessageCode().setValue("ACK");
        // ack.getMSH().getMsh12_VersionID().getVid1_VersionID().setValue(version != null ? version : "2.6");
        ack.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().setValue(event != null ? event : "A01");


      } catch (DataTypeException e) {
        logger.error("An error occured when creating an ACK from scratch.", e);


      }
    }

    // Setting information that hasn't been generated.
    try {


      ack.getMSH().getMsh12_VersionID().getVid1_VersionID().setValue("2.6");
      ack.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().setValue("ACK");
      ack.getMSH().getMsh3_SendingApplication().getHd1_NamespaceID().setValue(ackConfig.getAPPLICATION_NAMESPACE_ID());
      ack.getMSH().getMsh3_SendingApplication().getHd2_UniversalID().setValue(ackConfig.getAPPLICATION_UNIVERSAL_ID());
      ack.getMSH().getMsh3_SendingApplication().getHd3_UniversalIDType().setValue(ackConfig.getAPPLICATION_UNIVERSAL_ID_TYPE());
      ack.getMSH().getMsh15_AcceptAcknowledgmentType().setValue("NE");
      ack.getMSH().getMsh16_ApplicationAcknowledgmentType().setValue("AL");
      ack.getMSH().getMsh21_MessageProfileIdentifier(0).getEi1_EntityIdentifier().setValue(ackConfig.getMessageProfileIdentifier_entityIdentifier());
      ack.getMSH().getMsh21_MessageProfileIdentifier(0).getEi2_NamespaceID().setValue(ackConfig.getMessageProfileIdentifier_namespaceId());
      ack.getMSH().getMsh21_MessageProfileIdentifier(0).getEi3_UniversalID().setValue(ackConfig.getMessageProfileIdentifier_universalId());
      ack.getMSH().getMsh21_MessageProfileIdentifier(0).getEi4_UniversalIDType().setValue(ackConfig.getMessageProfileIdentifier_universalIdType());

    } catch (DataTypeException e) {
      logger.error("Setting MSH information has failed", e);


    }

    return ack;


  }

  /***
   *  Encode ACK object into HL7 format
   * @param ackMsg ACK object to parse into a String
   * @return The String resulting of the parsing
   */
  private String parseACK(ACK ackMsg) {
    String hl7ack = "";


    if (ackMsg != null) {
      Parser parser = new PipeParser();


      try {
        hl7ack = parser.encode(ackMsg);


      } catch (HL7Exception e) {
        logger.error(applicationErrorCodes.getACK_AckParsingError(), e);


      }
    }
    return hl7ack;


  }

  /**
   * Get AA/AE/AR in function of the exception
   * @param success
   * @param exception
   * @return
   */
  public String getAckCode(boolean success, ContinuaConnectorException exception) {

    if (success) {
      return "AA";


    }

    if (exception == null) {
      logger.error("success was false and exception param in getAckCode was null");


      return "AR";


    }

    if (exception instanceof SegmentSequenceErrorException || exception instanceof RequiredFieldMissingException || exception instanceof DataTypeErrorException || exception instanceof TableValueNotFoundException) {
      return "AE";


    }
    if (exception instanceof UnsupportedMessageTypeException || exception instanceof UnsupportedEventCodeException || exception instanceof UnsupportedProcessingIdException || exception instanceof UnsupportedVersionIdException || exception instanceof UnknownKeyIdentifierException || exception instanceof ApplicationRecordLockedException || exception instanceof ApplicationInternalErrorException) {
      return "AR";


    }


    logger.error("The exception " + exception.getClass().getSimpleName() + " is not a valid ContinuaConnectorException.");


    return "AR";

  }
}

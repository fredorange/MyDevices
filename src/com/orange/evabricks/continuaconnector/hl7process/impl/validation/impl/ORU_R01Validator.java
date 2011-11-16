package com.orange.evabricks.continuaconnector.hl7process.impl.validation.impl;

import ca.uhn.hl7v2.model.v26.datatype.PT;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import ca.uhn.hl7v2.model.v26.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v26.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.model.v26.segment.MSH;
import ca.uhn.hl7v2.model.v26.segment.OBR;
import ca.uhn.hl7v2.model.v26.segment.OBX;
import ca.uhn.hl7v2.model.v26.segment.PID;

import com.orange.evabricks.continuaconnector.hl7process.impl.commons.HL7MessageParser;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.RequiredFieldMissingException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.SegmentSequenceErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedProcessingIdException;
import com.orange.evabricks.continuaconnector.hl7process.impl.validation.Validator;
import com.orange.evabricks.continuaconnector.hl7process.impl.validation.impl.util.HapiValidator;
import com.orange.evabricks.continuaconnector.hl7process.impl.validation.impl.util.MissingFieldDetector;
import javax.annotation.Resource;

@Component("oRU_R01Validator")
public class ORU_R01Validator implements Validator {

  private static final Logger logger = Logger.getLogger(ORU_R01Validator.class);
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;
  @Resource(name = "hapiValidator")
  private HapiValidator hapiValidator;
  @Resource(name = "hl7MessageParser")
  private HL7MessageParser hl7MessageParser;

  @Override
  public void validate(String hl7Message) throws ContinuaConnectorException {

    logger.debug("Start executing validate ...");

    if (hl7Message == null) {
      logger.warn("HL7 message should not be null.");
      throw new ApplicationInternalErrorException(applicationErrorCodes.getVALIDATION_DevError_NullMessageError());
    }

    // process basic validation on hl7 message
    hapiValidator.processHapiDefaultValidation(hl7Message);
    logger.info("The message is a valid HL7 message.");


    // verify if Continua required fields are filled
    processContinuaValidation(hl7Message);
    logger.info("The message is a valid Continua message.");

    // check some other characteristics of the message
    processAdditionnalCheckings(hl7Message);
    logger.info("The message is well formed (additionals checkings passed).");

  }

  /**
   * Verify if there are missing segments or missing fields.
   * @param hl7Message
   * @param oruMessage
   * @throws ContinuaConnectorException If some segments or fields are missing, an exception is thrown.
   */
  private void processContinuaValidation(String hl7Message) throws ContinuaConnectorException {
    processContinuaValidationForMissingSegments(hl7Message);
    processContinuaValidationForMissingFields(hl7Message);
  }

  /**
   * Verigy if there are missing segments.
   * @param hl7Message
   * @throws ContinuaConnectorException
   */
  private void processContinuaValidationForMissingSegments(String hl7Message) throws ContinuaConnectorException {

    String missingSegments = "";

    if (hl7Message.indexOf("MSH") == -1) {
      missingSegments += "MSH ; ";
    }
    if (hl7Message.indexOf("PID") == -1) {
      missingSegments += "PID ; ";
    }
    if (hl7Message.indexOf("OBR") == -1) {
      missingSegments += "OBR ; ";
    }
    if (hl7Message.indexOf("OBX") == -1) {
      missingSegments += "OBX ; ";
    }


    if (!missingSegments.isEmpty()) {
      throw new SegmentSequenceErrorException(applicationErrorCodes.getVALIDATION_ContinuaRequiredSegmentsMissing().setValueComplement(missingSegments));
    }

  }

  /**
   * Verify if there are missing fields.
   * @param oruMessage
   * @throws ContinuaConnectorException
   */
  private void processContinuaValidationForMissingFields(String hl7Message) throws ContinuaConnectorException {

    ORU_R01 oruMessage = hl7MessageParser.getORU_R01(hl7Message);
    if (oruMessage == null) {
      logger.warn("ORU_R01 message should not be null.");
      throw new ApplicationInternalErrorException(applicationErrorCodes.getVALIDATION_DevError_BadInputMessageType().setValueComplement(ORU_R01.class.getName()));
    }


    String missingFields = "";

    missingFields += processContinuaValidationForMSHMissingFields(oruMessage.getMSH());
    missingFields += processContinuaValidationForPIDMissingFields(oruMessage.getPATIENT_RESULT().getPATIENT().getPID());

    ORU_R01_PATIENT_RESULT patientResult = oruMessage.getPATIENT_RESULT();
    int numObr = patientResult.getORDER_OBSERVATIONReps();
    for (int i = 0; i < numObr; i++) {


      ORU_R01_ORDER_OBSERVATION orderObservation = null;
      orderObservation = patientResult.getORDER_OBSERVATION(i);

      if (orderObservation != null) {

        OBR obr = orderObservation.getOBR();
        missingFields += processContinuaValidationForOBRMissingFields(obr);

        int numObs = orderObservation.getOBSERVATIONReps();

        for (int j = 0; j < numObs; j++) {

          OBX obx = orderObservation.getOBSERVATION(j).getOBX();

          if (obx != null) {
            missingFields += processContinuaValidationForOBXMissingFields(obx);
          }
        }
      }
    }

    if (!missingFields.isEmpty()) {
      throw new RequiredFieldMissingException(applicationErrorCodes.getVALIDATION_ContinuaRequiredFieldsMissing().setValueComplement(missingFields));
    }
  }

  /**
   * Verify if there are missing fields into MSH segment.
   * @param mshSegment
   * @return Names of the missing fields.
   */
  private String processContinuaValidationForMSHMissingFields(MSH mshSegment) {
    String missingFields = "";


    if (MissingFieldDetector.typeSTIsMissing(mshSegment.getMsh1_FieldSeparator())) {
      missingFields += "MSH-1(FieldSeparator) ; ";
    }
    if (MissingFieldDetector.typeSTIsMissing(mshSegment.getMsh2_EncodingCharacters())) {
      missingFields += "MSH-2(EncodingCharacters) ; ";
    }
    if (MissingFieldDetector.typeHDIsMissing(mshSegment.getMsh3_SendingApplication())) {
      missingFields += "MSH-3(SendingApplication) ; ";
    }
    if (MissingFieldDetector.typeDTMIsMissing(mshSegment.getMsh7_DateTimeOfMessage())) {
      missingFields += "MSH-7(Date/TimeOfMessage) ; ";
    }
    if (MissingFieldDetector.typeSTIsMissing(mshSegment.getMsh10_MessageControlID())) {
      missingFields += "MSH-10(MessageControlId) ; ";
    }
    if (MissingFieldDetector.typePTIsMissing(mshSegment.getMsh11_ProcessingID())) {
      missingFields += "MSH-11(ProcessingId) ; ";
    }
    if (MissingFieldDetector.typeVIDIsMissing(mshSegment.getMsh12_VersionID())) {
      missingFields += "MSH-12(VersionId) ; ";
    }
    if (MissingFieldDetector.typeIDIsMissing(mshSegment.getMsh15_AcceptAcknowledgmentType())) {
      missingFields += "MSH-15(AcceptAcknowledgmentType) ; ";
    }
    if (MissingFieldDetector.typeIDIsMissing(mshSegment.getMsh16_ApplicationAcknowledgmentType())) {
      missingFields += "MSH-16(ApplicationAcknowledgmentType) ; ";
    }
    if (MissingFieldDetector.typeEIIsMissing(mshSegment.getMsh21_MessageProfileIdentifier(0))) {
      missingFields += "MSH-21(MessageProfileIdentifier) ; ";
    }

    return missingFields;
  }

  /**
   * Verify if there are missing fields into PID segment.
   * @param pidSegment
   * @return Names of the missing fields.
   */
  private String processContinuaValidationForPIDMissingFields(PID pidSegment) {

    String missingFields = "";

    if (MissingFieldDetector.typeCXIsMissing(pidSegment.getPid3_PatientIdentifierList(0))) {
      missingFields += "PID-3(PatientIdentifierList) ; ";
    }

    if (MissingFieldDetector.typeXPNIsMissing(pidSegment.getPid5_PatientName(0))) {
      missingFields += "PID-5(PatientName) ; ";
    }

    return missingFields;

  }

  /**
   * Verify if there are missing fields into OBR segment.
   * @param obrSegment
   * @return Names of the missing fields.
   */
  private String processContinuaValidationForOBRMissingFields(OBR obrSegment) {
    String missingFields = "";

    if (MissingFieldDetector.typeSIIsMissing(obrSegment.getObr1_SetIDOBR())) {
      missingFields += "OBR-1(SetIDOBR)  ; ";
    }
    if (MissingFieldDetector.typeEIIsMissing(obrSegment.getObr2_PlacerOrderNumber())) {
      missingFields += "OBR-2(PlaceOrderNumber) rep " + obrSegment.getObr1_SetIDOBR() + " ; ";
    }
    if (MissingFieldDetector.typeEIIsMissing(obrSegment.getObr3_FillerOrderNumber())) {
      missingFields += "OBR-3(fillerOrderNumber)  rep " + obrSegment.getObr1_SetIDOBR() + " ; ";
    }
    if (MissingFieldDetector.typeCWEIsMissing(obrSegment.getObr4_UniversalServiceIdentifier())) {
      missingFields += "OBR-4(UniversalServiceIdentifier) rep " + obrSegment.getObr1_SetIDOBR() + " ; ";
    }

    return missingFields;
  }

  /**
   * Verify if there are missing fields into OBX segment.
   * @param obxSegment
   * @return Names of the missing fields.
   */
  private String processContinuaValidationForOBXMissingFields(OBX obxSegment) {
    String missingFields = "";

    if (MissingFieldDetector.typeSIIsMissing(obxSegment.getObx1_SetIDOBX())) {
      missingFields += "OBX-1(SetIDOBX) ; ";
    }
    if (MissingFieldDetector.typeCWEIsMissing(obxSegment.getObx3_ObservationIdentifier())) {
      missingFields += "OBX-3(ObservationIdentifier) rep " + obxSegment.getObx1_SetIDOBX() + " ; ";
    }
    if (MissingFieldDetector.typeSTIsMissing(obxSegment.getObx4_ObservationSubID())) {
      missingFields += "OBX-4(ObservationSubID) rep " + obxSegment.getObx1_SetIDOBX() + " ; ";
    }
    if (MissingFieldDetector.typeIDIsMissing(obxSegment.getObx11_ObservationResultStatus())) {
      missingFields += "OBX-11(ObservationResultStatus) rep " + obxSegment.getObx1_SetIDOBX() + " ; ";
    }

    return missingFields;
  }

  private void processAdditionnalCheckings(String hl7Message) throws ContinuaConnectorException {
    ORU_R01 oru = hl7MessageParser.getORU_R01(hl7Message);
    checkProcessingId(oru.getMSH().getMsh11_ProcessingID());
    checkObservationIdentifierAndUnit(oru);
  }

  private void checkProcessingId(PT msh11) throws ContinuaConnectorException {
    String processingId = msh11.getPt1_ProcessingID().getValue();
    String processingMode = msh11.getPt2_ProcessingMode().getValue();
    if (!"P".equals(processingId) && !"D".equals(processingId) && !"T".equals(processingId)) {
      throw new UnsupportedProcessingIdException(applicationErrorCodes.getVALIDATION_UnsupportedProcessingId().setValueComplement(" Processing id expected: P or D or T. Found: " + processingId));
    }
    if (processingMode != null && !processingMode.isEmpty()) {
      if (!"A".equals(processingMode) && !"R".equals(processingMode) && !"I".equals(processingMode) && !"T".equals(processingMode)) {
        throw new UnsupportedProcessingIdException(applicationErrorCodes.getVALIDATION_UnsupportedProcessingId().setValueComplement(" Processing mode expected: A or R or I or T. Found: " + processingMode));
      }
    }
  }

  /**
   * In Observation Identifier and Unit CWE fields, subfields 1 and 3 are required.
   */
  private void checkObservationIdentifierAndUnit(ORU_R01 oru) throws RequiredFieldMissingException {
    ORU_R01_PATIENT_RESULT patientResult = oru.getPATIENT_RESULT();
    int numObr = patientResult.getORDER_OBSERVATIONReps();
    for (int i = 0; i < numObr; i++) {
      ORU_R01_ORDER_OBSERVATION orderObservation = patientResult.getORDER_OBSERVATION(i);
      int numObs = orderObservation.getOBSERVATIONReps();
      for (int j = 0; j < numObs; j++) {
        OBX obx = orderObservation.getOBSERVATION(j).getOBX();
        if (obx.getObx3_ObservationIdentifier() != null) {
          // check that cwe1 and cwe3 are not empty
          if (obx.getObx3_ObservationIdentifier().getCwe1_Identifier().getValue() == null || obx.getObx3_ObservationIdentifier().getCwe1_Identifier().getValue().isEmpty() || obx.getObx3_ObservationIdentifier().getCwe3_NameOfCodingSystem().getValue() == null || obx.getObx3_ObservationIdentifier().getCwe3_NameOfCodingSystem().getValue().isEmpty()) {
            ErrorCode errorCode = applicationErrorCodes.getVALIDATION_RequiredCWESubfieldMissing();
            errorCode.setValueComplement(" Location: OBX|" + obx.getObx1_SetIDOBX() + ", field 3 (observation identifier)");
            throw new RequiredFieldMissingException(errorCode);
          }
        }
        if (obx.getObx6_Units() != null) {
          // if obx-6 is not empty, check that cwe1 et cwe3 are not empty
          if (obx.getObx6_Units().getCwe1_Identifier().getValue() != null || obx.getObx6_Units().getCwe2_Text().getValue() != null || obx.getObx6_Units().getCwe3_NameOfCodingSystem().getValue() != null) {
            {
              if (obx.getObx6_Units().getCwe1_Identifier().getValue() == null || obx.getObx6_Units().getCwe1_Identifier().getValue().isEmpty() || obx.getObx6_Units().getCwe3_NameOfCodingSystem().getValue() == null || obx.getObx6_Units().getCwe3_NameOfCodingSystem().getValue().isEmpty()) {
                ErrorCode errorCode = applicationErrorCodes.getVALIDATION_RequiredCWESubfieldMissing();
                errorCode.setValueComplement(" Location: OBX|" + obx.getObx1_SetIDOBX() + " , field 6 (unit)");
                throw new RequiredFieldMissingException(errorCode);
              }
            }
          }
        }
      }
    }
  }
}

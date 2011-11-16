package com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This class contains all application error codes.
 * Application error codes are used in HL7InteropException that are thrown by the application.
 * @author tmdn5264
 *
 */
@Component("applicationErrorCodes")
@Scope("singleton")
public class ApplicationErrorCodes {

  /**
   * name of the coding system used to encode the errors. Here, the coding system is relative to the application and does't come from a norm.
   */
  protected static final String NAME_OF_CODING_SYSTEM = "ContinuaConnector";

  public ErrorCode getCONFIG_ErrorLoadingConfigFile() {
    return new ErrorCode("000", "Error when loading config file.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_DatabaseError_ErrorOpenningDbConnection() {
    return new ErrorCode("001", "Database error : unable to open db connection. Please check the db_url in config file and verify that the database is launched.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_DatabaseError_ErrorLoadingAccessToken() {
    return new ErrorCode("002", "Database error : error when loading access token. Data is probably null.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_DatabaseError_ErrorSavingCredentials() {
    return new ErrorCode("003", "Database error : error when saving credentials.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_DatabaseError_ErrorLoadingCredentials_businessError() {
    return new ErrorCode("004", "Database error : unable to load credentials.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_DatabaseError_ErrorLoadingCredentials_infrastructureError() {
    return new ErrorCode("005", "Database error : error when loading credentials. Unable to access the database.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_DatabaseError_ErrorClosingConnection() {
    return new ErrorCode("006", "Database error : error when closing database connection.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_DatabaseError_ErrorExecutingQuery() {
    return new ErrorCode("007", "Database error : error when executing query.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_IndivoException_BadUrl() {
    return new ErrorCode("008", "Configuration error for Indivo : malformed URL.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_IndivoException_UnexpectedException() {
    return new ErrorCode("009", "Unexpected error when instanciating indivo. Please check your params configuration (consumer_key, consumer_secret, etc).", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMON_DatabaseError_ErrorCleaningDatabase() {
    return new ErrorCode("010", "Database error : error when cleaning database.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCONFIG_ConfigFileNotFound() {
    return new ErrorCode("011", "Config file not found.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCONFIG_MissingPropertyInConfigFile() {
    return new ErrorCode("012", "A property is missing in config file.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMONPARSING_ParsingError() {
    return new ErrorCode("100", "Parsing error.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMONPARSING_UnsupportedMessageType() {
    return new ErrorCode("101", "Parsing error.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMONPARSING_NullMessageError() {
    return new ErrorCode("103", "Message should not be null.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMONPARSING_PIDNotFound() {
    return new ErrorCode("104", "Patient Identifier (PID-3-1) was not found.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMONPARSING_UnsupportedEventCode() {
    return new ErrorCode("105", "Unsupported event code.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMONPARSING_InvalidMessage() {
    return new ErrorCode("106", "Invalid message.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getCOMMONPARSING_InvalidFieldSeparators() {
    return new ErrorCode("107", "Invalid field separators. PCD-01 constrains to the characters |^~\\&. Message should shart with |^~\\&.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_DevError_NullMessageError() {
    return new ErrorCode("200", "Validation error :  message should not be null.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_InvalidHL7Message() {
    return new ErrorCode("201", "Validation error : the message is not a valid HL7 message.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_DevError_BadInputMessageType() {
    return new ErrorCode("202", "Validation error : incorrect type of Hapi message. Should be ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_ContinuaRequiredSegmentsMissing() {
    return new ErrorCode("203", "Validation error :  some Continua required segments are missing : ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_ContinuaRequiredFieldsMissing() {
    return new ErrorCode("204", "Validation error :  some Continua required fields are missing : ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_UnsupportedHL7Version() {
    return new ErrorCode("205", "Validation error :  unsupported HL7 version.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_UnsupportedProcessingId() {
    return new ErrorCode("206", "Validation error :  unsupported processing id.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_BadObx5Type() {
    return new ErrorCode("207", "Translation error : obx5 type doesn't match type defined in obx2.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getVALIDATION_RequiredCWESubfieldMissing() {
    return new ErrorCode("208", "Validation error :  some required CWE subfields are missing. ObservationIdentifer and Unit CWE-1 (identifier) and CWE-3 (name of coding system) subfields should not be empty.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_HapiError() {
    return new ErrorCode("300", "Translation error : an error occured during translation.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_DevError_NullMessageError() {
    return new ErrorCode("301", "Translation error : message should not be null.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_DevError_BadInputMessageType() {
    return new ErrorCode("302", "Translation error : incorrect type of Hapi message. Should be ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_UnknownObxSegment() {
    return new ErrorCode("304", "Translation error : the system doesn't know what to do with this segment: ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_DevError_BadValueType() {
    return new ErrorCode("305", "Translation error : bad value type.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_UnknownMedicalDeviceSystem() {
    return new ErrorCode("306", "Translation error : this medical device system is not supported yet: ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_UnknownMeasureOrDevice() {
    return new ErrorCode("307", "Translation error : this measure type / device item is not supported yet: ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_UncompatibleUnit() {
    return new ErrorCode("308", "Translation error : the unit is not compatible with the measure type: ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_VirtualMedicalDeviceShouldNotBeUsedInContinua() {
    return new ErrorCode("309", "Translation error : Virtual Medical Device is not used by the Continua WAN interface. OBX-4 should not match the pattern MDS.VMD. ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_MDSParentNotFoundForItem() {
    return new ErrorCode("310", "Translation error : MDS parent has not be found for the item.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getTRANSLATION_VoidObxSubId() {
    return new ErrorCode("311", "Translation error : sub-ID should contain at least one number.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION() {
    return new ErrorCode("400", "Integration error : an error occured during integration.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_DevError_NullPivotObjectError() {
    return new ErrorCode("401", "Integration error : pivot object should not be null", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_DevError_BadPivotObjectType() {
    return new ErrorCode("402", "Integration error : incorrect type of pivot object.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_ErrorLoadingAccessToken() {
    return new ErrorCode("403", "Integration error : unable to load access token.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_IndivoVitalSignDocumentFieldMissing() {
    return new ErrorCode("404", "Integration error : Indivo VitalSign document field is missing : ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_IndivoEquipmentDocumentFieldMissing() {
    return new ErrorCode("405", "Integration error : Indivo Equipment document field is missing : ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_DevError_AggregateOfAggregateNoSupported() {
    return new ErrorCode("406", "Integration error : the measures contained in an AggregatedMeasures object should be only SingleMeasure.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_IncoherentsSystemId() {
    return new ErrorCode("407", "Integration error : system-id found in MDS and in MDC_ATTR_SYS_ID are not the same.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_UnsupportedValueTypeForVitalSign() {
    return new ErrorCode("408", "Integration error : The Indivo VitalSign document only supports numeric value in the value field.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_MissingDeviceId() {
    return new ErrorCode("409", "Integration error : the system-id of the device is missing (should appear in OBX-18 of the MDS-level or in the MDC_ATTR_SYS_ID item).", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_DevError_MissingDeviceId() {
    return new ErrorCode("410", "Integration error : error when posting equipement: the id of the equipement should not be null.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINTEGRATION_ErrorCheckingIfEquipementExists() {
    return new ErrorCode("411", "Integration error : error when checking if equipement already exists in Indivo.", NAME_OF_CODING_SYSTEM);
  }

  //	public ErrorCode getINDIVOAPI_IndivoDaoException=new ErrorCode("500", "Indivo API error : unable to create indivoDao.", nameOfCodingSystem);
//	public ErrorCode getINDIVOAPI_AccessTokenDaoException=new ErrorCode("501", "Indivo API error : did not find stored access token : first access to the PHA from Indivo UI.", nameOfCodingSystem);
//	public ErrorCode getINDIVOAPI_IndivoException=new ErrorCode("502", "Indivo API error : an error occured during indivo api call.", nameOfCodingSystem);
  public ErrorCode getINDIVO_API_ErrorInitializingVitalSignManager() {
    return new ErrorCode("501", "Unexpected error when initializing vital sign manager.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorInitializingEquipmentManager() {
    return new ErrorCode("502", "Unexpected error when initializing equipment manager.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorPostingVitalSign() {
    return new ErrorCode("503", "Unexpected error when calling vitalSignMgr.postDocument()", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorPostingEquipment() {
    return new ErrorCode("504", "Unexpected error when calling equipmentMgr.postDocument()", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorArchivingDocument() {
    return new ErrorCode("505", "Unexpected error when archiving documents", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_PostingVitalSignFailed() {
    return new ErrorCode("506", "Posting vital sign failed. DocumentMeta returned by Indivo is null or DocumentMeta.getId() is null or DocumentMeta.getId() is empty.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_PostingEquipmentFailed() {
    return new ErrorCode("507", "Posting equipment failed. DocumentMeta returned by Indivo is null or DocumentMeta.getId() is null or DocumentMeta.getId() is empty.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorLinkingEquipment() {
    return new ErrorCode("508", "Error when linking equipment.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_LinkingEquipmentFailed() {
    return new ErrorCode("509", "Linnking equipment failed. The indivo method linkDocumentToExisting() returned false.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorGettingEquipmentMetaByExternalId() {
    return new ErrorCode("510", "Unexpected error when calling equipmentManager.getDocumentMetaByExternalId()", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorPostingEquipmentByExternalId() {
    return new ErrorCode("511", "Unexpected error when calling equipmentMgr.postDocumentByExternalId()", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorInitializingMessagingManager() {
    return new ErrorCode("512", "Unexpected error when initializing messaging manager.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorWhenSendingNotification() {
    return new ErrorCode("513", "Unexpected error when sending notification.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_ErrorLinkingVitalSign() {
    return new ErrorCode("514", "Error when linking vital sign.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getINDIVO_API_LinkingVitalSignFailed() {
    return new ErrorCode("515", "Linnking vital sign failed. The indivo method linkDocumentToExisting() returned false.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getACK_DevError_NullMessageError() {
    return new ErrorCode("600", "Acknowledgment error : message should not be null.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getACK_DevError_BadInputMessageType() {
    return new ErrorCode("601", "Acknowledgment error : incorrect type of Hapi message. Should be ", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getACK_MessageControlIDNotFound() {
    return new ErrorCode("602", "Acknowledgment error : messageControlID not found.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getACK_HapiAckGenerationFailed() {
    return new ErrorCode("603", "Acknowledgment error : the ack message generation using Hapi failed.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getACK_ErrorCompletingAckMessageFields() {
    return new ErrorCode("604", "Acknowledgment error : error when completing ack message fields.", NAME_OF_CODING_SYSTEM);
  }

  public ErrorCode getACK_AckParsingError() {
    return new ErrorCode("605", "Error when parsing ack hapi message.", NAME_OF_CODING_SYSTEM);
  }
}

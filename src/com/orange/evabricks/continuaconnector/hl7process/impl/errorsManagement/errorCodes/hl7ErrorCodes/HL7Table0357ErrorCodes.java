package com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.hl7ErrorCodes;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationRecordLockedException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.DataTypeErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.DuplicateKeyIdentifierException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.RequiredFieldMissingException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.SegmentSequenceErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.TableValueNotFoundException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnknownKeyIdentifierException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedEventCodeException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedMessageTypeException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedProcessingIdException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnsupportedVersionIdException;

/**
 * This class defines the HL7 error codes described in the HL7 Table 0357.
 * Each exception from hl7CompliantExceptions package corresponds to a HL7 error code.
 * @author tmdn5264
 *
 */
public class HL7Table0357ErrorCodes {

  private static String nameOfCodingSystem = "HL7Table0357";
  public static ErrorCode messageAccepted = new ErrorCode("0", "Message accepted", nameOfCodingSystem);
  public static ErrorCode segmentSequenceError = new ErrorCode("100", "Segment sequence error", nameOfCodingSystem);
  public static ErrorCode requiredFieldMissing = new ErrorCode("101", "Required field missing", nameOfCodingSystem);
  public static ErrorCode dataTypeError = new ErrorCode("102", "Data type error", nameOfCodingSystem);
  public static ErrorCode tableValueNotFound = new ErrorCode("103", "Table value not found", nameOfCodingSystem);
  public static ErrorCode unsupportedMessageType = new ErrorCode("200", "Unsupported message type", nameOfCodingSystem);
  public static ErrorCode unsupportedEventCode = new ErrorCode("201", "Unsupported event code", nameOfCodingSystem);
  public static ErrorCode unsupportedProcessingId = new ErrorCode("202", "Unsupported processing id", nameOfCodingSystem);
  public static ErrorCode unsupportedVersionId = new ErrorCode("203", "Unsupported version id", nameOfCodingSystem);
  public static ErrorCode unknownKeyIdentifier = new ErrorCode("204", "Unknown key identifier", nameOfCodingSystem);
  public static ErrorCode duplicateKeyIdentifier = new ErrorCode("205", "Duplicate key identifier", nameOfCodingSystem);
  public static ErrorCode applicationRecordLocked = new ErrorCode("206", "Application record locked", nameOfCodingSystem);
  public static ErrorCode applicationInternalError = new ErrorCode("207", "Application internal error", nameOfCodingSystem);

  /**
   * Get the ErrorCode object corresponding to the HL7InteropException passed into parameter.
   * @param e
   * @return
   */
  public static ErrorCode getErrorCodeByException(ContinuaConnectorException e) {
    if (e instanceof SegmentSequenceErrorException) {
      return HL7Table0357ErrorCodes.segmentSequenceError;
    }
    if (e instanceof RequiredFieldMissingException) {
      return HL7Table0357ErrorCodes.requiredFieldMissing;
    }
    if (e instanceof DataTypeErrorException) {
      return HL7Table0357ErrorCodes.dataTypeError;
    }
    if (e instanceof TableValueNotFoundException) {
      return HL7Table0357ErrorCodes.tableValueNotFound;
    }
    if (e instanceof UnsupportedMessageTypeException) {
      return HL7Table0357ErrorCodes.unsupportedMessageType;
    }
    if (e instanceof UnsupportedEventCodeException) {
      return HL7Table0357ErrorCodes.unsupportedEventCode;
    }
    if (e instanceof UnsupportedProcessingIdException) {
      return HL7Table0357ErrorCodes.unsupportedProcessingId;
    }
    if (e instanceof UnsupportedVersionIdException) {
      return HL7Table0357ErrorCodes.unsupportedVersionId;
    }
    if (e instanceof UnknownKeyIdentifierException) {
      return HL7Table0357ErrorCodes.unknownKeyIdentifier;
    }
    if (e instanceof DuplicateKeyIdentifierException) {
      return HL7Table0357ErrorCodes.duplicateKeyIdentifier;
    }
    if (e instanceof ApplicationRecordLockedException) {
      return HL7Table0357ErrorCodes.applicationRecordLocked;
    }
    if (e instanceof ApplicationInternalErrorException) {
      return HL7Table0357ErrorCodes.applicationInternalError;
    } else {
      return HL7Table0357ErrorCodes.applicationInternalError;
    }
  }
}

package com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions;

import ca.uhn.hl7v2.model.DataTypeException;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * @author tmdn5264
 */
@Component("exceptionGenerator")
@Scope("singleton")
public class ExceptionGenerator {

  public ExceptionGenerator() {
  }

  public ContinuaConnectorException getExceptionByErrorCode(int hl7ErrorCode, ErrorCode connectorErrorCode) {

    if (hl7ErrorCode == 100) {
      return new SegmentSequenceErrorException(connectorErrorCode);
    }
    if (hl7ErrorCode == 101) {
      return new RequiredFieldMissingException(connectorErrorCode);
    }
    if (hl7ErrorCode == 102) {
      return new DataTypeErrorException(connectorErrorCode);
    }
    if (hl7ErrorCode == 103) {
      return new TableValueNotFoundException(connectorErrorCode);
    }
    if (hl7ErrorCode == 200) {
      return new UnsupportedMessageTypeException(connectorErrorCode);
    }
    if (hl7ErrorCode == 201) {
      return new UnsupportedEventCodeException(connectorErrorCode);
    }
    if (hl7ErrorCode == 202) {
      return new UnsupportedProcessingIdException(connectorErrorCode);
    }
    if (hl7ErrorCode == 203) {
      return new UnsupportedVersionIdException(connectorErrorCode);
    }
    if (hl7ErrorCode == 204) {
      return new UnknownKeyIdentifierException(connectorErrorCode);
    }
    if (hl7ErrorCode == 205) {
      return new DuplicateKeyIdentifierException(connectorErrorCode);
    }
    if (hl7ErrorCode == 206) {
      return new ApplicationRecordLockedException(connectorErrorCode);
    }
    if (hl7ErrorCode == 207) {
      return new ApplicationInternalErrorException(connectorErrorCode);
    }
    return new ApplicationInternalErrorException(connectorErrorCode);
  }

  public ContinuaConnectorException getExceptionByErrorCode(int hl7ErrorCode, ErrorCode connectorErrorCode, Throwable e) {
    if (hl7ErrorCode == 100) {
      return new SegmentSequenceErrorException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 101) {
      return new RequiredFieldMissingException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 102 || e instanceof DataTypeException) {
      return new DataTypeErrorException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 103) {
      return new TableValueNotFoundException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 200) {
      return new UnsupportedMessageTypeException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 201) {
      return new UnsupportedEventCodeException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 202) {
      return new UnsupportedProcessingIdException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 203) {
      return new UnsupportedVersionIdException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 204) {
      return new UnknownKeyIdentifierException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 205) {
      return new DuplicateKeyIdentifierException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 206) {
      return new ApplicationRecordLockedException(connectorErrorCode, e);
    }
    if (hl7ErrorCode == 207) {
      return new ApplicationInternalErrorException(connectorErrorCode, e);
    }
    return new ApplicationInternalErrorException(connectorErrorCode, e);
  }
}

package com.orange.evabricks.continuaconnector.hl7process.impl.exceptions;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;

/**
 * Basic exception class of the project.
 * Each exception returned by this project must be a HL7InteropException.
 * A HL7InteropException must contain an applicationErrorCode and can contain the "parent" exception (Throwable).
 * Classes from hl7CompliantExceptions package inherits from HL7InteropException. Each of these exceptions corresponds to a HL7 error code. 
 * @author tmdn5264
 *
 */
public abstract class ContinuaConnectorException extends Exception {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private ErrorCode applicationErrorCode;

  public ContinuaConnectorException(ErrorCode errorCode, Throwable e) {
    super(e);
    this.setApplicationErrorCode(errorCode);
  }

  public ContinuaConnectorException(ErrorCode errorCode) {
    super();
    this.setApplicationErrorCode(errorCode);
  }

  @Override
  public String toString() {

    StringBuilder str = new StringBuilder();
    str.append(super.toString() + "\r");
    str.append(applicationErrorCode.toString() + "\r");
    if (this.getStackTrace() != null && this.getStackTrace().length > 0) {
      for (int i = 0; i < Math.min(3, this.getStackTrace().length); i++) {
        str.append("at " + this.getStackTrace()[i] + "\r");

      }
    }

    return str.toString();
  }

  public void setApplicationErrorCode(ErrorCode errorCode) {
    this.applicationErrorCode = errorCode;
  }

  public ErrorCode getApplicationErrorCode() {
    return applicationErrorCode;
  }
}

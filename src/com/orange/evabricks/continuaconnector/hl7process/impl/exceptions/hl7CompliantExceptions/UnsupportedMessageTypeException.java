package com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;

public class UnsupportedMessageTypeException extends ContinuaConnectorException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public UnsupportedMessageTypeException(ErrorCode errorCode, Throwable e) {
    super(errorCode, e);
  }

  public UnsupportedMessageTypeException(ErrorCode errorCode) {
    super(errorCode);
  }
}

package com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;

public class ApplicationInternalErrorException extends ContinuaConnectorException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ApplicationInternalErrorException(ErrorCode errorCode, Throwable e) {
    super(errorCode, e);
  }

  public ApplicationInternalErrorException(ErrorCode errorCode) {
    super(errorCode);
  }
}

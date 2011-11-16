package com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;

public class ApplicationRecordLockedException extends ContinuaConnectorException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public ApplicationRecordLockedException(ErrorCode errorCode, Throwable e) {
    super(errorCode, e);
  }

  public ApplicationRecordLockedException(ErrorCode errorCode) {
    super(errorCode);
  }
}

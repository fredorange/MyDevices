package com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;

public class DataTypeErrorException extends ContinuaConnectorException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public DataTypeErrorException(ErrorCode errorCode, Throwable e) {
    super(errorCode, e);
  }

  public DataTypeErrorException(ErrorCode errorCode) {
    super(errorCode);
  }
}

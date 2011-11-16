package com.orange.evabricks.continuaconnector.hl7process.impl.validation.impl.util;

import org.apache.log4j.Logger;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.impl.DefaultValidation;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ExceptionGenerator;
import javax.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("hapiValidator")
@Scope("singleton")
public class HapiValidator {

  private static final Logger logger = Logger.getLogger(HapiValidator.class);
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;
  @Resource(name = "exceptionGenerator")
  private ExceptionGenerator exceptionGenerator;

  public HapiValidator() {
  }

  /**
   * Verify if the message is a valid HL7 message.
   * @param hl7Message
   * @return A Hapi message
   * @throws ApplicationInternalErrorException If validation failed, an exception is thrown.
   */
  public Message processHapiDefaultValidation(String hl7Message) throws ContinuaConnectorException {
    try {
      Parser parser = new GenericParser();
      parser.setValidationContext(new DefaultValidation());
      return parser.parse(hl7Message);
    } catch (EncodingNotSupportedException e) {
      logger.error("Hapi validation failed");
      ErrorCode applicationErrorCode = applicationErrorCodes.getVALIDATION_InvalidHL7Message();
      applicationErrorCode.setValueComplement(" " + e.getMessage());
      throw exceptionGenerator.getExceptionByErrorCode(e.getErrorCode(), applicationErrorCode, e);
    } catch (HL7Exception e) {
      logger.error("Hapi validation failed");
      ErrorCode applicationErrorCode = applicationErrorCodes.getVALIDATION_InvalidHL7Message();
      applicationErrorCode.setValueComplement(" " + e.getMessage());
      throw exceptionGenerator.getExceptionByErrorCode(e.getErrorCode(), applicationErrorCode, e);
    }
  }
}

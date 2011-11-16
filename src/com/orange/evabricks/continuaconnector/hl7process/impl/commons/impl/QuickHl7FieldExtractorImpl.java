package com.orange.evabricks.continuaconnector.hl7process.impl.commons.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.commons.QuickHl7FieldExtractor;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.SegmentSequenceErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.UnknownKeyIdentifierException;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("quickHl7FieldExtractor")
@Scope("singleton")
public class QuickHl7FieldExtractorImpl implements QuickHl7FieldExtractor {

  private static final Logger logger = Logger.getLogger(QuickHl7FieldExtractorImpl.class);
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;

  public QuickHl7FieldExtractorImpl() {
  }

  /**
   * Get PID3-CX1 (subfield CX1 of field PID3)
   * @param hl7Message
   * @return PID3-CX1 String
   * @throws UnknownKeyIdentifierException
   */
  @Override
  public String getPidFromHL7Message(String hl7Message) throws ContinuaConnectorException {

    try {
      if (!hl7Message.startsWith("MSH")) {
        throw new SegmentSequenceErrorException(applicationErrorCodes.getCOMMONPARSING_InvalidMessage().setValueComplement(" MSH segment not found."));
      }

      if (!hl7Message.startsWith("MSH|^~\\&")) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMONPARSING_InvalidFieldSeparators());
      }

      String separator1 = "|";
      String separator2 = "^";
      logger.debug("separator1 : " + separator1 + " ; separator2 : " + separator2);

      int pidSegmentIndex = hl7Message.indexOf("PID");
      logger.debug("pidSegmentIndex : " + pidSegmentIndex);
      if (pidSegmentIndex == -1) {
        throw new SegmentSequenceErrorException(applicationErrorCodes.getCOMMONPARSING_InvalidMessage().setValueComplement(" PID segment not found."));
      }

      String strBeginningWithPID = hl7Message.substring(pidSegmentIndex);
      logger.debug("strBeginningWithPID : " + strBeginningWithPID.substring(0, Math.min(200, strBeginningWithPID.length() - 1)));

      String[] pidFields = strBeginningWithPID.split(escape(separator1));

      if (pidFields == null || pidFields.length <= 2) {
        throw new UnknownKeyIdentifierException(applicationErrorCodes.getCOMMONPARSING_PIDNotFound().setValueComplement(" PID3 field not found."));
      }
      String pid3 = pidFields[3];
      logger.debug("pid3 : " + pid3);
      if (pid3 == null || pid3.trim().isEmpty()) {
        throw new UnknownKeyIdentifierException(applicationErrorCodes.getCOMMONPARSING_PIDNotFound().setValueComplement(" PID3 should not be empty."));
      }

      String[] pid3Subfields = pid3.split(escape(separator2));
      if (pid3Subfields == null || pid3Subfields.length == -1) {
        throw new UnknownKeyIdentifierException(applicationErrorCodes.getCOMMONPARSING_PIDNotFound().setValueComplement(" PID3-CX1 subfield not found"));
      }
      String pid3Cx1 = pid3Subfields[0];
      logger.debug("pid3Cx1 : " + pid3Cx1);
      if (pid3Cx1 == null || pid3Cx1.trim().isEmpty()) {
        throw new UnknownKeyIdentifierException(applicationErrorCodes.getCOMMONPARSING_PIDNotFound().setValueComplement(" PID3-CX1 should not be empty."));
      }
      return pid3Cx1;
    } catch (SegmentSequenceErrorException e) {
      throw e;
    } catch (UnknownKeyIdentifierException e) {
      throw e;
    } catch (ApplicationInternalErrorException e) {
      throw e;
    } catch (Exception e) {
      throw new UnknownKeyIdentifierException(applicationErrorCodes.getCOMMONPARSING_PIDNotFound().setValueComplement(" Unexpected error"), e);
    }
  }

  /**
   * Strings have to be escaped to use the "split" method
   * @param str The String to escape
   * @return The escaped string
   */
  private String escape(String str) {
    return str.replace("|", "\\|").replace(".", "\\.").replace("^", "\\^").replace("*", "\\*");

    // TODO : utiliser StringEscapeUtil pour escaper les separateurs, et tester avec différents séparateurs
  }
}

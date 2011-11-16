package com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;

/***
 * Class used in acknowledgment.
 * Contains informations that will be displayed into the ACK message.
 * @author tmdn5264
 *
 */
public class Error {

  private ErrorCode hl7ErrorCode;
  private String severity;
  private ErrorCode applicationErrorCode;
  private String diagnosticInformation;

  public Error(ErrorCode hl7ErrorCode, String severity,
          ErrorCode applicationErrorCode, String diagnosticInformation) {
    super();
    this.hl7ErrorCode = hl7ErrorCode;
    this.severity = severity;
    this.applicationErrorCode = applicationErrorCode;
    this.diagnosticInformation = diagnosticInformation;
  }

  public ErrorCode getHl7ErrorCode() {
    return hl7ErrorCode;
  }

  public void setHl7ErrorCode(ErrorCode hl7ErrorCode) {
    this.hl7ErrorCode = hl7ErrorCode;
  }

  public String getSeverity() {
    return severity;
  }

  public void setSeverity(String sevrity) {
    this.severity = sevrity;
  }

  public ErrorCode getApplicationErrorCode() {
    return applicationErrorCode;
  }

  public void setApplicationErrorCode(ErrorCode applicationErrorCode) {
    this.applicationErrorCode = applicationErrorCode;
  }

  public String getDiagnosticInformation() {
    return diagnosticInformation;
  }

  public void setDiagnosticInformation(String diagnosticInformation) {
    this.diagnosticInformation = diagnosticInformation;
  }
}

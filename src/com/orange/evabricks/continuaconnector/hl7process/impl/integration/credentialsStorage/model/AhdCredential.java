package com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.model;

/**
 * The object representing a credential in the AHD (accessKey and controlKey)
 * @author tmdn5264
 */
public class AhdCredential {

  private String accessKey;
  private String controlKey;

  public AhdCredential() {
  }

  public AhdCredential(String accessKey, String controlKey) {
    super();
    this.accessKey = accessKey;
    this.controlKey = controlKey;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getControlKey() {
    return controlKey;
  }

  public void setControlKey(String controlKey) {
    this.controlKey = controlKey;
  }
}

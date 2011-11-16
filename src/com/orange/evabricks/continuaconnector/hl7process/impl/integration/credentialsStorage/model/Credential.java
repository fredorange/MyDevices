package com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.model;

/**
 * Object representing the database model.
 * 
 * @author tmdn5264
 *
 */
public class Credential {

  private String accessKey;
  private String controlKey;
  private String token;
  private String tokenSecret;
  private String recordId;

  public Credential() {
  }

  public Credential(String accessKey, String controlKey, String token,
          String tokenSecret, String recordId) {
    super();
    this.accessKey = accessKey;
    this.controlKey = controlKey;
    this.token = token;
    this.tokenSecret = tokenSecret;
    this.recordId = recordId;
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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getTokenSecret() {
    return tokenSecret;
  }

  public void setTokenSecret(String tokenSecret) {
    this.tokenSecret = tokenSecret;
  }

  public String getRecordId() {
    return recordId;
  }

  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  @Override
  public String toString() {
    return "Credential [accessKey=" + accessKey + ", controlKey=" + controlKey + ", token="
            + token + ", tokenSecret=" + tokenSecret + ", recordId="
            + recordId + "]";
  }
}

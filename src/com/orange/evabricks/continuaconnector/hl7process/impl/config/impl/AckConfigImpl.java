package com.orange.evabricks.continuaconnector.hl7process.impl.config.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.config.AckConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author tmdn5264
 */
@Component("ackConfig")
public class AckConfigImpl implements AckConfig {

  @Value("${ack.fillERRSegment}")
  private String FILL_ERR_SEGMENT_IN_ACK_as_string = "";
  @Value("${ack.application.namespaceId}")
  private String APPLICATION_NAMESPACE_ID = "";
  @Value("${ack.application.universalId}")
  private String APPLICATION_UNIVERSAL_ID = "";
  @Value("${ack.application.universalIdType}")
  private String APPLICATION_UNIVERSAL_ID_TYPE = "";
  @Value("${ack.messageProfileIdentifier.entityIdentifier}")
  private String messageProfileIdentifier_entityIdentifier = "";
  @Value("${ack.messageProfileIdentifier.namespaceId}")
  private String messageProfileIdentifier_namespaceId = "";
  @Value("${ack.messageProfileIdentifier.universalId}")
  private String messageProfileIdentifier_universalId = "";
  @Value("${ack.messageProfileIdentifier.universalIdType}")
  private String messageProfileIdentifier_universalIdType = "";

  public AckConfigImpl() {
  }

  @Override
  public boolean getFILL_ERR_SEGMENT_IN_ACK() {
    return Boolean.parseBoolean(FILL_ERR_SEGMENT_IN_ACK_as_string);
  }

  @Override
  public String getAPPLICATION_NAMESPACE_ID() {
    return APPLICATION_NAMESPACE_ID;
  }

  @Override
  public String getAPPLICATION_UNIVERSAL_ID() {
    return APPLICATION_UNIVERSAL_ID;
  }

  @Override
  public String getAPPLICATION_UNIVERSAL_ID_TYPE() {
    return APPLICATION_UNIVERSAL_ID_TYPE;
  }

  @Override
  public String getMessageProfileIdentifier_entityIdentifier() {
    return messageProfileIdentifier_entityIdentifier;
  }

  @Override
  public String getMessageProfileIdentifier_namespaceId() {
    return messageProfileIdentifier_namespaceId;
  }

  @Override
  public String getMessageProfileIdentifier_universalId() {
    return messageProfileIdentifier_universalId;
  }

  @Override
  public String getMessageProfileIdentifier_universalIdType() {
    return messageProfileIdentifier_universalIdType;
  }

  public void setAPPLICATION_NAMESPACE_ID(String APPLICATION_NAMESPACE_ID) {
    this.APPLICATION_NAMESPACE_ID = APPLICATION_NAMESPACE_ID;
  }

  public void setAPPLICATION_UNIVERSAL_ID(String APPLICATION_UNIVERSAL_ID) {
    this.APPLICATION_UNIVERSAL_ID = APPLICATION_UNIVERSAL_ID;
  }

  public void setAPPLICATION_UNIVERSAL_ID_TYPE(String APPLICATION_UNIVERSAL_ID_TYPE) {
    this.APPLICATION_UNIVERSAL_ID_TYPE = APPLICATION_UNIVERSAL_ID_TYPE;
  }

  public String getFILL_ERR_SEGMENT_IN_ACK_as_string() {
    return FILL_ERR_SEGMENT_IN_ACK_as_string;
  }

  public void setFILL_ERR_SEGMENT_IN_ACK_as_string(String FILL_ERR_SEGMENT_IN_ACK_as_string) {
    this.FILL_ERR_SEGMENT_IN_ACK_as_string = FILL_ERR_SEGMENT_IN_ACK_as_string;
  }

  public void setMessageProfileIdentifier_entityIdentifier(String messageProfileIdentifier_entityIdentifier) {
    this.messageProfileIdentifier_entityIdentifier = messageProfileIdentifier_entityIdentifier;
  }

  public void setMessageProfileIdentifier_namespaceId(String messageProfileIdentifier_namespaceId) {
    this.messageProfileIdentifier_namespaceId = messageProfileIdentifier_namespaceId;
  }

  public void setMessageProfileIdentifier_universalId(String messageProfileIdentifier_universalId) {
    this.messageProfileIdentifier_universalId = messageProfileIdentifier_universalId;
  }

  public void setMessageProfileIdentifier_universalIdType(String messageProfileIdentifier_universalIdType) {
    this.messageProfileIdentifier_universalIdType = messageProfileIdentifier_universalIdType;
  }
}

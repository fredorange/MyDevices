package com.orange.evabricks.continuaconnector.hl7process.impl.config;

/**
 * Params used to generate an acknowledgment
 * @author tmdn5264
 */
public interface AckConfig {
/**
   * Indicates if the acknowledgment module has to fill or not the ERR segment in the acknowledgment message
   * @return a boolean indicating if the acknowledgment module has to fill or not the ERR segment in the acknowledgment message
   */
  public boolean getFILL_ERR_SEGMENT_IN_ACK();

  /**
   * Get the application namespace id, used in field MSH-9 of ACK message
   * @return
   */
  public String getAPPLICATION_NAMESPACE_ID();

  /**
   *  Get the application universal id, used in field MSH-9 of ACK message
   * @return
   */
  public String getAPPLICATION_UNIVERSAL_ID();

  /**
   *  Get the application universal id type, used in field MSH-9 of ACK message
   * @return
   */
  public String getAPPLICATION_UNIVERSAL_ID_TYPE();

  /**
   * Get the message profile identifier, used in field MSH-21 of ACK message
   * @return
   */
  public String getMessageProfileIdentifier_entityIdentifier();

  /**
   * Get the message profile identifier, used in field MSH-21 of ACK message
   * @return
   */
  public String getMessageProfileIdentifier_namespaceId();

  /**
   * Get the message profile identifier, used in field MSH-21 of ACK message
   * @return
   */
  public String getMessageProfileIdentifier_universalId();

  /**
   * Get the message profile identifier, used in field MSH-21 of ACK message
   * @return
   */
  public String getMessageProfileIdentifier_universalIdType();
}

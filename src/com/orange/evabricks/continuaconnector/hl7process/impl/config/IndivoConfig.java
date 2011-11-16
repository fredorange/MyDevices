package com.orange.evabricks.continuaconnector.hl7process.impl.config;

/**
 * Config informations for Indivo setting
 * @author tmdn5264
 */
public interface IndivoConfig {

   /**
   * Get the pha consumer key (the key registered in Indivo for the pha)
   * @return the pha consumer key
   */
  public String getPHA_CONSUMER_KEY();

  /**
   * Get the pha consumer secret (the secret registered in Indivo for the pha)
   * @return the pha consumer secret
   */
  public String getPHA_CONSUMER_SECRET();

  /**
   * Get the pha app_id (ie the email registered in Indivo for the pha)
   * @return the pha app_id
   */
  public String getPHA_APP_ID();

  /**
   * Get the Indivo server url
   * @return the Indivo server url
   */
  public String getINDIVO_SERVER_URL();

  /**
   * Get the Indivo ui server url
   * @return the Indivo ui server url
   */
  public String getINDIVO_UI_SERVER_URL();
}

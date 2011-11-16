package com.orange.evabricks.continuaconnector.hl7process.impl.config;

/**
 * Params used for the connection to the database in which accessKey/control/Key/token/recordId will be saved
 * @author tmdn5264
 */
public interface DbConfig {

  /**
   * Get the driver used to access the database in which accessKey/control/Key/token/recordId will be saved
   * @return the database driver
   */
  public String getDB_DRIVER();

  /**
   * Get the url of the database in which accessKey/control/Key/token/recordId will be saved
   * @return the database url
   */
  public String getDB_URL();

  /**
   * Get the user used to access the database in which accessKey/control/Key/token/recordId will be saved
   * @return the database user
   */
  public String getDB_USER();

  /**
   * Get the password used to access the database in which accessKey/control/Key/token/recordId will be saved
   * @return the database password
   */
  public String getDB_PASSWORD();

}

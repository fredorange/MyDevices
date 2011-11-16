package com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import java.sql.Connection;

/**
 * Database connection management
 * 
 * @author tmdn5264
 *
 */
public interface DbConnection {

  /**
   * open the connection to the database
   * @return The Connection object
   * @throws ContinuaConnectorException
   */
  public Connection getConnection() throws ContinuaConnectorException;

  /**
   * Close the connection to the database.
   * This method has to be called at the end of each query (or group of query).
   * @throws ContinuaConnectorException
   */
  public void closeConnection() throws ContinuaConnectorException;
}

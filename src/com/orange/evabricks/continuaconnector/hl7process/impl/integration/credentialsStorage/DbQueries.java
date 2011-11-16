package com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.model.Credential;

public interface DbQueries {

  /**
   * Add a row into the credentials table.
   * @param credential The Credential object containing data to save in the database
   * @throws ContinuaConnectorException
   */
  public void saveCredential(Credential credential) throws ContinuaConnectorException;

  /**
   * Update indivo_token and indivo_token_secret for the record identified by indivo_recordId
   * @param indivo_recordId The recordId whose token has to be updated
   * @param indivo_token The token to update
   * @param indivo_tokenSecret the token secret to update
   * @throws ContinuaConnectorException
   */
  public void updateAccessToken(String indivo_recordId, String indivo_token, String indivo_tokenSecret) throws ContinuaConnectorException;

  /**
   * Get credentials by accessKey.
   * @param accessKey The id used for AHD credentials.
   * @return A Credential containing requested credentials
   * @throws ContinuaConnectorException
   */
  public Credential loadCredentialByAccessKey(String accessKey) throws ContinuaConnectorException;

  /**
   * Get credentials by recordId.
   * @param indivo_recordId The recordId
   * @return A Credential containing requested credentials
   * @throws ContinuaConnectorException
   */
  public Credential loadCredentialByRecordId(String indivo_recordId) throws ContinuaConnectorException;

  /**
   * Indicates if a credential is already stored in database for the given recordId
   * @param indivo_recordId The given recordId
   * @return True if credential already exists, else false.
   * @throws ContinuaConnectorException
   */
  public boolean isCredentialStored(String indivo_recordId) throws ContinuaConnectorException;
}

package com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo;

import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.model.AhdCredential;
import com.orange.jlinx.auth.AccessToken;

public interface AccessTokenMgr {

  /**
   * Save the accessToken associated to ahdCredentials
   * @param ahdCredential credentials used in the AHD
   * @param accessToken Indivo access token
   * @throws ContinuaConnectorException
   */
  public void saveAccessToken(AhdCredential ahdCredential, AccessToken accessToken) throws ContinuaConnectorException;

  /**
   * Update accessToken by recordId
   * @param accessToken The access token (containing recordId and access token information)
   * @throws ContinuaConnectorException
   */
  public void updateAccessToken(AccessToken accessToken) throws ContinuaConnectorException;

  /**
   * Get access token by accessKey
   * @param accessKey The accessKey used to get accessKey
   * @return A AccessToken object containing acess token information
   * @throws ContinuaConnectorException
   */
  public AccessToken loadAccessTokenByAccessKey(String accessKey) throws ContinuaConnectorException;

  /**
   * Get access token by recordId
   * @param indivo_recordId The recordId used to get accessKey
   * @return A AccessToken object containing acess token information
   * @throws ContinuaConnectorException
   */
  public AhdCredential loadAhdCredentialByRecordId(String indivo_recordId) throws ContinuaConnectorException;

  /**
   * Indicates if a token has already been saved with this recordId
   * @param indivo_recordId
   * @return True if a token has already been saved with this recordId, else false
   * @throws ContinuaConnectorException
   */
  public boolean isAccessTokenAlreadySaved(String indivo_recordId) throws ContinuaConnectorException;
}

package com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.DbQueries;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.model.AhdCredential;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.credentialsStorage.model.Credential;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.AccessTokenMgr;
import com.orange.jlinx.IndivoException;
import com.orange.jlinx.RecordId;
import com.orange.jlinx.auth.AccessToken;
import org.apache.log4j.Logger;

@Component("accessTokenMgr")
@Scope("singleton")
public class AccessTokenMgrImpl implements AccessTokenMgr {

  private static final Logger logger = Logger.getLogger(AccessTokenMgrImpl.class);
  @Resource(name = "dbQueries")
  private DbQueries dbQueries;
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;

  public AccessTokenMgrImpl() {
  }

  @Override
  public void saveAccessToken(AhdCredential ahdCredential, AccessToken accessToken) throws ContinuaConnectorException {
    logger.debug("Start executing saveAccessToken ...");
    Credential credential = new Credential();
    credential.setAccessKey(ahdCredential.getAccessKey());
    credential.setControlKey(ahdCredential.getControlKey());
    credential.setToken(accessToken.getToken());
    credential.setTokenSecret(accessToken.getTokenSecret());
    credential.setRecordId(accessToken.getRecordId().getId());
    dbQueries.saveCredential(credential);
  }

  @Override
  public void updateAccessToken(AccessToken accessToken) throws ContinuaConnectorException {
    logger.debug("Start executing updateAccessToken ...");
    dbQueries.updateAccessToken(accessToken.getRecordId().getId(), accessToken.getToken(), accessToken.getTokenSecret());
  }

  @Override
  public AccessToken loadAccessTokenByAccessKey(String accessKey) throws ContinuaConnectorException {
    logger.debug("Start executing loadAccessTokenByAccessKey ...");

    Credential credential = dbQueries.loadCredentialByAccessKey(accessKey);
    AccessToken accessToken = new AccessToken();

    try {
      accessToken.setToken(credential.getToken());
      accessToken.setTokenSecret(credential.getTokenSecret());
      accessToken.setRecordId(new RecordId(credential.getRecordId(), null));
    }
    catch (IndivoException e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMON_DatabaseError_ErrorLoadingAccessToken(), e);
    }
    return accessToken;
  }

  @Override
  public AhdCredential loadAhdCredentialByRecordId(String indivo_recordId) throws ContinuaConnectorException {
    logger.debug("Start executing loadAhdCredentialByRecordId ...");
    Credential credential = dbQueries.loadCredentialByRecordId(indivo_recordId);
    return new AhdCredential(credential.getAccessKey(), credential.getControlKey());
  }

  @Override
  public boolean isAccessTokenAlreadySaved(String indivo_recordId) throws ContinuaConnectorException {
    logger.debug("Start executing isAccessTokenAlreadySaved ...");
    return dbQueries.isCredentialStored(indivo_recordId);
  }
}

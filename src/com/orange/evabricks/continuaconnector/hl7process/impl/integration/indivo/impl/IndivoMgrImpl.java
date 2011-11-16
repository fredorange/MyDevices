package com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.config.IndivoConfig;
import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.IndivoMgr;
import com.orange.jlinx.Indivo;
import com.orange.jlinx.IndivoException;
import org.apache.log4j.Logger;

@Component("indivoMgr")
@Scope("singleton")
public class IndivoMgrImpl implements IndivoMgr {

  private static final Logger logger = Logger.getLogger(IndivoMgrImpl.class);
  private Indivo indivo = null;
  @Resource(name = "indivoConfig")
  private IndivoConfig indivoConfig;
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;

  public IndivoMgrImpl() {
  }

  @Override
  public Indivo getIndivo() throws ContinuaConnectorException {
    if (indivo == null) {
      logger.debug("IndivoMgrImpl constructor : call initIndivo()");
      initIndivo();
    }
    return indivo;
  }

  private void initIndivo() throws ContinuaConnectorException {
    logger.debug("initIndivo");

    try {
      logger.debug("indivoCconfig : " + indivoConfig);
      indivo = new Indivo(indivoConfig.getPHA_CONSUMER_KEY(),
              indivoConfig.getPHA_CONSUMER_SECRET(),
              indivoConfig.getPHA_APP_ID(),
              indivoConfig.getINDIVO_SERVER_URL(),
              indivoConfig.getINDIVO_UI_SERVER_URL());
      logger.debug("indivo : " + indivo);
    } catch (MalformedURLException e) {
      logger.error("Configuration error for Indivo : bad URL.", e);
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMON_IndivoException_BadUrl(), e);
    } catch (IndivoException e) {
      logger.error("Unexpected error when instanciating indivo.", e);
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMON_IndivoException_UnexpectedException(), e);
    } catch (Exception e) {
      logger.error("Unexpected error when instanciating indivo.", e);
      throw new ApplicationInternalErrorException(applicationErrorCodes.getCOMMON_IndivoException_UnexpectedException(), e);
    }

  }
}

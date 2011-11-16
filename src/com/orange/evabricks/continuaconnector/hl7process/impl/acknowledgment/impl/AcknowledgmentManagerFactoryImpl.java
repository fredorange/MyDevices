package com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.AcknowledgmentManager;
import com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.AcknowledgmentManagerFactory;
import com.orange.evabricks.continuaconnector.hl7process.impl.commons.HL7MessageParser;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/***
 *
 * @author tmdn5264
 *
 * The AcknowledgmentManagerFactory creates the AcknowledgmentManager object corresponding to the type of the HL7 message received.
 */
@Component("acknowledgmentManagerFactory")
@Scope("singleton")
public class AcknowledgmentManagerFactoryImpl implements AcknowledgmentManagerFactory {
 private static final Logger logger = Logger.getLogger(AcknowledgmentManagerFactory.class);
  @Resource(name = "hl7MessageParser")
  private HL7MessageParser hl7MessageParser;
  @Resource(name = "oRU_R01AcknowledgmentManager")
  private ORU_R01AcknowledgmentManager oRU_R01AcknowledgmentManager;
  @Resource(name = "defaultAcknowledgmentManager")
  private DefaultAcknowledgmentManager defaultAcknowledgmentManager;

  public AcknowledgmentManagerFactoryImpl() {
  }

  /**
   * Get the AcknowledgmentManager corresponding to the HL7 message
   * @param hl7message The HL7 message that has been processed.
   * @return The AcknowledgmentManager corresponding to the HL7 message
   */
  @Override
  public AcknowledgmentManager getAcknowlegmentManager(String hl7message) {
    if (hl7message != null) {

      if (hl7MessageParser.isORU_R01(hl7message)) {
        logger.info("ORU_R01AcknowledgmentManager has been selected.");
        return oRU_R01AcknowledgmentManager;
      }
    }
    logger.info("DefaultAcknowledgmentManager has been selected.");
    return defaultAcknowledgmentManager;
  }
}

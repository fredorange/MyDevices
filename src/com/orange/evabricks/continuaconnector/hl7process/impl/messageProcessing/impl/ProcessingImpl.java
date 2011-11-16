package com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing.impl;


import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.acknowledgment.Acknowledging;
import com.orange.evabricks.continuaconnector.hl7process.impl.commons.QuickHl7FieldExtractor;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.messageProcessing.Processing;

/**
 * This class is the entry point of the API.
 * This class providIes methods which process the hl7 message.
 * @author tmdn5264
 *
 */
@Component("processing")
@Scope("singleton")
public class ProcessingImpl implements Processing {

  private static final Logger logger = Logger.getLogger(ProcessingImpl.class);
  @Resource(name = "processorFactory")
  private ProcessorFactoryImpl processorFactory;
  @Resource(name = "acknowledging")
  private Acknowledging acknowledging;
  @Resource(name = "quickHl7FieldExtractor")
  private QuickHl7FieldExtractor quickHl7FieldExtractor;

  public ProcessingImpl() {
  }

  @Override
  public void processMessage(String accessKey, String hl7Message) throws ContinuaConnectorException {
    logger.info("Start to process message ...");
    logger.info("accessKey=" + accessKey);
    logger.info("HL7 message:\r" + hl7Message);
    processorFactory.getProcessor(hl7Message).processMessage(accessKey);
  }

  @Override
  public String processMessageAndReturnHL7Ack(String accessKey, String hl7Message) {

    boolean success = false;

    ContinuaConnectorException exception = null;

    // Process message
    logger.info("Start to process message ...");
    logger.info("accessKey=" + accessKey);
    logger.info("HL7 message: \r" + hl7Message);
    try {
      processorFactory.getProcessor(hl7Message).processMessage(accessKey);
      success = true;
    } catch (ContinuaConnectorException e) {
      exception = e;
      logger.error("Error when processing message. This error will be reported in the ack message.", e);
    }

    // Generate ack
    logger.info("Start to generate ack ...");
    try {
      String ack = acknowledging.acknowledge(hl7Message, success, exception);
//      if (ack != null) {
//        ack = ack.replace("\r", "\n");
//      }
      logger.info("Ack returned : \r" + ack.replace("\r", "\n"));
      return ack;
    } catch (ContinuaConnectorException e) {
      logger.error("Error when selecting the acknowledgment manager. Please verify AcknowledgmentManagerFactory.getAcknowlegmentManager() method.", e);
    }
    return "An error occured when generating acknowledgment.";

  }

  @Override
  public void processMessage(String hl7Message) throws ContinuaConnectorException {
    // get accessKey from HL7 message
    String accessKey = quickHl7FieldExtractor.getPidFromHL7Message(hl7Message);

    // process message with accessKey
    processMessage(accessKey, hl7Message);
  }

  @Override
  public String processMessageAndReturnHL7Ack(String hl7Message) {
    try {
      // get accessKey from HL7 message
      String accessKey = quickHl7FieldExtractor.getPidFromHL7Message(hl7Message);

      // process message with accessKey
      return processMessageAndReturnHL7Ack(accessKey, hl7Message);

    } catch (ContinuaConnectorException e) {
      logger.error("An error occured when getting PID from HL7 message.", e);

      // Generate error ack
      logger.info("Start to generate error ack ...");
      try {
        String ack = acknowledging.acknowledge(hl7Message, false, e);
//        if (ack != null) {
//          ack = ack.replace("\r", "\n");
//        }
        logger.info("Ack returned : \r" + ack.replace("\r", "\n"));
        return ack;
      } catch (ContinuaConnectorException ex) {
        logger.error("Error when selecting the acknowledgment manager. Please verify AcknowledgmentManagerFactory.getAcknowlegmentManager() method.", ex);
        return "PID-3-1 (accessKey) not found in the message. An error occured when generating error acknowledgment.";
      }
    }
  }
}

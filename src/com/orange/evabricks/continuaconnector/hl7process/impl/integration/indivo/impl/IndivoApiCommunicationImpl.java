package com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.ErrorCode;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.IndivoApiCommunication;
import com.orange.evabricks.continuaconnector.hl7process.impl.errorsManagement.errorCodes.applicationErrorCodes.ApplicationErrorCodes;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.ContinuaConnectorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.exceptions.hl7CompliantExceptions.ApplicationInternalErrorException;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.AccessTokenMgr;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.IndivoMgr;
import com.orange.evabricks.continuaconnector.hl7process.impl.integration.indivo.model.MyIndivoGroupedVitalSignsAndEquipment;
import com.orange.jlinx.Indivo;
import com.orange.jlinx.IndivoException;
import com.orange.jlinx.QueryFilter;
import com.orange.jlinx.auth.AccessToken;
import com.orange.jlinx.document.DocumentManager;
import com.orange.jlinx.document.ext.DocumentMeta;
import com.orange.jlinx.document.ext.Report;
import com.orange.jlinx.document.medical.Equipment;
import com.orange.jlinx.document.medical.VitalSign;
import com.orange.jlinx.messaging.MessagingManager;
import java.util.ArrayList;

@Component("indivoApiCommunication")
@Scope("singleton")
public class IndivoApiCommunicationImpl implements IndivoApiCommunication {

  private static final Logger logger = Logger.getLogger(IndivoApiCommunicationImpl.class);
  private DocumentManager<VitalSign> vitalSignMgr;
  private DocumentManager<Equipment> equipmentManager;
  @Resource(name = "indivoMgr")
  private IndivoMgr indivoMgr;
  @Resource(name = "accessTokenMgr")
  private AccessTokenMgr accessTokenMgr;
  private MessagingManager messagingManager;
  @Resource(name = "applicationErrorCodes")
  private ApplicationErrorCodes applicationErrorCodes;

  public IndivoApiCommunicationImpl() {
  }

  private void initDocumentManagers() throws ContinuaConnectorException {
    logger.debug("Start executing initDocumentManagers ...");


    logger.debug("Start creation of Indivo instance ...");
    Indivo indivo = getIndivoMgr().getIndivo();

    if (vitalSignMgr == null) {
      // Create a VitalSignManager to add and read VitalSign documents
      try {
        logger.debug("Start initialisation of vitalSignManager ...");
        vitalSignMgr = indivo.getDocumentManager(VitalSign.DOCUMENT_TYPE);
      } catch (IndivoException e) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorInitializingVitalSignManager(), e);
      } catch (Exception e) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorInitializingVitalSignManager(), e);
      }
    }

    if (equipmentManager == null) {
      // Create an EquipmentManager to add and read Equipment documents
      try {
        logger.debug("Start initialisation of equipmentManager ...");
        equipmentManager = indivo.getDocumentManager(Equipment.DOCUMENT_TYPE);
      } catch (IndivoException e) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorInitializingEquipmentManager(), e);
      } catch (Exception e) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorInitializingEquipmentManager(), e);
      }
    }

    if (messagingManager == null) {
      // Create a MessagingManager to send notifications
      try {
        logger.debug("Start initialisation of messagingManager ...");
        messagingManager = indivo.getMessagingManager();
      } catch (IndivoException e) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorInitializingMessagingManager(), e);
      } catch (Exception e) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorInitializingMessagingManager(), e);
      }
    }
  }

  @Override
  public void sendNotification(String accessKey, String text) throws ContinuaConnectorException {
    logger.info("Start executing sendNotification ...");
    initDocumentManagers();

    /**********************************************/
    /********* Get access token ******************/
    /**********************************************/
    AccessToken token = null;
    logger.info("loadAccessTokenByAccessKey : " + accessKey);
    try {
      token = getAccessTokenMgr().loadAccessTokenByAccessKey(accessKey);
      logger.info("RecordId : " + token.getRecordId());
    } catch (ApplicationInternalErrorException e) {
      throw e;
    } catch (Exception e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINTEGRATION_ErrorLoadingAccessToken(), e);
    }

    /**********************************************/
    /********* Send notification ******************/
    /**********************************************/
    // Indivo doesn't accept more than 500 characters in the notification.
    String text2Send = text;
    if (text.length() > 500) {
      text2Send = text.substring(0, Math.min(text.length() - 1, 494)) + "[...]";
    }

    try {
      messagingManager.sendNotificationToRecord(token, token.getRecordId().getId(), text2Send, null, null);
    } catch (IndivoException e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorWhenSendingNotification(), e);
    }
  }

  private void linkVitalSign(AccessToken accessToken, String documentId, String relatedDocumentId) throws ApplicationInternalErrorException {
    try {
      boolean linkedOk = vitalSignMgr.linkDocumentToExisting(accessToken, documentId, DocumentManager.RELATION_TYPE_ANNOTATION, relatedDocumentId);
      logger.info("vitalSign linkedOk : " + linkedOk);
      if (!linkedOk) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_LinkingVitalSignFailed());
      }
    } catch (IndivoException e) {
      logger.error("An error occured when linking equipement to vital sign.", e);
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorLinkingVitalSign());
    } catch (Exception e) {
      logger.error("An unexpected error occured when linking equipement to vital sign.", e);
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorLinkingVitalSign());
    }
  }

  private void linkEquipment(AccessToken accessToken, String vitalSignDocumentId, String equipmentDocumentId) throws ApplicationInternalErrorException {
    try {
      boolean linkedOk = vitalSignMgr.linkDocumentToExisting(accessToken, vitalSignDocumentId, DocumentManager.RELATION_TYPE_ANNOTATION, equipmentDocumentId);
      logger.info("equipment linkedOk : " + linkedOk);
      if (!linkedOk) {
        throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_LinkingEquipmentFailed());
      }
    } catch (IndivoException e) {
      logger.error("An error occured when linking equipement to vital sign.", e);
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorLinkingEquipment());
    } catch (Exception e) {
      logger.error("An unexpected error occured when linking equipement to vital sign.", e);
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorLinkingEquipment());
    }
  }

  @Override
  public void archiveAllDocuments(String accessKey) throws ContinuaConnectorException {

    logger.debug("Start executing archiveAllDocuments ...");

    initDocumentManagers();

    try {
      AccessToken accessToken = getAccessTokenMgr().loadAccessTokenByAccessKey(accessKey);
      while (!vitalSignMgr.getReports(accessToken, null).getReportList().isEmpty()) {
        for (Report<VitalSign> report : vitalSignMgr.getReports(accessToken, null).getReportList()) {
          vitalSignMgr.setDocumentStatusById(accessToken, report.getMeta().getId(), "archived", "old");
        }
      }
    } catch (IndivoException e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorArchivingDocument(), e);
    } catch (ApplicationInternalErrorException e) {
      throw e;
    } catch (Exception e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorArchivingDocument(), e);
    }

  }

  private String vitalSignToString(VitalSign vitalSign) {
    StringBuilder str = new StringBuilder();
    if (vitalSign != null) {
      if (vitalSign.getDateMeasured() != null) {
        str.append("date : " + vitalSign.getDateMeasured() + "\r");
      }
      if (vitalSign.getName() != null) {
        str.append("name :");
        str.append(vitalSign.getName().getValue() == null ? "" : vitalSign.getName().getValue() + " ");
        str.append(vitalSign.getName().getHumanReadable() == null ? "" : vitalSign.getName().getHumanReadable() + " ");
        str.append(vitalSign.getName().getAbbrev() == null ? "" : vitalSign.getName().getAbbrev() + " ");
        str.append("\r");
      }
      if (vitalSign.getValue() != null) {
        str.append("value : " + vitalSign.getValue() + "\r");
      }

    }
    logger.debug("vitalSignToString : " + str.toString());
    return str.toString();
  }

  /**
   * @return the indivoMgr
   */
  public IndivoMgr getIndivoMgr() {
    return indivoMgr;
  }

  /**
   * @param indivoMgr the indivoMgr to set
   */
  public void setIndivoMgr(IndivoMgr indivoMgr) {
    this.indivoMgr = indivoMgr;
  }

  /**
   * @return the accessTokenMgr
   */
  public AccessTokenMgr getAccessTokenMgr() {
    return accessTokenMgr;
  }

  /**
   * @param accessTokenMgr the accessTokenMgr to set
   */
  public void setAccessTokenMgr(AccessTokenMgr accessTokenMgr) {
    this.accessTokenMgr = accessTokenMgr;
  }

  @Override
  public void postObservations(String accessKey, List<MyIndivoGroupedVitalSignsAndEquipment> observations) throws ContinuaConnectorException {
    logger.info("Start executing postObservations ...");

    initDocumentManagers();


    /**********************************************/
    /********* Get access token ******************/
    /**********************************************/
    AccessToken token = null;
    logger.info("loadAccessTokenByAccessKey : " + accessKey);
    try {
      token = getAccessTokenMgr().loadAccessTokenByAccessKey(accessKey);
      logger.info("RecordId : " + token.getRecordId());
    } catch (ApplicationInternalErrorException e) {
      throw e;
    } catch (Exception e) {
      throw new ApplicationInternalErrorException(applicationErrorCodes.getINTEGRATION_ErrorLoadingAccessToken(), e);
    }

    for (MyIndivoGroupedVitalSignsAndEquipment myIndivoVitalSigns : observations) {
      /********************************************************************************/
      /***  Post each VitalSign (and its equiment) on the PHR using the indivo_api ****/
      /********************************************************************************/
      String previousVitalSignDocumentId = null; // to link vital signs, each vital sign will be linked to the previous vital sign
      String firstVitalSignDocumentId = null; // the last vital sign will be linked with the first one.
      for (VitalSign vitalSign : myIndivoVitalSigns.getGroupedVitalSigns()) {
        logger.debug("vitalSign : " + vitalSignToString(vitalSign));
        Equipment equipment = myIndivoVitalSigns.getEquipment();
        logger.debug("equipment : " + equipment);


        /**********************************************/
        /******** Posting vital sign *******************/
        /**********************************************/
        DocumentMeta metaVitalSign = null;
        logger.info("Start posting vital sign.");
        try {
          metaVitalSign = vitalSignMgr.postDocument(token, vitalSign, false);
        } catch (IndivoException e) {
          throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorPostingVitalSign(), e);
        } catch (Exception e) {
          throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorPostingVitalSign(), e);
        }

        if (metaVitalSign == null || metaVitalSign.getId() == null || metaVitalSign.getId().isEmpty()) {
          throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_PostingVitalSignFailed());
        }


        /**********************************************/
        /************** Link vital signs ****************/
        /**********************************************/
        // Each vital lisn is linked with the previous vital sign.
        if (previousVitalSignDocumentId != null) {
          linkVitalSign(token, previousVitalSignDocumentId, metaVitalSign.getId());

        }

        // update "previousVitalSignDocumentId" for the next iteration
        previousVitalSignDocumentId = metaVitalSign.getId();

        // if this is the first iteration, save the documentId in order to link it at the end with the last vital sign.
        if (myIndivoVitalSigns.getGroupedVitalSigns().indexOf(vitalSign) == 0) {
          firstVitalSignDocumentId = metaVitalSign.getId();
        }

        // if this is the last iteration, link the last vital sign with the first one (only if there are several vital signs)
        if (myIndivoVitalSigns.getGroupedVitalSigns().size() > 1) {
          if (myIndivoVitalSigns.getGroupedVitalSigns().indexOf(vitalSign) == myIndivoVitalSigns.getGroupedVitalSigns().size() - 1) {
            linkVitalSign(token, metaVitalSign.getId(), firstVitalSignDocumentId);
          }
        }

        /**********************************************/
        /*********** Process equipment ****************/
        /**********************************************/
        if (equipment != null) {

          /**********************************************/
          /*********** Posting equipment ****************/
          /**********************************************/
          DocumentMeta metaEquipment = null;
          if (equipment.getId() == null || equipment.getId().isEmpty()) {
            // equipement id is required.
            ErrorCode errorCode = applicationErrorCodes.getINTEGRATION_DevError_MissingDeviceId();
            throw new ApplicationInternalErrorException(errorCode);
          }
          logger.info("Equipement id: " + equipment.getId());

          metaEquipment = this.getEquipementDocument(token, equipment.getId());
          logger.info("MetaDocument get by the method getDocumentMetaByExternalId() successfully.");

          // if the equipment not already exists (ie metaEquipment is null), we post it
          if (metaEquipment == null || metaEquipment.getId() == null || metaEquipment.getId().isEmpty()) {
            logger.info("Equipment doesn't already exist. Start posting equipment.");
            metaEquipment = postEquipementDocument(token, equipment);

            // Verification if the Equipment has been correctly posted.
            if (metaEquipment == null || metaEquipment.getId() == null || metaEquipment.getId().isEmpty()) {
              ErrorCode errorCode = applicationErrorCodes.getINDIVO_API_PostingEquipmentFailed();
              errorCode.setValueComplement("MetaEquipement returned after posting Equipement document is null.");
              throw new ApplicationInternalErrorException(errorCode);
            }
          }

          /**********************************************/
          /************** Link equipment ****************/
          /**********************************************/
          linkEquipment(token, metaVitalSign.getId(), metaEquipment.getId());

        }

        /**********************************************/
        /************** Send notification ****************/
        /**********************************************/
        String notif = "A VITAL SIGN has been added:" + vitalSign.getDateMeasured() + " / " + vitalSign.getName().getHumanReadable() + " / " + vitalSign.getValue() + " / " + vitalSign.getUnit().getHumanReadable();
        if (equipment != null) {
          notif += " for EQUIPMENT: " + equipment.getName() + " / " + equipment.getId() + " / " + equipment.getType() + " / " + equipment.getVendor() + " / " + equipment.getDescription() + " / " + equipment.getCertification() + " / " + equipment.getSpecification();
        }
        this.sendNotification(accessKey, notif);
      }
    }
  }

  private DocumentMeta getEquipementDocument(AccessToken accessToken, String equipementId) throws ApplicationInternalErrorException {
    //*************** with external id ********************//
//    try {
//      return equipmentManager.getDocumentMetaByExternalId(accessToken, equipementId, true);
//     catch  (IndivoException e) {
//      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorGettingEquipmentMetaByExternalId(), e);
//    }

    //*************** without external id ********************//
    List<Report<Equipment>> allEquipements = this.getAllEquipementsFromIndivo(accessToken);
    for (Report<Equipment> report : allEquipements) {
      if (equipementId.equals(report.getDocument().getId())) {
        return report.getMeta();
      }
    }
    return null;

    //*********** sans traiter unicité équipement *********************//
//    return null;

  }


  private List<Report<Equipment>> getAllEquipementsFromIndivo(AccessToken accessToken) throws ApplicationInternalErrorException {
    // list which will contain all vital signs
    List<Report<Equipment>> allEquipements = new ArrayList<Report<Equipment>>();

    QueryFilter filter = new QueryFilter();
    int limit = 200;
    filter.setLimit(limit);

    List<Report<Equipment>> someEquipements;
    int offset = 0;
    boolean stop = false;
    while (!stop) {
      filter.setOffset(offset);
      try {
        someEquipements = equipmentManager.getReports(accessToken, filter).getReportList();
      } catch (IndivoException e) {
        ErrorCode errorCode = applicationErrorCodes.getINTEGRATION_ErrorCheckingIfEquipementExists();
        errorCode.setValueComplement("Error when getting all equipements from Indivo.");
        throw new ApplicationInternalErrorException(errorCode, e);
      }
      logger.debug("someVitalSigns.size() : " + someEquipements.size());
      if (someEquipements.isEmpty()) {
        stop = true;
      } else {
        offset += limit;
        allEquipements.addAll(someEquipements);
      }
    }
    return allEquipements;
  }

  private DocumentMeta postEquipementDocument(AccessToken accessToken, Equipment equipment) throws ApplicationInternalErrorException {

    //*************** with external id ********************//
//    try {
//      return equipmentManager.postDocumentByExternalId(accessToken, equipment, equipment.getId(), false);
//    } catch (IndivoException e) {
//      throw new ApplicationInternalErrorException(applicationErrorCodes.getINDIVO_API_ErrorPostingEquipmentByExternalId(), e);
//    }

    //*************** without external id ********************//
    try {
      return equipmentManager.postDocument(accessToken, equipment, false);
    } catch (IndivoException e) {
      ErrorCode errorCode = applicationErrorCodes.getINDIVO_API_ErrorPostingEquipment();
      throw new ApplicationInternalErrorException(errorCode, e);
    }
  }
}

class MyVitalSign {

  private VitalSign vitalSign;
  private Equipment linkedEquipment;

  public MyVitalSign(VitalSign vitalSign, Equipment linkedEquipment) {
    this.vitalSign = vitalSign;
    this.linkedEquipment = linkedEquipment;
  }

  /**
   * @return the vitalSign
   */
  public VitalSign getVitalSign() {
    return vitalSign;
  }

  /**
   * @param vitalSign the vitalSign to set
   */
  public void setVitalSign(VitalSign vitalSign) {
    this.vitalSign = vitalSign;
  }

  /**
   * @return the linkedEquipment
   */
  public Equipment getLinkedEquipment() {
    return linkedEquipment;
  }

  /**
   * @param linkedEquipment the linkedEquipment to set
   */
  public void setLinkedEquipment(Equipment linkedEquipment) {
    this.linkedEquipment = linkedEquipment;
  }
}

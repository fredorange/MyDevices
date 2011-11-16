package com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.impl;

import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.DeviceItem;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.MeasureType;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.MedicalDeviceSystem;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.SegmentIdentificationManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author tmdn5264
 */
@Component("segmentIdentificationManager")
public class SegmentIdentificationManagerImpl implements SegmentIdentificationManager {

  private Map<String, MedicalDeviceSystem> mappingMedicalDeviceSystem;
  private Map<String, MeasureType> mappingMeasureType;
  private Map<MedicalDeviceSystem, List<MeasureType>> mappingMdsMeasureType;
  private Map<String, DeviceItem> mappingDeviceItem;

  public SegmentIdentificationManagerImpl() {

    this.mappingDeviceItem = new HashMap<String, DeviceItem>();
    this.mappingDeviceItem.put("67974", DeviceItem.SYSTEM_TYPE);
    this.mappingDeviceItem.put("531969", DeviceItem.SYSTEM_MODEL);
    this.mappingDeviceItem.put("531970", DeviceItem.SYSTEM_MANUFACTURER);
    this.mappingDeviceItem.put("67972", DeviceItem.SYSTEM_ID);
    this.mappingDeviceItem.put("531971", DeviceItem.PRODUCTION_SPECIFICATION_UNSPECIFIED);
    this.mappingDeviceItem.put("531972", DeviceItem.PRODUCTION_SPECIFICATION_SERIAL);
    this.mappingDeviceItem.put("531973", DeviceItem.PRODUCTION_SPECIFICATION_PART);
    this.mappingDeviceItem.put("531974", DeviceItem.PRODUCTION_SPECIFICATION_HARDWARE);
    this.mappingDeviceItem.put("531975", DeviceItem.PRODUCTION_SPECIFICATION_SOFTWARE);
    this.mappingDeviceItem.put("531976", DeviceItem.PRODUCTION_SPECIFICATION_FIRMWARE);
    this.mappingDeviceItem.put("531977", DeviceItem.PRODUCTION_SPECIFICATION_PROTOCOL);
    this.mappingDeviceItem.put("531978", DeviceItem.PRODUCTION_SPECIFICATION_GMDN_GROUP);
    this.mappingDeviceItem.put("67975", DeviceItem.DATA_AND_TIME);
    this.mappingDeviceItem.put("67983", DeviceItem.RELATIVE_TIME);
    this.mappingDeviceItem.put("68072", DeviceItem.HIRES_RELATIVE_TIME);
    this.mappingDeviceItem.put("68219", DeviceItem.MDS_TIME_CAP_STATE);
    this.mappingDeviceItem.put("68220", DeviceItem.TIME_SYNC_PROTOCOL);
    this.mappingDeviceItem.put("68221", DeviceItem.TIME_SYNC_ACCURACY);
    this.mappingDeviceItem.put("68222", DeviceItem.TIME_RESOLUTION_ABS_TIME);
    this.mappingDeviceItem.put("68223", DeviceItem.TIME_RESOLUTION_REL_TIME);
    this.mappingDeviceItem.put("68224", DeviceItem.TIME_RESOLUTION_HIGH_RES_TIME);
    this.mappingDeviceItem.put("67925", DeviceItem.POWER_STATUS);
    this.mappingDeviceItem.put("67996", DeviceItem.BATTERY_LEVEL);
    this.mappingDeviceItem.put("67976", DeviceItem.REMAINING_BATTERY_TIME);
    this.mappingDeviceItem.put("68218", DeviceItem.REGULATION_CERTIFICATION_AUTH_BODY);
    this.mappingDeviceItem.put("588800", DeviceItem.REGULATION_CERTIFICATION_CONTINUA_VERSION);
    this.mappingDeviceItem.put("588801", DeviceItem.REGULATION_CERTIFICATION_CONTINUA_CERTIFIED_DEVICE_LIST);
    this.mappingDeviceItem.put("588802", DeviceItem.REGULATION_CERTIFICATION_CONTINUA_REGULATION_STATUS);
    this.mappingDeviceItem.put("68186", DeviceItem.SYSTEM_TYPE_SPEC_LIST);



    this.mappingMedicalDeviceSystem = new HashMap<String, MedicalDeviceSystem>();
    this.mappingMedicalDeviceSystem.put("528391", MedicalDeviceSystem.BLOOD_PRESSURE);
    this.mappingMedicalDeviceSystem.put("528399", MedicalDeviceSystem.WEIGHING_SCALE);
    this.mappingMedicalDeviceSystem.put("528392", MedicalDeviceSystem.THERMOMETER);

    this.mappingMeasureType = new HashMap<String, MeasureType>();
    this.mappingMeasureType.put("188736", MeasureType.BODY_WEIGHT);
    this.mappingMeasureType.put("188740", MeasureType.BODY_HEIGHT);
    this.mappingMeasureType.put("188752", MeasureType.BODY_MASS_INDEX);

    this.mappingMeasureType.put("150021", MeasureType.BP_SYSTOLIC);
    this.mappingMeasureType.put("150022", MeasureType.BP_DIASTOLIC);
    this.mappingMeasureType.put("150023", MeasureType.BP_MEAN);
    this.mappingMeasureType.put("149546", MeasureType.PULSE_RATE);

    this.mappingMeasureType.put("188452", MeasureType.TEMP_AXILLA);
    this.mappingMeasureType.put("150364", MeasureType.TEMP_BODY);
    this.mappingMeasureType.put("188428", MeasureType.TEMP_EAR);
    this.mappingMeasureType.put("188432", MeasureType.TEMP_FINGER);
    this.mappingMeasureType.put("188456", MeasureType.TEMP_GIT);
    this.mappingMeasureType.put("188424", MeasureType.TEMP_ORAL);
    this.mappingMeasureType.put("188420", MeasureType.TEMP_RECT);
    this.mappingMeasureType.put("188448", MeasureType.TEMP_TOE);
    this.mappingMeasureType.put("150392", MeasureType.TEMP_TYMP);

    this.mappingMdsMeasureType = new HashMap<MedicalDeviceSystem, List<MeasureType>>();

    List bp = new ArrayList<MeasureType>();
    bp.add(MeasureType.BP_SYSTOLIC);
    bp.add(MeasureType.BP_DIASTOLIC);
    bp.add(MeasureType.BP_MEAN);
    bp.add(MeasureType.PULSE_RATE);
    this.mappingMdsMeasureType.put(MedicalDeviceSystem.BLOOD_PRESSURE, bp);

    List weight = new ArrayList<MeasureType>();
    weight.add(MeasureType.BODY_WEIGHT);
    weight.add(MeasureType.BODY_HEIGHT);
    weight.add(MeasureType.BODY_MASS_INDEX);
    this.mappingMdsMeasureType.put(MedicalDeviceSystem.WEIGHING_SCALE, weight);

    List temp = new ArrayList<MeasureType>();
    temp.add(MeasureType.TEMP_AXILLA);
    temp.add(MeasureType.TEMP_BODY);
    temp.add(MeasureType.TEMP_EAR);
    temp.add(MeasureType.TEMP_FINGER);
    temp.add(MeasureType.TEMP_GIT);
    temp.add(MeasureType.TEMP_ORAL);
    temp.add(MeasureType.TEMP_RECT);
    temp.add(MeasureType.TEMP_TOE);
    this.mappingMdsMeasureType.put(MedicalDeviceSystem.THERMOMETER, temp);
  }

  @Override
  public MedicalDeviceSystem getMedicalDeviceSystem(String identifier, String nameOfCodingSystem) {
    if (identifier == null || nameOfCodingSystem == null) {
      return MedicalDeviceSystem.UNKNOWN;
    }
    if (!nameOfCodingSystem.equals("MDC")) {
      return MedicalDeviceSystem.UNKNOWN;
    }
    MedicalDeviceSystem mds = mappingMedicalDeviceSystem.get(identifier);
    if (mds != null) {
      return mds;
    }
    return MedicalDeviceSystem.UNKNOWN;
  }

  @Override
  public boolean isMeasure(String mdsIdentifier, String mdsNameOfCodingSystem, String measureIdentifier, String measureNameOfCodingSystem) {
    if (mdsIdentifier == null || mdsNameOfCodingSystem == null || measureIdentifier == null || measureNameOfCodingSystem == null) {
      return false;
    }
    if (!mdsNameOfCodingSystem.equals("MDC") || !measureNameOfCodingSystem.equals("MDC")) {
      return false;
    }

    return !MeasureType.UNKNOWN.equals(getMeasureType(mdsIdentifier, mdsNameOfCodingSystem, measureIdentifier, measureNameOfCodingSystem));

  }

  @Override
  public boolean isDevice(String deviceIdentifier, String deviceNameOfCodingSystem) {
    if (deviceIdentifier == null || deviceNameOfCodingSystem == null) {
      return false;
    }
    if (!deviceNameOfCodingSystem.equals("MDC")) {
      return false;
    }

    return !DeviceItem.UNKNOWN.equals(getDeviceItem(deviceIdentifier, deviceNameOfCodingSystem));
  }

//  @Override
//  public IsMeasureOrDevice isMeasureOrDevice(String mdsIdentifier, String mdsNameOfCodingSystem, String measureOrDeviceIdentifier, String measureOrDeviceNameOfCodingSystem) {
//    if (!mdsNameOfCodingSystem.equals("MDC") || !measureOrDeviceNameOfCodingSystem.equals("MDC")) {
//      return IsMeasureOrDevice.UNKNOWN;
//    }
//
//    DeviceItem deviceItem = getDeviceItem(measureOrDeviceIdentifier, measureOrDeviceNameOfCodingSystem);
//    if (!DeviceItem.UNKNOWN.equals(deviceItem)) {
//      return IsMeasureOrDevice.DEVICE;
//    }
//
//    MeasureType measureType = getMeasureType(mdsIdentifier, mdsNameOfCodingSystem, measureOrDeviceIdentifier, measureOrDeviceNameOfCodingSystem);
//    if (!MeasureType.UNKNOWN.equals(measureType)) {
//      return IsMeasureOrDevice.MEASURE;
//    }
//
//    return IsMeasureOrDevice.UNKNOWN;
//  }
  private boolean isValidMeasureTypeForMds(MedicalDeviceSystem mds, MeasureType measureType) {
    if (mds == null || measureType == null) {
      return false;
    }
    List<MeasureType> acceptedMeasureTypes = this.mappingMdsMeasureType.get(mds);
    for (MeasureType m : acceptedMeasureTypes) {
      if (m.equals(measureType)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public MeasureType getMeasureType(String mdsIdentifier, String mdsNameOfCodingSystem, String measureIdentifier, String measureNameOfCodingSystem) {
    if (mdsIdentifier == null || mdsNameOfCodingSystem == null || measureIdentifier == null || measureNameOfCodingSystem == null) {
      return MeasureType.UNKNOWN;
    }
    if (!mdsNameOfCodingSystem.equals("MDC") || !measureNameOfCodingSystem.equals("MDC")) {
      return MeasureType.UNKNOWN;
    }

    MedicalDeviceSystem mds = this.mappingMedicalDeviceSystem.get(mdsIdentifier);
    MeasureType measureType = this.mappingMeasureType.get(measureIdentifier);

    if (isValidMeasureTypeForMds(mds, measureType)) {
      return measureType;
    }

    return MeasureType.UNKNOWN;

  }

  @Override
  public DeviceItem getDeviceItem(String identifier, String nameOfCodingSystem) {
    if (identifier == null || nameOfCodingSystem == null) {
      return DeviceItem.UNKNOWN;
    }
    if (!nameOfCodingSystem.equals("MDC")) {
      return DeviceItem.UNKNOWN;
    }
    DeviceItem deviceItem = mappingDeviceItem.get(identifier);
    if (deviceItem != null) {
      return deviceItem;
    }
    return DeviceItem.UNKNOWN;
  }

  @Override
  public boolean isValidUnitForMeasureType(String measureIdentifier, String measureNameOfCodingSystem,
          String unitIdentifier, String unitNameOfCodingSystem) {
    if (measureIdentifier == null || measureNameOfCodingSystem == null || unitIdentifier == null || unitNameOfCodingSystem == null) {
      return false;
    }
    // TODO : implementer isValidUnitForMeasureType


    return true;
  }
}

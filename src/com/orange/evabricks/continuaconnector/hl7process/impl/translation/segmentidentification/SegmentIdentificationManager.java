package com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification;

import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.DeviceItem;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.IsMeasureOrDevice;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.MeasureType;
import com.orange.evabricks.continuaconnector.hl7process.impl.translation.segmentidentification.MeasureAndDeviceTypes.MedicalDeviceSystem;

/**
 *
 * @author tmdn5264
 */
public interface SegmentIdentificationManager {

  /**
   * Identify the type of the device.
   * @param identifier identifier field of OBX-3
   * @param nameOfCodingSystem name of coding system field of OBX-3
   * @return enum type MedicalDeviceSystem representing the type of the device
   */
  public MedicalDeviceSystem getMedicalDeviceSystem(String identifier, String nameOfCodingSystem);


  /**
   * Indicate if the item corresponds to a device or to a measure for the specified MDS.
    * @param mdsIdentifier MDS identifier
    * @param mdsNameOfCodingSystem MDS name of coding system
   * @param measureOrDeviceIdentifier measure or device identifier
   * @param measureOrDeviceNameOfCodingSystem measure or device name of coding system
   * @return enum type IsMeasureOrDevice indicating if the item is a measure or a device
   */
 // public IsMeasureOrDevice isMeasureOrDevice(String mdsIdentifier, String mdsNameOfCodingSystem, String measureOrDeviceIdentifier, String measureOrDeviceNameOfCodingSystem);


  public boolean isMeasure(String mdsIdentifier, String mdsNameOfCodingSystem, String measureIdentifier, String measureNameOfCodingSystem);

  public boolean isDevice(String deviceIdentifier, String deviceNameOfCodingSystem);

   /**
    * Get the measure type of the measure for the specified MDS
    * @param mdsIdentifier MDS identifier
    * @param mdsNameOfCodingSystem MDS name of coding system
    * @param measureIdentifier measure identifier
    * @param measureNameOfCodingSystem measure name of coding system
    * @return enum type MeasureType representing the type of the measure
    */
  public MeasureType getMeasureType(String mdsIdentifier, String mdsNameOfCodingSystem, String measureIdentifier, String measureNameOfCodingSystem);


  /**
   * Get the device item type of the device item represented by identifier and name of coding system
   * @param identifier identifier of the device item
   * @param nameOfCodingSystem nameOfCodingSystem of the device item
   * @return enum type DeviceItem representing the type of the device item
   */
  public DeviceItem getDeviceItem(String identifier, String nameOfCodingSystem);


  /**
   * Indicate if the unit is a valid unit for a measure type
   * @param measureIdentifier measure identifier
   * @param measureNameOfCodingSystem measure name of coding system
   * @param unitIdentifier unit identifier
   * @param unitNameOfCodingSystem unit name of coding system
   * @return True is the unit is valid. Else false.
   */
   public boolean isValidUnitForMeasureType(String measureIdentifier, String measureNameOfCodingSystem, String unitIdentifier, String unitNameOfCodingSystem);
}

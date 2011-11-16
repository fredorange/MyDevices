package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

/**
 * Represents a CWE type of an HL7 message.
 * @author tmdn5264
 */
public class CodedInfo {

  private String identifier;
  private String text;
  private String nameOfCodingSystem;

  public CodedInfo() {
  }

  public CodedInfo(String identifier, String text, String nameOfCodingSystem) {
    this.identifier = identifier;
    this.text = text;
    this.nameOfCodingSystem = nameOfCodingSystem;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getNameOfCodingSystem() {
    return nameOfCodingSystem;
  }

  public void setNameOfCodingSystem(String nameOfCodingSystem) {
    this.nameOfCodingSystem = nameOfCodingSystem;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof CodedInfo)) {
      return false;
    }
    CodedInfo codedInfoObj = (CodedInfo) obj;
    return this.identifier.equals(codedInfoObj.getIdentifier()) && this.text.equals(codedInfoObj.getText()) && this.nameOfCodingSystem.equals(codedInfoObj.getNameOfCodingSystem());
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public String toString() {
    return identifier != null ? identifier : "" + "^" + text != null ? text : "" + "^" + nameOfCodingSystem != null ? nameOfCodingSystem : "";
  }
}

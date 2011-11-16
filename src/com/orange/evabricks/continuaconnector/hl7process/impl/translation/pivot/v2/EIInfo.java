package com.orange.evabricks.continuaconnector.hl7process.impl.translation.pivot.v2;

/**
 *
 * @author tmdn5264
 */
public class EIInfo {

  private String entityIdentifier;
  private String namespace;

  public EIInfo() {
  }

  public EIInfo(String entityIdentifier, String namespace) {
    this.entityIdentifier = entityIdentifier;
    this.namespace = namespace;
  }

  public String getEntityIdentifier() {
    return entityIdentifier;
  }

  public void setEntityIdentifier(String entityIdentifier) {
    this.entityIdentifier = entityIdentifier;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof EIInfo)) {
      return false;
    }
    EIInfo eiObj = (EIInfo) obj;
    return this.entityIdentifier.equals(eiObj.getEntityIdentifier()) && this.namespace.equals(eiObj.getNamespace());
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + (this.entityIdentifier != null ? this.entityIdentifier.hashCode() : 0);
    hash = 67 * hash + (this.namespace != null ? this.namespace.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString() {
    if (entityIdentifier == null && namespace == null) {
      return null;
    }
    String strEntityIdentifier = entityIdentifier != null ? entityIdentifier : "";
    String strNamespace = namespace != null ? namespace : "";
    String str = strEntityIdentifier;
    if (!strNamespace.isEmpty()) {
      str += "^" + strNamespace;
    }
    return str;
  }
}

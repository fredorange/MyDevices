package com.orange.evabricks.continuaconnector.hl7process.impl.validation.impl.util;

import ca.uhn.hl7v2.model.v26.datatype.CWE;
import ca.uhn.hl7v2.model.v26.datatype.CX;
import ca.uhn.hl7v2.model.v26.datatype.DTM;
import ca.uhn.hl7v2.model.v26.datatype.EI;
import ca.uhn.hl7v2.model.v26.datatype.FN;
import ca.uhn.hl7v2.model.v26.datatype.HD;
import ca.uhn.hl7v2.model.v26.datatype.ID;
import ca.uhn.hl7v2.model.v26.datatype.IS;
import ca.uhn.hl7v2.model.v26.datatype.PT;
import ca.uhn.hl7v2.model.v26.datatype.SI;
import ca.uhn.hl7v2.model.v26.datatype.ST;
import ca.uhn.hl7v2.model.v26.datatype.VID;
import ca.uhn.hl7v2.model.v26.datatype.XPN;

public class MissingFieldDetector {

  public static boolean typeSTIsMissing(ST st) {
    return st == null || st.getValue() == null || st.getValue().isEmpty();
  }

  public static boolean typeFNIsMissing(FN fn) {
    if (fn == null) {
      return true;
    }
    boolean fn1IsMissing = typeSTIsMissing(fn.getFn1_Surname());
    return fn1IsMissing;
  }

  public static boolean typeISIsMissing(IS is) {
    return is == null || is.getValue() == null || is.getValue().isEmpty();
  }

  public static boolean typeCXIsMissing(CX cx) {
    if (cx == null) {
      return true;
    }
    boolean cx1IsMissing = typeSTIsMissing(cx.getCx1_IDNumber());
    boolean cx4IsMissing = typeHDIsMissing(cx.getCx4_AssigningAuthority());
    boolean cx5IsMissing = typeIDIsMissing(cx.getCx5_IdentifierTypeCode());

    return cx1IsMissing && cx4IsMissing && cx5IsMissing;
  }

  public static boolean typeXPNIsMissing(XPN xpn) {
    if (xpn == null) {
      return true;
    }
    boolean xpn1IsMissing = typeFNIsMissing(xpn.getXpn1_FamilyName());
    boolean xpn2IsMissing = typeSTIsMissing(xpn.getXpn2_GivenName());

    return xpn1IsMissing && xpn2IsMissing;
  }

  public static boolean typeHDIsMissing(HD hd) {
    if (hd == null) {
      return true;
    }
    boolean hd1IsMissing = typeISIsMissing(hd.getHd1_NamespaceID());
    boolean hd2IsMissing = typeSTIsMissing(hd.getHd2_UniversalID());
    boolean hd3IsMissing = typeIDIsMissing(hd.getHd3_UniversalIDType());

    return hd1IsMissing && hd2IsMissing && hd3IsMissing;
  }

  public static boolean typePTIsMissing(PT pt) {
    if (pt == null) {
      return true;
    }
    boolean pt1IsMissing = typeIDIsMissing(pt.getPt1_ProcessingID());
    boolean pt2IsMissing = typeIDIsMissing(pt.getPt2_ProcessingMode());

    return pt1IsMissing && pt2IsMissing;
  }

  public static boolean typeDTMIsMissing(DTM dtm) {
    return dtm == null || dtm.getValue() == null || dtm.getValue().isEmpty();
  }

  public static boolean typeVIDIsMissing(VID vid) {
    if (vid == null) {
      return true;
    }
    boolean vid1IsMissing = typeIDIsMissing(vid.getVid1_VersionID());
    boolean vid2IsMissing = typeCWEIsMissing(vid.getVid2_InternationalizationCode());
    boolean vid3IsMissing = typeCWEIsMissing(vid.getVid3_InternationalVersionID());

    return vid1IsMissing && vid2IsMissing && vid3IsMissing;
  }

  public static boolean typeIDIsMissing(ID id) {
    return id == null || id.getValue() == null || id.getValue().isEmpty();
  }

  public static boolean typeSIIsMissing(SI si) {
    return si == null || si.getValue() == null || si.getValue().isEmpty();
  }

  public static boolean typeCWEIsMissing(CWE cwe) {
    if (cwe == null) {
      return true;
    }
    boolean cwe1IsMissing = typeSTIsMissing(cwe.getCwe1_Identifier());
    boolean cwe2IsMissing = typeSTIsMissing(cwe.getCwe2_Text());
    boolean cwe3IsMissing = typeIDIsMissing(cwe.getCwe3_NameOfCodingSystem());

    return cwe1IsMissing && cwe2IsMissing && cwe3IsMissing;
  }

  public static boolean typeEIIsMissing(EI ei) {
    if (ei == null) {
      return true;
    }
    boolean ei1IsMissing = typeSTIsMissing(ei.getEi1_EntityIdentifier());
    boolean ei2IsMissing = typeISIsMissing(ei.getEi2_NamespaceID());
    boolean ei3IsMissing = typeSTIsMissing(ei.getEi3_UniversalID());
    boolean ei4IsMissing = typeIDIsMissing(ei.getEi4_UniversalIDType());

    return ei1IsMissing && ei2IsMissing && ei3IsMissing && ei4IsMissing;
  }
}

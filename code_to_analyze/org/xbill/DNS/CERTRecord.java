// SPDX-License-Identifier: BSD-2-Clause
// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base64;

/**
 * Certificate Record - Stores a certificate associated with a name. The certificate might also be
 * associated with a KEYRecord.
 *
 * @see KEYRecord
 * @author Brian Wellington
 * @see <a href="https://tools.ietf.org/html/rfc4398">RFC 4398: Storing Certificates in the Domain
 *     Name System (DNS)</a>
 */
public class CERTRecord extends Record {

  /** PKIX (X.509v3) */
  public static final int PKIX = CertificateType.PKIX;

  /** Simple Public Key Infrastructure */
  public static final int SPKI = CertificateType.SPKI;

  /** Pretty Good Privacy */
  public static final int PGP = CertificateType.PGP;

  /** Certificate format defined by URI */
  public static final int URI = CertificateType.URI;

  /** Certificate format defined by IOD */
  public static final int OID = CertificateType.OID;

  private int certType, keyTag;
  private int alg;
  private byte[] cert;

  CERTRecord() {}

  /**
   * Creates a CERT Record from the given data
   *
   * @param certType The type of certificate (see constants)
   * @param keyTag The ID of the associated KEYRecord, if present
   * @param alg The algorithm of the associated KEYRecord, if present
   * @param cert Binary data representing the certificate
   */
  public CERTRecord(
      Name name, int dclass, long ttl, int certType, int keyTag, int alg, byte[] cert) {
    super(name, Type.CERT, dclass, ttl);
    this.certType = checkU16("certType", certType);
    this.keyTag = checkU16("keyTag", keyTag);
    this.alg = checkU8("alg", alg);
    this.cert = cert;
  }

  @Override
  protected void rrFromWire(DNSInput in) throws IOException {
    certType = in.readU16();
    keyTag = in.readU16();
    alg = in.readU8();
    cert = in.readByteArray();
  }

  @Override
  protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
    String certTypeString = st.getString();
    certType = CertificateType.value(certTypeString);
    if (certType < 0) {
      throw st.exception("Invalid certificate type: " + certTypeString);
    }
    keyTag = st.getUInt16();
    String algString = st.getString();
    alg = DNSSEC.Algorithm.value(algString);
    if (alg < 0) {
      throw st.exception("Invalid algorithm: " + algString);
    }
    cert = st.getBase64();
  }

  /** Converts rdata to a String */
  @Override
  protected String rrToString() {
    StringBuilder sb = new StringBuilder();
    sb.append(certType);
    sb.append(" ");
    sb.append(keyTag);
    sb.append(" ");
    sb.append(alg);
    if (cert != null) {
      if (Options.check("multiline")) {
        sb.append(" (\n");
        sb.append(base64.formatString(cert, 64, "\t", true));
      } else {
        sb.append(" ");
        sb.append(base64.toString(cert));
      }
    }
    return sb.toString();
  }

  /** Returns the type of certificate */
  public int getCertType() {
    return certType;
  }

  /** Returns the ID of the associated KEYRecord, if present */
  public int getKeyTag() {
    return keyTag;
  }

  /** Returns the algorithm of the associated KEYRecord, if present */
  public int getAlgorithm() {
    return alg;
  }

  /** Returns the binary representation of the certificate */
  public byte[] getCert() {
    return cert;
  }

  @Override
  protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
    out.writeU16(certType);
    out.writeU16(keyTag);
    out.writeU8(alg);
    out.writeByteArray(cert);
  }
}

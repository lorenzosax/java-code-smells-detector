// SPDX-License-Identifier: BSD-2-Clause
// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base16;

/**
 * Transport Layer Security Authentication
 *
 * @author Brian Wellington
 * @see <a href="https://tools.ietf.org/html/rfc6698">RFC 6698: The DNS-Based Authentication of
 *     Named Entities (DANE) Transport Layer Security (TLS) Protocol: TLSA</a>
 */
public class TLSARecord extends Record {

  private int certificateUsage;
  private int selector;
  private int matchingType;
  private byte[] certificateAssociationData;

  TLSARecord() {}

  /**
   * Creates an TLSA Record from the given data
   *
   * @param certificateUsage The provided association that will be used to match the certificate
   *     presented in the TLS handshake.
   * @param selector The part of the TLS certificate presented by the server that will be matched
   *     against the association data.
   * @param matchingType How the certificate association is presented.
   * @param certificateAssociationData The "certificate association data" to be matched.
   */
  protected TLSARecord(
      Name name,
      int type,
      int dclass,
      long ttl,
      int certificateUsage,
      int selector,
      int matchingType,
      byte[] certificateAssociationData) {
    super(name, type, dclass, ttl);
    this.certificateUsage = checkU8("certificateUsage", certificateUsage);
    this.selector = checkU8("selector", selector);
    this.matchingType = checkU8("matchingType", matchingType);
  }

  /**
   * Creates an TLSA Record from the given data
   *
   * @param certificateUsage The provided association that will be used to match the certificate
   *     presented in the TLS handshake.
   * @param selector The part of the TLS certificate presented by the server that will be matched
   *     against the association data.
   * @param matchingType How the certificate association is presented.
   * @param certificateAssociationData The "certificate association data" to be matched.
   */
  public TLSARecord(
      Name name,
      int dclass,
      long ttl,
      int certificateUsage,
      int selector,
      int matchingType,
      byte[] certificateAssociationData) {
    this(
        name,
        Type.TLSA,
        dclass,
        ttl,
        certificateUsage,
        selector,
        matchingType,
        certificateAssociationData);
  }

  @Override
  protected void rrFromWire(DNSInput in) throws IOException {
    certificateUsage = in.readU8();
    selector = in.readU8();
    matchingType = in.readU8();
    certificateAssociationData = in.readByteArray();
  }

  @Override
  protected void rdataFromString(Tokenizer st, Name origin) throws IOException {
    certificateUsage = st.getUInt8();
    selector = st.getUInt8();
    matchingType = st.getUInt8();
    certificateAssociationData = st.getHex();
  }

  /** Converts rdata to a String */
  @Override
  protected String rrToString() {
    StringBuilder sb = new StringBuilder();
    sb.append(certificateUsage);
    sb.append(" ");
    sb.append(selector);
    sb.append(" ");
    sb.append(matchingType);
    sb.append(" ");
    sb.append(base16.toString(certificateAssociationData));

    return sb.toString();
  }

  @Override
  protected void rrToWire(DNSOutput out, Compression c, boolean canonical) {
    out.writeU8(certificateUsage);
    out.writeU8(selector);
    out.writeU8(matchingType);
    out.writeByteArray(certificateAssociationData);
  }

  /** Returns the certificate usage of the TLSA record */
  public int getCertificateUsage() {
    return certificateUsage;
  }

  /** Returns the selector of the TLSA record */
  public int getSelector() {
    return selector;
  }

  /** Returns the matching type of the TLSA record */
  public int getMatchingType() {
    return matchingType;
  }

  /** Returns the certificate associate data of this TLSA record */
  public final byte[] getCertificateAssociationData() {
    return certificateAssociationData;
  }
}

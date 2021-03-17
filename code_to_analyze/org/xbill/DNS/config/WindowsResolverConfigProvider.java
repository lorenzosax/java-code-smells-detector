// SPDX-License-Identifier: BSD-2-Clause
package org.xbill.DNS.config;

import org.xbill.DNS.config.IPHlpAPI.AF_UNSPEC;
import  org.xbill.DNS.config.IPHlpAPI.GAA_FLAG_SKIP_ANYCAST;
import  org.xbill.DNS.config.IPHlpAPI.GAA_FLAG_SKIP_FRIENDLY_NAME;
import  org.xbill.DNS.config.IPHlpAPI.GAA_FLAG_SKIP_MULTICAST;
import  org.xbill.DNS.config.IPHlpAPI.GAA_FLAG_SKIP_UNICAST;
import  org.xbill.DNS.config.IPHlpAPI.INSTANCE;
import  org.xbill.DNS.config.IPHlpAPI.IP_ADAPTER_ADDRESSES_LH;
import  org.xbill.DNS.config.IPHlpAPI.IP_ADAPTER_DNS_SERVER_ADDRESS_XP;
import  org.xbill.DNS.config.IPHlpAPI.IP_ADAPTER_DNS_SUFFIX;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.ptr.IntByReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.Name;
import org.xbill.DNS.SimpleResolver;

/**
 * Resolver config provider for Windows. It reads the nameservers and search path by calling the API
 * <a
 * href="https://docs.microsoft.com/en-us/windows/win32/api/iphlpapi/nf-iphlpapi-getadaptersaddresses">GetAdaptersAddresses</a>.
 * This class requires the <a href="https://github.com/java-native-access/jna">JNA library</a> on
 * the classpath.
 */
@Slf4j
public class WindowsResolverConfigProvider implements ResolverConfigProvider {
  private InnerWindowsResolverConfigProvider inner;

  public WindowsResolverConfigProvider() {
    if (System.getProperty("os.name").contains("Windows")) {
      try {
        inner = new InnerWindowsResolverConfigProvider();
      } catch (NoClassDefFoundError e) {
        log.debug("JNA not available");
      }
    }
  }


  @Override
  public void initialize() throws InitializationException {
    inner.initialize();
  }

  @Override
  public List<InetSocketAddress> servers() {
    return inner.servers();
  }

  @Override
  public List<Name> searchPaths() {
    return inner.searchPaths();
  }

  @Override
  public boolean isEnabled() {
    return inner != null;
  }
}

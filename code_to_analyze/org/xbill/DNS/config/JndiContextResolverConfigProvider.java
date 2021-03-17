// SPDX-License-Identifier: BSD-2-Clause
package org.xbill.DNS.config;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.Name;
import org.xbill.DNS.SimpleResolver;

/**
 * Resolver config provider that tries to extract the system's DNS servers from the <a
 * href="https://docs.oracle.com/javase/8/docs/technotes/guides/jndi/jndi-dns.html">JNDI DNS Service
 * Provider</a>.
 */
@Slf4j
public class JndiContextResolverConfigProvider implements ResolverConfigProvider {
  private InnerJndiContextResolverConfigProvider inner;

  public JndiContextResolverConfigProvider() {
    if (!System.getProperty("java.vendor").contains("Android")) {
      try {
        inner = new InnerJndiContextResolverConfigProvider();
      } catch (NoClassDefFoundError e) {
        log.debug("JNDI DNS not available");
      }
    }
  }

  @Override
  public void initialize() {
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

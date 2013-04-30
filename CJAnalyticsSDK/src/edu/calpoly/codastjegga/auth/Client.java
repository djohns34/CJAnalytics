package edu.calpoly.codastjegga.auth;

import java.net.URI;

public interface Client {
  public String getClientId();
  public URI getInstanceUrl();
  public URI getLoginUrl();
}

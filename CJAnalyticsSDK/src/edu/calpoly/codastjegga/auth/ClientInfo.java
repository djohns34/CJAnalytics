package edu.calpoly.codastjegga.auth;

import java.net.URI;
/*
 * All immutable information for an authenticated client 
 * (e.g. username, org ID, etc.).
 */
public class ClientInfo implements Tokenizable, Client {
  private final String clientId;
  private final URI instanceUrl;
  private final URI loginUrl;
  private Token mToken;

  public ClientInfo(String clientId, 
      URI instanceUrl, URI loginUrl, 
      String refreshToken){
    this.clientId = clientId;
    this.instanceUrl = instanceUrl;
    this.loginUrl = loginUrl;
    this.mToken = new Token("", refreshToken, this);

  }

  @Override
  public String getAccessToken() {
    return mToken.getAccessToken();
  }

  @Override
  public String getNewAccessToken() {
    return mToken.getNewAccessToken();
  }

  @Override
  public String getClientId() {
    return clientId;
  }

  @Override
  public URI getInstanceUrl() {
    return instanceUrl;
  }

  @Override
  public URI getLoginUrl() {
    return loginUrl;
  }
}


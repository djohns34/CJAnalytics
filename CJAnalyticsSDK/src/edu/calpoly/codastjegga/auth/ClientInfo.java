package edu.calpoly.codastjegga.auth;

import java.net.URI;

import android.app.Application;
/*
 * All immutable information for an authenticated client 
 * (e.g. username, org ID, etc.).
 */
final public class ClientInfo implements Tokenizable, Client {
  private final String clientId;
  private final URI instanceUrl;
  private final URI loginUrl;
  private Token mToken;

  @Deprecated 
  public ClientInfo(String clientId, URI instanceUrl, URI loginUrl){
    this.clientId = clientId;
    this.instanceUrl = instanceUrl;
    this.loginUrl = loginUrl;
    mToken = null;

  }

  public ClientInfo(Application app, String appName, String clientId, 
      URI instanceUrl, URI loginUrl, 
      String refreshToken){
    this.clientId = clientId;
    this.instanceUrl = instanceUrl;
    this.loginUrl = loginUrl;
    HttpAccess.init(app, "Android-App: " + appName);
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


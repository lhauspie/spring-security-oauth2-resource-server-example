package com.example.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Queries the /introspect endpoint to obtain the contents of an access token.
 * @author lhauspie
 */
public class KeycloakTokenServices implements ResourceServerTokenServices {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private RestOperations restTemplate;

  private String checkTokenEndpointUrl;

  private String clientId;

  private String clientSecret;

  private String tokenName = "token";

  private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

  public KeycloakTokenServices(String authServerUrl, String realm, String clientId, String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.checkTokenEndpointUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token/introspect";
    restTemplate = new RestTemplate();
    // it's possible to manage specific errors by overriding the handleError method
    ((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler());
  }

  public void setTokenName(String tokenName) {
    this.tokenName = tokenName;
  }

  public void setRestTemplate(RestOperations restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
    this.tokenConverter = accessTokenConverter;
  }

  @Override
  public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
    formData.add(tokenName, accessToken);
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
    Map<String, Object> map = postForMap(checkTokenEndpointUrl, formData, headers);

    if (map.containsKey("error")) {
      logger.debug("check_token returned error: " + map.get("error"));
      throw new InvalidTokenException(accessToken);
    }
    if (map.containsKey("active")) {
      if ((Boolean) map.get("active")) {
                 Assert.state(map.containsKey("client_id"), "Client id must be present in response from auth server");
      } else {
         logger.debug("the token is not active !");
         throw new InvalidTokenException("token is not active");
       }
     }

    OAuth2Authentication oAuth2Authentication = tokenConverter.extractAuthentication(map);
    logger.info("oAuth2Authentication is {}", oAuth2Authentication);
    oAuth2Authentication.setDetails(map);
    return oAuth2Authentication;
  }

  @Override
  public OAuth2AccessToken readAccessToken(String accessToken) {
    throw new UnsupportedOperationException("Not supported: read access token");
  }

   private String getAuthorizationHeader(String clientId, String clientSecret) {
     if (clientId == null || clientSecret == null) {
       logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
     }

    String credentials = String.format("%s:%s", clientId, clientSecret);
     try {
      return "Basic " + new String(Base64.encode(credentials.getBytes("UTF-8")));
     } catch (UnsupportedEncodingException e) {
       throw new IllegalStateException("Could not convert String");
     }
  }

  private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
     if (headers.getContentType() == null) {
       headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
     }
    return restTemplate.exchange(path, HttpMethod.POST, new HttpEntity(formData, headers), Map.class).getBody();
   }
 }

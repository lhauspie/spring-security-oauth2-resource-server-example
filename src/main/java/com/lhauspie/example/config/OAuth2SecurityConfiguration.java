package com.lhauspie.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

import java.util.Map;

@Configuration
@EnableResourceServer
@EnableAutoConfiguration
public class OAuth2SecurityConfiguration extends ResourceServerConfigurerAdapter {

  @Value("${security.oauth2.client.clientId}")
  private String clientId;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        // disable CSRF to avoid securing all endpoints
        .csrf().disable()

        .authorizeRequests()
        .antMatchers("/customers", "/customers/**").fullyAuthenticated()
        .antMatchers("/tests", "/tests/**").fullyAuthenticated();
  }

  @Autowired
  public void configure(RemoteTokenServices tokenServices) {
    DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter() {
      @Override
      public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication oAuth2Authentication = super.extractAuthentication(map);
        oAuth2Authentication.setDetails(map);
        return oAuth2Authentication;
      }
    };
    tokenServices.setAccessTokenConverter(accessTokenConverter);
  }


  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    // the resourceId is the 'aud' claim in access_token provided by the AuthorizationServer (Keycloak)
    resources.resourceId(clientId);
  }
}

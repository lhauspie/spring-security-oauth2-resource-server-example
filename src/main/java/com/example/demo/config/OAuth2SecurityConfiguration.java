package com.example.demo.config;

import com.example.demo.KeycloakTokenServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableResourceServer
@EnableAutoConfiguration
 public class OAuth2SecurityConfiguration extends ResourceServerConfigurerAdapter {

  @Value("${myKeycloak.auth-server-url}")
  private String authServerUrl;

  @Value("${myKeycloak.realm}")
  private String realm;

  @Value("${myKeycloak.client-id}")
  private String clientId;

  @Value("${myKeycloak.client-secret}")
  private String clientSecret;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable() // disable CSRF to avoid securing all endpoints
        .authorizeRequests()
        .antMatchers("/customers", "/customers/**").fullyAuthenticated()
        .antMatchers("/tests", "/tests/**").fullyAuthenticated();
  }

  @Bean
  public ResourceServerTokenServices tokenServices() {
    return new KeycloakTokenServices(authServerUrl, realm, clientId, clientSecret);
  }


  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    // the resourceId is the 'aud' claim in access_token provided by the AuthorizationServer (Keycloak)
    resources.resourceId(clientId);
    resources.tokenServices(tokenServices());
  }
}

package com.example.demo.config;

import com.example.demo.RemoteTokenServices;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class OAuth2SecurityConfiguration extends ResourceServerConfigurerAdapter {



  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable() // disable CSRF to avoid securing all endpoints
        .authorizeRequests()
        .antMatchers("/customers", "/customers/**").authenticated()
        .antMatchers("/tests", "/tests/**").authenticated();
/*
    http
        .csrf().disable()
//        .anonymous().disable()
        .requestMatcher(authorizationHeaderRequestMatcher())
        .authorizeRequests()
//        .antMatchers("/users", "/users/**").permitAll()
        .antMatchers("/customers", "/customers/**").authenticated()
        .antMatchers("/tests", "/tests/**").authenticated()
        .and()
        .authorizeRequests()
        .anyRequest().permitAll();
*/
  }

  @Bean
  @Qualifier("authorizationHeaderRequestMatcher")
  public RequestMatcher authorizationHeaderRequestMatcher() {
    return new RequestHeaderRequestMatcher("Authorization");
  }

//  RemoteTokenServices tokenServices;
//
//  @Autowired
//  @Qualifier("keycloakRemoteTokenServices")
//  public void setTokenServices(ResourceServerTokenServices tokenServices) {
//    this.tokenServices = (RemoteTokenServices) tokenServices;
//    this.tokenServices.setClientId("my-client");
//    this.tokenServices.setClientSecret("34486f3c-d419-4daf-9308-64fafa9e6dce");
//    this.tokenServices.setCheckTokenEndpointUrl("http://localhost:32768/auth/realms/demo/protocol/openid-connect/token/introspect");
//  }


  @Bean
  public ResourceServerTokenServices tokenServices() {
    RemoteTokenServices tokenServices = new RemoteTokenServices();
    tokenServices.setClientId("my-client");
    tokenServices.setClientSecret("34486f3c-d419-4daf-9308-64fafa9e6dce");
    tokenServices.setCheckTokenEndpointUrl("http://localhost:32768/auth/realms/demo/protocol/openid-connect/token/introspect");
    return tokenServices;
  }


  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    // this is the 'aud' claim in access_token provided by the AuthorizationServer (Keycloak)
    resources.resourceId("my-client");
    resources.tokenServices(tokenServices());
  }
}

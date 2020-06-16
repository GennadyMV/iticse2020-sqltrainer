package rage.sqltrainer.security;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableOAuth2Sso
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
@Order(1)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/", "/login**", "/logout**", "/webjars**", "/css**", "/api**", "/api/courses/**/users/current/progress").permitAll()
                .antMatchers("/practice**", "/review**", "/exercises**").authenticated()
                .antMatchers("/databases**", "/topics**").hasRole("ADMIN")
                .and()
                .oauth2Login()
                .clientRegistrationRepository(clientRegistrationRepository())
                .authorizedClientService(authorizedClientService());
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext, OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    // TODO: read these from configuration file
    @Bean
    @Profile("production")
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration productionClient = ClientRegistration.withRegistrationId("XXX")
                .clientName("XXX")
                .clientId("XXX")
                .clientSecret("XXX")
                .tokenUri("XXX")
                .authorizationUri("XXX")
                .userInfoUri("XXX")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("XXX")
                .build();

        return new InMemoryClientRegistrationRepository(productionClient);
    }

    @Bean(name = "clientRegistrationRepository")
    @Profile("default")
    public ClientRegistrationRepository defaultClientRegistrationRepository() {
        ClientRegistration defaultClient = ClientRegistration.withRegistrationId("XXX")
                .clientName("XXX")
                .clientId("XXX")
                .clientSecret("XXX")
                .tokenUri("XXX")
                .authorizationUri("XXX")
                .userInfoUri("XXX")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUriTemplate("http://localhost:8080")
                .build();

        return new InMemoryClientRegistrationRepository(defaultClient);
    }

    @Bean
    public OAuth2AuthorizedClientService authorizedClientService() {
        return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/api/courses/**/users/current/progress", config);
        return new CorsFilter(source);
    }
}

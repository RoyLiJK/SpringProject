package com.example;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@EnableZuulProxy
@EnableOAuth2Sso
@RestController
@SpringBootApplication
public class SpringProjectApplication extends WebSecurityConfigurerAdapter {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringProjectApplication.class, args);
	}

	@RequestMapping("/user")
	  public Principal user(Principal user) {
	    return user;
	  }
	
	
	 @Configuration
	 @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	 protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
			
		 @Override
			protected void configure(HttpSecurity http) throws Exception {
				http.authorizeRequests().antMatchers("/index.html", "/home.html", "/")
		          .permitAll().anyRequest().authenticated().and().csrf()
		          .csrfTokenRepository(csrfTokenRepository()).and()
		          .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		    }
	
	 private CsrfTokenRepository csrfTokenRepository() {
		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		  repository.setHeaderName("X-XSRF-TOKEN");
		  return repository;
		}
	 }
	 
	 @RequestMapping("/token")
	  @ResponseBody
	  public Map<String,String> token(HttpSession session) {
	    return Collections.singletonMap("token", session.getId());
	  }
	 
}

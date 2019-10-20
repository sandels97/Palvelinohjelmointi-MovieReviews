package com.santtuhyvarinen.moviereviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.santtuhyvarinen.moviereviews.web.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled= true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests()
			.antMatchers("/index")
			.permitAll()
			.anyRequest()
			.authenticated()
		.and()
			.formLogin()
			.permitAll()
			.defaultSuccessUrl("/index", true)
		.and()
			.logout()
			.permitAll()
		.and()
			.csrf()
			.disable();
	}
	
	@Autowired
	private UserDetailServiceImpl userDetailsService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
}

package com.maintainapps.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class AvailoffersSecurityConfig extends WebSecurityConfigurerAdapter {

	
	
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       
        http.authorizeRequests().antMatchers("/").permitAll()
        .antMatchers("/").hasAnyRole("DATAUSER", "ADMIN","DATAAGGREGATOR")
        .antMatchers("#!/custom/parser/*").hasAnyRole("DATAUSER", "ADMIN")
        .antMatchers("#!/custom/generator/").hasAnyRole("DATAAGGREGATOR", "ADMIN")
        .anyRequest().authenticated().and().formLogin().permitAll().and().logout().permitAll();
        
     

        http.csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        authenticationMgr.inMemoryAuthentication()
        .withUser("mustaq.ahmed@raziqtechnologies.com").password(encoder.encode("Sadad123")).authorities("ROLE_ADMIN").and()
        .withUser("fazal.basha@raziqtechnologies.com").password(encoder.encode("fazal@786")).authorities("ROLE_ADMIN").and()
        .withUser("raseeth.abubakker@raziqtechnologies.com").password(encoder.encode("Raziq@786")).authorities("ROLE_DATAAGGREGATOR");
    }

}

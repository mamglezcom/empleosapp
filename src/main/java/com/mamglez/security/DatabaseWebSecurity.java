package com.mamglez.security;



import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity {
	
//	@Bean
//	UserDetailsManager users(DataSource dataSource) {
//		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//		return users;
//		
//	}
	
	@Bean
	public UserDetailsManager usersCustom(DataSource dataSource) {
		JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
		users.setUsersByUsernameQuery("select username,password,estatus from Usuarios u where username=?");
		users.setAuthoritiesByUsernameQuery(
				"select u.username,p.perfil from UsuarioPerfil up " + 
				"inner join Usuarios u on u.id = up.idUsuario " +
				"inner join Perfiles p on p.id = up.idPerfil " + "where u.username=?");
		
		return users;
		
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize
				.antMatchers("/bootstrap/**","/images/**","/tinymce/**","/logos/**").permitAll()
				.antMatchers("/", "/signup", "/search", "/vacantes/view/**").permitAll()
				.antMatchers("/vacantes/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
				.antMatchers("/categorias/**").hasAnyAuthority("SUPERVISOR","ADMINISTRADOR")
				.antMatchers("/usuarios/**").hasAnyAuthority("ADMINISTRADOR")
				.anyRequest().authenticated());
		http.formLogin(form -> form.permitAll());
		return http.build();
	}

}

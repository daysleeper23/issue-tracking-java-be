package org.projectmanagement.presentation.config;

import org.projectmanagement.application.services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;
    private final CompanyFilter companyFilter;

    public SecurityConfig(UserDetailsServiceImpl udsi, JwtAuthFilter jwtAF, CompanyFilter cF) {
        this.userDetailsService = udsi;
        this.jwtAuthFilter = jwtAF;
        this.companyFilter = cF;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth ->
                auth
                    .requestMatchers("/auth/signup",
                            "/auth/login",
                            "/swagger-ui/**",
                            "/api-docs/**",
                            "/{companyId}/invitations/verify/**").permitAll()

                    /*
                        COMPANY PERMISSIONS
                     */
                    //Allow GET requests on /companies
                    .requestMatchers(HttpMethod.GET,"/{companyId}").hasAuthority("COMPANY_READ")

                    //Allow PUT requests on /companies
                    .requestMatchers(HttpMethod.PUT,"/{companyId}").hasAuthority("COMPANY_UPDATE")

                    //Allow GET requests on /{companyId}/companyManagers
                    .requestMatchers(HttpMethod.GET,"/{companyId}/companyManagers")
                        .hasAuthority("COMPANY_READ")

                    //Allow POST/PATCH/DELETE requests on /{companyId}/companyManagers
                    .requestMatchers(HttpMethod.POST,"/{companyId}/companyManagers")
                        .hasAuthority("COMPANY_UPDATE")

                    .requestMatchers(HttpMethod.PATCH,"/{companyId}/companyManagers/{id}")
                        .hasAuthority("COMPANY_UPDATE")

                    .requestMatchers(HttpMethod.DELETE,"/{companyId}/companyManagers/{id}")
                        .hasAuthority("COMPANY_UPDATE")

                    /*
                        WORKSPACE PERMISSIONS
                     */
                    //Allow GET requests on /{companyId}/workspaces
                    .requestMatchers(HttpMethod.GET,"/{companyId}/workspaces")
                        .hasAnyAuthority("WORKSPACE_READ_ALL", "WORKSPACE_READ_ONE")

                    //Allow POST requests on /{companyId}/workspaces
                    .requestMatchers(HttpMethod.POST,"/{companyId}/workspaces")
                        .hasAuthority("WORKSPACE_CREATE")

                    //Allow DELETE requests on /{companyId}/workspaces/{id}
                    .requestMatchers(HttpMethod.DELETE,"/{companyId}/workspaces/{id}")
                        .hasAnyAuthority("WORKSPACE_DELETE_ALL", "WORKSPACE_DELETE_ONE")

                    //Allow PATCH requests on /{companyId}/workspaces/{id}
                    .requestMatchers(HttpMethod.PATCH,"/{companyId}/workspaces/{id}")
                        .hasAnyAuthority("WORKSPACE_UPDATE_ALL", "WORKSPACE_UPDATE_ONE")

                    //Allow GET requests on /{companyId}/{workspaceId}/members/roles
                    .requestMatchers(HttpMethod.GET,"/{companyId}/{workspaceId}/members/roles")
                        .hasAnyAuthority("WORKSPACE_READ_ALL", "WORKSPACE_READ_ONE")

                    //Allow POST requests on /{companyId}/{workspaceId}/members/roles
                    .requestMatchers(HttpMethod.POST,"/{companyId}/{workspaceId}/members/roles")
                        .hasAnyAuthority("WORKSPACE_UPDATE_ALL", "WORKSPACE_UPDATE_ONE")

                    //Allow PUT requests on /{companyId}/{workspaceId}/members/roles/{id}
                    .requestMatchers(HttpMethod.PUT,"/{companyId}/{workspaceId}/members/roles/{id}")
                        .hasAnyAuthority("WORKSPACE_UPDATE_ALL", "WORKSPACE_UPDATE_ONE")

                    //Allow DELETE requests on /{companyId}/{workspaceId}/members/roles/{id}
                    .requestMatchers(HttpMethod.DELETE,"/{companyId}/{workspaceId}/members/roles/{id}")
                        .hasAnyAuthority("WORKSPACE_UPDATE_ALL", "WORKSPACE_UPDATE_ONE")

                    /*
                        PROJECT PERMISSIONS
                     */
                    //Allow GET requests on /{companyId}/{workspaceId}/projects
                    .requestMatchers(HttpMethod.GET,"/{companyId}/{workspaceId}/projects")
                        .hasAuthority("PROJECT_READ_ALL")

                        //TODO: Do we need this endpoint?
                    .requestMatchers(HttpMethod.GET,"/{companyId}/{workspaceId}/projects/{id}")
                        .hasAnyAuthority("PROJECT_READ_ALL", "PROJECT_READ_ONE")

                    //Allow POST requests on /{companyId}/{workspaceId}/projects
                    .requestMatchers(HttpMethod.POST,"/{companyId}/{workspaceId}/projects")
                        .hasAuthority("PROJECT_CREATE")

                    //Allow DELETE requests on /{companyId}/{workspaceId}/projects/{id}
                    .requestMatchers(HttpMethod.DELETE,"/{companyId}/{workspaceId}/projects/{id}")
                        .hasAnyAuthority("PROJECT_DELETE_ALL", "PROJECT_DELETE_ONE")

                    //Allow PATCH requests on /{companyId}/{workspaceId}/projects/{id}
                    .requestMatchers(HttpMethod.PATCH,"/{companyId}/{workspaceId}/projects/{id}")
                        .hasAnyAuthority("PROJECT_UPDATE_ALL", "PROJECT_UPDATE_ONE")

                    //Allow GET requests on /{projectId}/projectMembers
                    .requestMatchers(HttpMethod.GET,"/{projectId}/projectMembers")
                        .hasAnyAuthority("PROJECT_READ_ALL", "PROJECT_READ_ONE")

                    //Allow POST requests on /{projectId}/projectMembers
                    .requestMatchers(HttpMethod.POST,"/{projectId}/projectMembers")
                        .hasAnyAuthority("PROJECT_UPDATE_ALL", "PROJECT_UPDATE_ONE")

                    //Allow DELETE requests on /{projectId}/projectMembers/{id}
                    .requestMatchers(HttpMethod.DELETE,"/{projectId}/projectMembers/{id}")
                        .hasAnyAuthority("PROJECT_UPDATE_ALL", "PROJECT_UPDATE_ONE")

                    //Allow PATCH requests on /{projectId}/projectMembers/{id}
                    .requestMatchers(HttpMethod.PATCH,"/{projectId}/projectMembers/{id}")
                        .hasAnyAuthority("PROJECT_UPDATE_ALL", "PROJECT_UPDATE_ONE")

                    /*
                        ROLE PERMISSIONS
                    */
                    //Allow GET requests on /roles
                    .requestMatchers(HttpMethod.GET,"/{companyId}/roles").hasAuthority("ROLE_READ_ALL")

                    //Allow POST requests on /roles
                    .requestMatchers(HttpMethod.POST,"/{companyId}/roles").hasAuthority("ROLE_CREATE")

                    //Allow DELETE requests on /roles
                    .requestMatchers(HttpMethod.DELETE,"/{companyId}/roles").hasAuthority("ROLE_DELETE_ALL")

                    //Allow PUT requests on /roles
                    .requestMatchers(HttpMethod.PUT,"/{companyId}/roles").hasAuthority("ROLE_UPDATE_ALL")

                        //TODO: Missing endpoints for updating permissions for a custom roles?
                    //Allow GET requests on /rolesPermissions
                    .requestMatchers(HttpMethod.GET,"/rolesPermissions").hasAuthority("ROLE_READ_ALL")

                    /* Allow Invitations endpoints /{companyId}/invitations/**  */
                    .requestMatchers(HttpMethod.POST,"/{companyId}/invitations/").hasAuthority("COMPANY_UPDATE")
                    .requestMatchers(HttpMethod.GET,"/{companyId}/invitations/").hasAuthority("COMPANY_UPDATE")
                    .requestMatchers(HttpMethod.PUT,"/{companyId}/invitations/{invitationId}").hasAuthority("COMPANY_UPDATE")
                    .requestMatchers(HttpMethod.DELETE,"/{companyId}/invitations/{invitationId}").hasAuthority("COMPANY_UPDATE")
                        //TODO: add authority for when tasks permissions are added
                    .requestMatchers("/{companyId}/task/**").authenticated()
                    //Allow POST requests on /companies
                    .requestMatchers(HttpMethod.POST,"/companies").authenticated()

//                    .requestMatchers("/**").permitAll()
                    .anyRequest().authenticated()

            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(companyFilter, JwtAuthFilter.class)
            .authenticationManager(authenticationManager(http));
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder amb = http.getSharedObject(AuthenticationManagerBuilder.class);
        amb.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return amb.build();
    }
}

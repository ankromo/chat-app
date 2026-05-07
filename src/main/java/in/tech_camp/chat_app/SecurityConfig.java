package in.tech_camp.chat_app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    http
    .csrf(AbstractHttpConfigurer::disable)
    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
    //ログアウト状態でも実行できるGETリクエスト記述
    .requestMatchers("/css/**","/users/sign_up","/users/login").permitAll()
    //ログアウトでも実行できるPOST
   
   .requestMatchers(HttpMethod.POST, "/user").permitAll()
    .anyRequest().authenticated())

    .formLogin(login -> login
      //ログインフォームのボタンをクリック時送信するパス
      .loginProcessingUrl("/login")
      //ログインページのパスを設定
      .loginPage("/users/login")
      //ログイン成功後のリダイレクト先
      .defaultSuccessUrl("/",true)
      //ログイン失敗後のリダイレクト先
      .failureUrl("/login?error")
      .usernameParameter("email")
      .permitAll())

      .logout(logout -> logout
      .logoutUrl("/logout")

      .logoutSuccessUrl("/users/login"));

        return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
  
}

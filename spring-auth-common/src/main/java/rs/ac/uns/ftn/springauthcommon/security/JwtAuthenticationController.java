package rs.ac.uns.ftn.springauthcommon.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JwtAuthenticationController {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenUtil jwtTokenUtil;

  private final JwtUserDetailsService userDetailsService;

  @PostMapping(value = "/login")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
    authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
    JwtUser userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
    String token = jwtTokenUtil.generateToken(userDetails);

    log.info("Successfully generated token for '{}'", authenticationRequest.getEmail());
    return ResponseEntity.ok(new JwtResponse(token));
  }

  private void authenticate(String email, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    } catch (Exception e) {
      throw new BadCredentialsException("Wrong email and password combination.");
    }
  }
}

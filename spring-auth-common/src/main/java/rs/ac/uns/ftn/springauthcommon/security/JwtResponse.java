package rs.ac.uns.ftn.springauthcommon.security;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse implements Serializable {

  private String token;
}

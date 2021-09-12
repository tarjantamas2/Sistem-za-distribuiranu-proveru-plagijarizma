package rs.ac.uns.ftn.springauthcommon.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.springauthcommon.entity.User;
import rs.ac.uns.ftn.springauthcommon.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public JwtUser loadUserByUsername(String email) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByEmail(email);

    if (!optionalUser.isPresent()) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    } else {
      User user = optionalUser.get();
      List<GrantedAuthority> grantedAuthorities =
          user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());

      return new JwtUser(email, user.getPassword(), true, true, true, true, grantedAuthorities, user.getId());
    }
  }
}

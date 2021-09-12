package rs.ac.uns.ftn.springauthcommon.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.springauthcommon.entity.Roles;
import rs.ac.uns.ftn.springauthcommon.entity.User;
import rs.ac.uns.ftn.springauthcommon.repository.UserRepository;
import rs.ac.uns.ftn.springauthcommon.security.JwtUser;
import rs.ac.uns.ftn.springcommonrestexceptions.exception.ForbiddenException;
import rs.ac.uns.ftn.springcommonrestexceptions.exception.InternalServerException;
import rs.ac.uns.ftn.springcommonrestexceptions.exception.NotLoggedInException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public User findMyInfo() {
    return getActiveUserOrFail();
  }

  public User getActiveUserOrFail() {
    String email = getActiveUserEmail();
    Optional<User> oUser = userRepository.findByEmail(email);

    if (!oUser.isPresent()) {
      log.info("User with email '{}' not found", email);
      throw new InternalServerException("User from token not found!");
    }
    return oUser.get();
  }

  private String getActiveUserEmail() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof UsernamePasswordAuthenticationToken)) {
      throw new NotLoggedInException();
    }

    UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
    if (auth.getPrincipal() == null) {
      throw new NotLoggedInException();
    }

    return ((JwtUser) auth.getPrincipal()).getUsername();
  }

  public User register(User user) {
    verifyUserDoesNotExist(user);
    user.setRoles(Collections.singleton(Roles.USER.getRole()));
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    user.setPassword(null);
    return user;
  }

  private void verifyUserDoesNotExist(User user) {
    userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
      throw new ForbiddenException("User with the given email already exists.");
    });
  }
}

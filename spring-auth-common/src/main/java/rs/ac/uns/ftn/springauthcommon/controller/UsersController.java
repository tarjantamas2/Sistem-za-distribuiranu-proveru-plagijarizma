package rs.ac.uns.ftn.springauthcommon.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.springauthcommon.entity.User;
import rs.ac.uns.ftn.springauthcommon.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UsersController {

  private final UserService userService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<User> register(@RequestBody User user) {
    return ResponseEntity.ok(userService.register(user));
  }
}

package rs.ac.uns.ftn.springauthcommon.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.springauthcommon.entity.User;
import rs.ac.uns.ftn.springauthcommon.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;

  @GetMapping(path = "/me", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<User> findMyInfo() {
    return ResponseEntity.ok(userService.findMyInfo());
  }
}

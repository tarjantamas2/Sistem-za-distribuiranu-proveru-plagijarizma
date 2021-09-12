package rs.ac.uns.ftn.frontend.config;

import java.lang.reflect.AnnotatedElement;
import java.security.Principal;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import com.vaadin.flow.spring.annotation.UIScope;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedRestClient;

@UIScope
@Component
@RequiredArgsConstructor
public class AccessAnnotationCheckerImpl extends AccessAnnotationChecker {

  private final SharedRestClient sharedRestClient;

  @Override
  public AnnotatedElement getSecurityTarget(Class<?> cls) {
    return super.getSecurityTarget(cls);
  }

  @Override
  public boolean hasAccess(Class<?> cls) {
    return checkAccess(cls);
  }

  @Override
  public boolean hasAccess(Class<?> cls, HttpServletRequest request) {
    return checkAccess(cls);
  }

  @Override
  public boolean hasAccess(Class<?> cls, Principal principal, Function<String, Boolean> roleChecker) {
    return checkAccess(cls);
  }

  private boolean checkAccess(Class<?> cls) {
    if (sharedRestClient.getLoggedInUser().isPresent()) {
      return !cls.isAnnotationPresent(OnlyLoggedOutAllowed.class);
    }
    return !cls.isAnnotationPresent(LoggedInAllowed.class);
  }
}

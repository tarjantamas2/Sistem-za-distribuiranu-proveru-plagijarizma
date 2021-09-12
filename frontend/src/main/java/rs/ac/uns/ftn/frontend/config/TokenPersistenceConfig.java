package rs.ac.uns.ftn.frontend.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.flow.component.UI;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.TokenPersistenceHandler;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TokenPersistenceConfig {

  @Bean
  public TokenPersistenceHandler tokenPersistenceHandler() {
    return new TokenPersistenceHandler() {

      private static final String KEY = "storage_key_1";

      private final Map<String, String> sessionIdToToken = new HashMap<>();

      private String get() {
        return sessionIdToToken.get(UI.getCurrent().getSession().getSession().getId());
      }

      private void set(String token) {
        sessionIdToToken.put(UI.getCurrent().getSession().getSession().getId(), token);
      }

      @Override
      public void saveToken(String token) {
        if (token == null) {
          set(null);
          UI.getCurrent()
            .accessSynchronously(() -> UI.getCurrent().getPage().executeJs("localStorage.removeItem($0);", KEY));
        } else {
          set(token);
          UI.getCurrent()
            .accessSynchronously(
                () -> UI.getCurrent().getPage().executeJs("localStorage.setItem($0, $1);", KEY, token));
        }
      }

      @Override
      public String getToken() {
        if (get() == null) {
          UI.getCurrent()
            .accessSynchronously(() -> UI.getCurrent()
              .getPage()
              .executeJs("return localStorage.getItem($0);", KEY)
              .then(String.class, this::set));
        }

        return get();
      }
    };
  }
}
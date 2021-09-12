package rs.ac.uns.ftn.springauthcommon.entity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

  @Id
  private String id;

  private String email;

  private String password;

  private String firstName;

  private String lastName;

  private Set<Role> roles;

  public boolean hasAnyRole(Role... roles) {
    return CollectionUtils.isNotEmpty(Sets.intersection(this.roles, Arrays.stream(roles).collect(Collectors.toSet())));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

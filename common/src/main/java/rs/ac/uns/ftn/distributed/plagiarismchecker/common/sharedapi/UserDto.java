package rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String email;

  private String password;
}

package rs.ac.uns.ftn.springauthcommon.entity;

public enum Roles {

  USER("USER"),
  ADMIN("ADMIN");

  private final Role role;

  Roles(String name) {
    Role role = new Role();
    role.setName(name);
    this.role = role;
  }

  public Role getRole() {
    return this.role;
  }

  public String getName() {
    return role.getName();
  }
}

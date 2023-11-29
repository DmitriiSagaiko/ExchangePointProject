package exception;

public class PasswordValidator {

  public static void validate(String password) throws PasswordValidateExcepton {
    if (password.length() < 8) throw new PasswordValidateExcepton("Length for pass < 8");


    boolean[] res = new boolean[4];
    for (int i = 0; i < password.length(); i++) {
      char c = password.charAt(i);

      if (Character.isDigit(c)) {
        res[0] = true;
      }

      if (Character.isLowerCase(c)) {
        res[1] = true;
      }

      if (Character.isUpperCase(c)) {
        res[2] = true;
      }

      if ("!%$@&".indexOf(c) >= 0) {
        res[3] = true;
      }
    }

    if (!res[0]) {
      throw new PasswordValidateExcepton("no digit");
    } else if (!res[1]) {
      throw new PasswordValidateExcepton("no lower case");
    } else if (!res[2]) {
      throw new PasswordValidateExcepton("no upper case");
    } else if (!res[3]) {
      throw new PasswordValidateExcepton("use special symbols");
    }

  }

}

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Program {
  public static void main(String[] args) throws LoginException {
    JDA jda = JDABuilder.createDefault("NzU3MzIyMDQ3NjAzODY3Nzc4.X2etHg.ka3Su-4gIX_mzWlTfu63xiTUago").build();

    jda.addEventListener();
  }
}

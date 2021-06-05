  import com.mongodb.MongoClient;
  import com.mongodb.MongoClientURI;
  import net.dv8tion.jda.api.JDA;
  import net.dv8tion.jda.api.JDABuilder;
  import xyz.radiish.gaming.chess.standard.StandardBoard;
import xyz.radiish.gaming.chess.state.GameState;
  import xyz.radiish.yuggsbot.YuggsBot;
  import xyz.radiish.zephyr.Zephyr;
  import xyz.radiish.zephyr.cereal.JsonSerializing;

  import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.*;

public class Program {
  public static void main(String[] args) throws Exception {
    if(args.length != 2) {
      throw new IllegalArgumentException("not enough arguments, idot");
    }

    JsonSerializing.initialize();

    JDA jda = JDABuilder.createDefault(args[0]).build();

    MongoClient mongo = new MongoClient(new MongoClientURI(args[1]));

    Zephyr client = new YuggsBot(jda, mongo.getDB("yuggs"));
  }
}

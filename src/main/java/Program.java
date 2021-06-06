import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import xyz.radiish.yuggsbot.Yuggs;
import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.cereal.JsonSerializing;

public class Program {
  public static void main(String[] args) throws Exception {
    if(args.length != 2) {
      throw new IllegalArgumentException("not enough arguments, idot");
    }

    JsonSerializing.initialize();

    JDA jda = JDABuilder.createDefault(args[0]).build();

    MongoClient mongo = new MongoClient(new MongoClientURI(args[1]));

    Zephyr client = new Yuggs(jda, mongo.getDB("yuggs"));
  }
}

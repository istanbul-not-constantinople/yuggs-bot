package xyz.radiish.hycord;

import com.mojang.brigadier.tree.RootCommandNode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.radiish.zephyr.Zephyr;
import xyz.radiish.zephyr.ZephyrCommandSource;

public class Hycord extends Zephyr {

  protected Hycord(JDA jda) {
    super(jda);
  }
}

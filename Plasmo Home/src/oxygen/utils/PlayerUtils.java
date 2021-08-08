package oxygen.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerUtils {

	public static TextComponent getHoverableComponent(String text, String hoverText) {
		TextComponent hoverableComponent = new TextComponent(ChatColor.translateAlternateColorCodes('&', text));
		hoverableComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hoverText)).create()));
		return hoverableComponent;
	}

	public static void sendHoverableMessage(CommandSender sender, String msg, String hoverMsg) {
		TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', msg));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hoverMsg)).create()));

		TextComponent extraMessage = new TextComponent("§4§ltest");
		extraMessage
				.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§1§ltest").create()));
		message.addExtra(extraMessage);
		sender.spigot().sendMessage(message);
	}

	public static void sendTextComponents(Player player, TextComponent... components) {

		TextComponent finalMessage = new TextComponent();

		for (TextComponent component : components) {
			finalMessage.addExtra(component);
		}

		player.spigot().sendMessage(finalMessage);

	}

}

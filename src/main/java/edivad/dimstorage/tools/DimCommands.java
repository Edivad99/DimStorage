package edivad.dimstorage.tools;

import java.util.ArrayList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import edivad.dimstorage.DimStorage;
import edivad.dimstorage.setup.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public class DimCommands {

  public static LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("dimtablet");

  public static void init(CommandDispatcher<CommandSourceStack> dispatcher) {
    root.then(Commands.literal("add").requires(cs -> cs.hasPermission(0)).executes(context -> {
      var player = context.getSource().getPlayerOrException();
      Item item = player.getMainHandItem().getItem();

      if (Config.DimTablet.addItem(item)) {
        player.displayClientMessage(
            Component.literal("Item added to the list").withStyle(ChatFormatting.GREEN), false);
      } else {
        player.displayClientMessage(
            Component.literal("You must select a valid item").withStyle(ChatFormatting.RED), false);
      }
      return 0;
    }));

    root.then(Commands.literal("remove").requires(cs -> cs.hasPermission(0)).executes(context -> {
      var player = context.getSource().getPlayerOrException();
      Item item = player.getMainHandItem().getItem();

      if (Config.DimTablet.removeItem(item)) {
        player.displayClientMessage(
            Component.literal("Item removed from the list").withStyle(ChatFormatting.GREEN), false);
      } else {
        player.displayClientMessage(
            Component.literal("You must select a valid item").withStyle(ChatFormatting.RED), false);
      }
      return 0;
    }));

    root.then(
        Commands.literal("removeAll").requires(cs -> cs.hasPermission(0)).executes(context -> {
          Config.DimTablet.ALLOW_LIST.set(new ArrayList<>());
          var player = context.getSource().getPlayerOrException();
          player.displayClientMessage(
              Component.literal("Removed all items from the list").withStyle(ChatFormatting.GREEN),
              false);
          return 0;
        }));

    root.then(Commands.literal("list").requires(cs -> cs.hasPermission(0)).executes(context -> {
      var player = context.getSource().getPlayerOrException();
      player.displayClientMessage(
          Component.literal("These are the items that the DimTablet will move:"), false);
      Config.DimTablet.ALLOW_LIST.get()
          .stream()
          .map(Component::literal)
          .forEach(item -> player.displayClientMessage(item, false));
      return 0;
    }));

    LiteralCommandNode<CommandSourceStack> rootNode = dispatcher.register(root);
    dispatcher.register(Commands.literal(DimStorage.ID).redirect(rootNode));
  }
}
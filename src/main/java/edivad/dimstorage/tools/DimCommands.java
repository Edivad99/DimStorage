package edivad.dimstorage.tools;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;

import edivad.dimstorage.Main;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class DimCommands {

	public static LiteralArgumentBuilder<CommandSource> root = Commands.literal("dimtablet");

	public static void init(CommandDispatcher<CommandSource> dispatcher)
	{
		root.then(Commands.literal("add").requires(cs -> cs.hasPermissionLevel(0)).executes(context -> {
			Item item = context.getSource().asPlayer().getHeldItemMainhand().getItem();
			if(!item.equals(Items.AIR))
			{
				String itemNameSpace = item.getRegistryName().toString();
				List<String> originalList = Config.DIMTABLET_LIST.get();
				originalList.add(itemNameSpace);

				Config.DIMTABLET_LIST.set(originalList);

				context.getSource().asPlayer().sendMessage(new StringTextComponent(TextFormatting.GREEN + "Added " + itemNameSpace + " to the list"));
			}
			else
				context.getSource().asPlayer().sendMessage(new StringTextComponent(TextFormatting.RED + "You must select a valid item"));
			return 0;
		}));

		root.then(Commands.literal("remove").requires(cs -> cs.hasPermissionLevel(0)).executes(context -> {
			Item item = context.getSource().asPlayer().getHeldItemMainhand().getItem();
			if(!item.equals(Items.AIR))
			{
				String itemNameSpace = item.getRegistryName().toString();
				List<String> originalList = Config.DIMTABLET_LIST.get();
				originalList.remove(itemNameSpace);

				Config.DIMTABLET_LIST.set(originalList);

				context.getSource().asPlayer().sendMessage(new StringTextComponent(TextFormatting.GREEN + "Removed " + itemNameSpace + " to the list"));
			}
			else
				context.getSource().asPlayer().sendMessage(new StringTextComponent(TextFormatting.RED + "You must select a valid item"));
			return 0;
		}));

		root.then(Commands.literal("list").requires(cs -> cs.hasPermissionLevel(0)).executes(context -> {
			context.getSource().asPlayer().sendMessage(new StringTextComponent("These are the items that the DimTablet will move"));
			for(String items : Config.DIMTABLET_LIST.get())
				context.getSource().asPlayer().sendMessage(new StringTextComponent(items));
			return 0;
		}));

		LiteralCommandNode<CommandSource> rootNode = dispatcher.register(root);
		dispatcher.register(Commands.literal(Main.MODID).redirect(rootNode));
	}
}
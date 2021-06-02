package edivad.dimstorage.tools;

import java.util.ArrayList;
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
        root.then(Commands.literal("add").requires(cs -> cs.hasPermission(0)).executes(context -> {
            Item item = context.getSource().getPlayerOrException().getMainHandItem().getItem();
            if(!item.equals(Items.AIR))
            {
                String itemNameSpace = item.getRegistryName().toString();
                List<String> originalList = Config.DIMTABLET_LIST.get();
                if(!originalList.contains(itemNameSpace))
                {
                    originalList.add(itemNameSpace);
                    Config.DIMTABLET_LIST.set(originalList);
                }

                context.getSource().getPlayerOrException().displayClientMessage(new StringTextComponent(TextFormatting.GREEN + "Added " + itemNameSpace + " to the list"), false);
            }
            else
                context.getSource().getPlayerOrException().displayClientMessage(new StringTextComponent(TextFormatting.RED + "You must select a valid item"), false);
            return 0;
        }));

        root.then(Commands.literal("remove").requires(cs -> cs.hasPermission(0)).executes(context -> {
            Item item = context.getSource().getPlayerOrException().getMainHandItem().getItem();
            if(!item.equals(Items.AIR))
            {
                String itemNameSpace = item.getRegistryName().toString();
                List<String> originalList = Config.DIMTABLET_LIST.get();
                originalList.remove(itemNameSpace);

                Config.DIMTABLET_LIST.set(originalList);

                context.getSource().getPlayerOrException().displayClientMessage(new StringTextComponent(TextFormatting.GREEN + "Removed " + itemNameSpace + " to the list"), false);
            }
            else
                context.getSource().getPlayerOrException().displayClientMessage(new StringTextComponent(TextFormatting.RED + "You must select a valid item"), false);
            return 0;
        }));

        root.then(Commands.literal("removeAll").requires(cs -> cs.hasPermission(0)).executes(context -> {
            Config.DIMTABLET_LIST.set(new ArrayList<String>());
            context.getSource().getPlayerOrException().displayClientMessage(new StringTextComponent(TextFormatting.GREEN + "Removed all items from the list"), false);
            return 0;
        }));

        root.then(Commands.literal("list").requires(cs -> cs.hasPermission(0)).executes(context -> {
            context.getSource().getPlayerOrException().displayClientMessage(new StringTextComponent("These are the items that the DimTablet will move"), false);
            for(String items : Config.DIMTABLET_LIST.get())
                context.getSource().getPlayerOrException().displayClientMessage(new StringTextComponent(items), false);
            return 0;
        }));

        LiteralCommandNode<CommandSource> rootNode = dispatcher.register(root);
        dispatcher.register(Commands.literal(Main.MODID).redirect(rootNode));
    }
}
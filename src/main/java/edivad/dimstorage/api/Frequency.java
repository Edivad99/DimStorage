package edivad.dimstorage.api;

import java.util.Optional;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import edivad.dimstorage.tools.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record Frequency(Optional<GameProfile> gameProfile, int channel) implements TooltipProvider {

  public static final Codec<Frequency> CODEC =
      RecordCodecBuilder.create(instance -> instance.group(
          ExtraCodecs.GAME_PROFILE.optionalFieldOf("gameProfile").forGetter(Frequency::gameProfile),
          Codec.INT.fieldOf("channel").forGetter(Frequency::channel)
      ).apply(instance, Frequency::new)
  );

  public static final StreamCodec<RegistryFriendlyByteBuf, Frequency> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.optional(ByteBufCodecs.GAME_PROFILE), frequency -> frequency.gameProfile,
          ByteBufCodecs.INT, frequency -> frequency.channel,
          Frequency::new);

  public Frequency() {
    this(1);
  }

  public Frequency(int channel) {
    this((Player) null, channel);
  }

  public Frequency(@Nullable Player player, int channel) {
    this(Optional.ofNullable(player).map(Player::getGameProfile), channel);
  }

  public Frequency setPublic() {
    return new Frequency(Optional.empty(), this.channel);
  }

  public Frequency setOwner(Player player) {
    return new Frequency(player, this.channel);
  }

  public Frequency setChannel(int channel) {
    return new Frequency(this.gameProfile, channel);
  }

  public boolean hasOwner() {
    return this.gameProfile.isPresent();
  }

  public boolean canAccess(Player player) {
    return this.gameProfile.map(profile -> profile.getId().equals(player.getGameProfile().getId())).orElse(true);
  }

  public String getOwner() {
    return this.gameProfile().map(GameProfile::getName).orElse("public");
  }

  @Override
  public String toString() {
    return "gameProfile=" + (this.hasOwner() ? this.gameProfile.get().getId() : "public") + ",channel=" + this.channel;
  }

  @Override
  public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder,
      TooltipFlag flag, DataComponentGetter componentGetter) {
    if (this.hasOwner()) {
      tooltipAdder.accept(Component.translatable(Translations.OWNER).append(" " + this.getOwner())
          .withStyle(ChatFormatting.DARK_RED));
    }
    tooltipAdder.accept(Component.translatable(Translations.FREQUENCY)
        .append(" " + this.channel()));
  }
}

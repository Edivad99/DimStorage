package edivad.dimstorage.blockentity.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class DimChestRenderState extends BlockEntityRenderState {

  public boolean locked;
  public boolean hasOwner;
  public float movablePartState;
  public int rotation;
}

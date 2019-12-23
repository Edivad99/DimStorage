package edivad.dimstorage.compat.opencomputers;

import edivad.dimstorage.tile.TileFrequencyOwner;
import li.cil.oc.api.Network;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.network.Visibility;
import li.cil.oc.api.prefab.AbstractManagedEnvironment;

public class AbstractDimStorageEnviroment<T extends TileFrequencyOwner> extends AbstractManagedEnvironment implements NamedBlock {

	protected T tile;
	protected String name;

	public AbstractDimStorageEnviroment(String name, T tile)
	{
		this.name = name;
		this.tile = tile;

		setNode(Network.newNode(this, Visibility.Network).withComponent(name, Visibility.Network).create());
	}

	@Override
	public String preferredName()
	{
		return name;
	}

	@Override
	public int priority()
	{
		return 5;
	}
}
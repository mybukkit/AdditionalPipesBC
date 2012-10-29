/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package buildcraft.additionalpipes.pipes;

import java.util.LinkedList;

import net.minecraft.src.IInventory;
import net.minecraft.src.TileEntity;
import buildcraft.additionalpipes.AdditionalPipes;
import buildcraft.api.core.Orientations;
import buildcraft.api.core.Position;
import buildcraft.api.transport.IPipedItem;
import buildcraft.core.inventory.TransactorSimple;
import buildcraft.core.utils.Utils;
import buildcraft.transport.IPipeTransportItemsHook;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.pipes.PipeLogicStone;

public class PipeItemsAdvancedInsertion extends Pipe implements IPipeTransportItemsHook {

	public PipeItemsAdvancedInsertion(int itemID) {
		super(new PipeTransportItems(), new PipeLogicStone(), itemID);
	}

	@Override
	public LinkedList<Orientations> filterPossibleMovements(LinkedList<Orientations> possibleOrientations, Position pos, IPipedItem item) {
		return filterPossibleMovements(possibleOrientations, pos, item, 0);
	}


	public LinkedList<Orientations> filterPossibleMovements(LinkedList<Orientations> possibleOrientations, Position pos, IPipedItem item, int Level) {
		LinkedList<Orientations> newOris = new LinkedList<Orientations>();

		for (int o = 0; o < 6; ++o) {
			if (Orientations.values()[o] != pos.orientation.reverse()) {
				Position newPos = new Position(pos);
				newPos.orientation = Orientations.values()[o];
				newPos.moveForwards(1.0);

				TileEntity entity = worldObj.getBlockTileEntity((int) newPos.x, (int) newPos.y, (int) newPos.z);

				if (entity instanceof IInventory) {
					TransactorSimple transactor = new TransactorSimple((IInventory) entity);
					if (transactor.add(item.getItemStack(), newPos.orientation.reverse(), false).stackSize > 0) {
						newOris.add(newPos.orientation);
					}
				}
			}
		}

		//System.out.println("NewOris Size: " + newOris.size() + " - PO Size: " + possibleOrientations.size() + " - Level: " + Level);
		if (newOris.size() > 0) {
			return newOris;
		}

		return possibleOrientations;
	}

	@Override
	public void readjustSpeed(IPipedItem item) {
		if (item.getSpeed() > Utils.pipeNormalSpeed) {
			item.setSpeed(item.getSpeed() - Utils.pipeNormalSpeed / 2.0F);
		}
		if (item.getSpeed() < Utils.pipeNormalSpeed) {
			item.setSpeed(Utils.pipeNormalSpeed);
		}
	}

	@Override
	public String getTextureFile() {
		return AdditionalPipes.TEXTURE_PIPES;
	}

	@Override
	public int getTextureIndex(Orientations direction) {
		return 8;
	}

	@Override
	public void entityEntered(IPipedItem item, Orientations orientation) {
	}

}
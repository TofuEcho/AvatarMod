/* 
  This file is part of AvatarMod.
    
  AvatarMod is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  AvatarMod is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with AvatarMod. If not, see <http://www.gnu.org/licenses/>.
*/

package com.crowsofwar.avatar.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.FMLCommonHandler;

import com.crowsofwar.avatar.common.AvatarCommonProxy;
import com.crowsofwar.avatar.common.controls.*;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.gui.AvatarGui;
import com.crowsofwar.gorecore.data.PlayerDataFetcher;

public class AvatarServerProxy implements AvatarCommonProxy {

	private AvatarKeybindingServer keys;

	@Override
	public void preInit() {
		keys = new AvatarKeybindingServer();
	}

	@Override
	public IControlsHandler getKeyHandler() {
		return keys;
	}

	@Override
	public double getPlayerReach() {
		return 0;
	}

	@Override
	public void init() {

	}

	@Override
	public AvatarGui createClientGui(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public PlayerDataFetcher<AvatarPlayerData> getClientDataFetcher() {
		return null;
	}

	@Override
	public IThreadListener getThreadListener() {
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}

	@Override
	public int getParticleAmount() {
		return 0;
	}

	@Override
	public KeybindingWrapper createKeybindWrapper(String keybindName) {
		return new KeybindingWrapper();
	}

	@Override
	public void registerItemModels() {
	}

	@Override
	public boolean isOptifinePresent() {
		return false;
	}

}

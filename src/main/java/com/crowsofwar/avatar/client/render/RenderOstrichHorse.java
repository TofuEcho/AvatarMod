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
package com.crowsofwar.avatar.client.render;

import com.crowsofwar.avatar.client.render.ostrich.ModelOstrichHorseWild;
import com.crowsofwar.avatar.common.entity.mob.EntityOstrichHorse;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class RenderOstrichHorse extends RenderLiving<EntityOstrichHorse> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation("avatarmod",
			"textures/mob/ostrich.png");
	
	public RenderOstrichHorse(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelOstrichHorseWild(), 0.5f);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityOstrichHorse entity) {
		return TEXTURE;
	}
	
}
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

package com.crowsofwar.avatar.common.entity.data;

import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.bending.BattlePerformanceScore;
import com.crowsofwar.avatar.common.bending.fire.AbilityFireball;
import com.crowsofwar.avatar.common.data.AbilityData.AbilityTreePath;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.entity.EntityFireball;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

/**
 * @author CrowsOfWar
 */
public abstract class FireballBehavior extends Behavior<EntityFireball> {

	public static final DataSerializer<FireballBehavior> DATA_SERIALIZER = new Behavior.BehaviorSerializer<>();

	public static int ID_NOTHING, ID_FALL, ID_PICKUP, ID_PLAYER_CONTROL, ID_THROWN;

	public static void register() {
		DataSerializers.registerSerializer(DATA_SERIALIZER);
		ID_NOTHING = registerBehavior(Idle.class);
		ID_PLAYER_CONTROL = registerBehavior(PlayerControlled.class);
		ID_THROWN = registerBehavior(Thrown.class);
	}

	public static class Idle extends FireballBehavior {

		@Override
		public FireballBehavior onUpdate(EntityFireball entity) {
			return this;
		}

		@Override
		public void fromBytes(PacketBuffer buf) {
		}

		@Override
		public void toBytes(PacketBuffer buf) {
		}

		@Override
		public void load(NBTTagCompound nbt) {
		}

		@Override
		public void save(NBTTagCompound nbt) {
		}

	}

	public static class Thrown extends FireballBehavior {

		int time = 0;

		@Override
		public FireballBehavior onUpdate(EntityFireball entity) {

			time++;

			if (entity.collided || (!entity.world.isRemote && time > 100)) {
				entity.setDead();
				entity.onCollideWithSolid();
			}

			entity.addVelocity(Vector.DOWN.times(1F / 40));

			World world = entity.world;
			if (!entity.isDead && !world.isRemote) {
				List<Entity> collidedList = world.getEntitiesWithinAABBExcludingEntity(entity,
						entity.getExpandedHitbox());
				if (!collidedList.isEmpty()) {
					Entity collided = collidedList.get(0);
					if (entity.canCollideWith(collided) && collided != entity.getOwner()) {
						collision(collided, entity);
					}
				}
			}

			return this;

		}

		private void collision(Entity collided, EntityFireball entity) {
			double speed = entity.velocity().magnitude();

			if (entity.canDamageEntity(collided)) {
				collided.setFire(STATS_CONFIG.fireballSettings.fireTime);

				if (collided.attackEntityFrom(AvatarDamageSource.causeFireballDamage(collided, entity.getOwner()),
						entity.getDamage())) {
					BattlePerformanceScore.addMediumScore(entity.getOwner());
				}
			}
			if (entity.canCollideWith(collided) && collided.canBeCollidedWith()) {

				Vector motion = entity.velocity().dividedBy(20);
				motion = motion.times(STATS_CONFIG.fireballSettings.push).withY(0.08);
				collided.addVelocity(motion.x(), motion.y(), motion.z());

				BendingData data = Bender.get(entity.getOwner()).getData();
				if (!collided.world.isRemote && data != null) {
					float xp = SKILLS_CONFIG.fireballHit;
					if (entity.getAbility() != null) {
						data.getAbilityData(entity.getAbility().getName()).addXp(xp);
					}

				}

				// Remove the fireball & spawn particles
				if (!entity.world.isRemote) {
					entity.onCollideWithSolid();
				}
				entity.setDead();

			}
		}

		@Override
		public void fromBytes(PacketBuffer buf) {
		}

		@Override
		public void toBytes(PacketBuffer buf) {
		}

		@Override
		public void load(NBTTagCompound nbt) {
		}

		@Override
		public void save(NBTTagCompound nbt) {
		}

	}

	public static class PlayerControlled extends FireballBehavior {

		public PlayerControlled() {
		}

		@Override
		public FireballBehavior onUpdate(EntityFireball entity) {
			EntityLivingBase owner = entity.getOwner();

			if (owner == null) return this;

			BendingData data = Objects.requireNonNull(Bender.get(owner)).getData();

			Raytrace.Result res = Raytrace.getTargetBlock(owner, 3, false);

			Vector target;
			if (res.hitSomething()) {
				target = res.getPosPrecise();
			} else {
				Vector look = Vector.toRectangular(Math.toRadians(owner.rotationYaw),
						Math.toRadians(owner.rotationPitch));
				target = Vector.getEyePos(owner).plus(look.times(3));
			}

			Vector motion = Objects.requireNonNull(target).minus(entity.position());
			motion = motion.times(0.5 * 20);
			entity.setVelocity(motion);

			if (entity.getAbility() instanceof AbilityFireball) {
				if (data.getAbilityData("fireball").isMasterPath(AbilityTreePath.SECOND)) {
					int size = entity.getSize();
					if (size < 60 && entity.ticksExisted % 4 == 0) {
						entity.setSize(size + 1);
					}
				}
			}
			return this;
		}

		@Override
		public void fromBytes(PacketBuffer buf) {
		}

		@Override
		public void toBytes(PacketBuffer buf) {
		}

		@Override
		public void load(NBTTagCompound nbt) {
		}

		@Override
		public void save(NBTTagCompound nbt) {
		}

	}

}

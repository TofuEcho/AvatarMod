package com.crowsofwar.avatar.common.entity;

import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.List;

public class EntityElementshard extends AvatarEntity {
	private double damageMult;

	public EntityElementshard(World worldIn) {
		super(worldIn);
		setSize(0.5f, 0.5f);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		motionY -= 1.0 / 20;
		if (isCollided) {
			shatter();
		}

		move(MoverType.SELF, motionX, motionY, motionZ);

		// Update rotation to match the velocity adjusted from gravity
		Vector newRotation = Vector.getRotationTo(Vector.ZERO, new Vector(motionX, motionY, motionZ));
		rotationYaw = (float) Math.toDegrees(newRotation.y());
		rotationPitch = (float) Math.toDegrees(newRotation.x());

		// Perform raycast to find targets
		Vector direction = Vector.toRectangular(Math.toRadians(rotationYaw), Math.toRadians(rotationPitch));
		List<Entity> collidedEntities = Raytrace.entityRaytrace(world, new Vector(this), direction, 4,
				entity -> !(entity instanceof EntityPlayer) && !(entity instanceof EntityIceShard));

		if (!collidedEntities.isEmpty()) {

			Entity collided = collidedEntities.get(0);

			DamageSource source = AvatarDamageSource.causeIceShardDamage(collided, null);
			collided.attackEntityFrom(source, 5 * (float) damageMult);

			shatter();

		}

	}

	// Prevent bouncing off of other entities
	@Override
	public void applyEntityCollision(Entity entity) {
	}

	/**
	 * Breaks the ice shard and plays particle/sound effects
	 */
	private void shatter() {
		if (!world.isRemote) {
			float volume = 0.3f + rand.nextFloat() * 0.3f;
			float pitch = 1.1f + rand.nextFloat() * 0.2f;
			world.playSound(null, posX, posY, posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS,
					volume, pitch);
		}

		setDead();
	}

	/**
	 * Sets the shard's rotations and motion to the given rotations/speed.
	 * Parameters should be in degrees.
	 *
	 * @param speed Speed in m/s
	 */
	public void aim(float yaw, float pitch, double speed) {
		rotationYaw = yaw;
		rotationPitch = pitch;

		double yawRad = Math.toRadians(yaw);
		double pitchRad = Math.toRadians(pitch);
		Vector velocity = Vector.toRectangular(yawRad, pitchRad).times(speed).dividedBy(20);
		motionX = velocity.x();
		motionY = velocity.y();
		motionZ = velocity.z();

	}

	public double getDamageMult() {
		return damageMult;
	}

	public void setDamageMult(double damageMult) {
		this.damageMult = damageMult;
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {

	}

}


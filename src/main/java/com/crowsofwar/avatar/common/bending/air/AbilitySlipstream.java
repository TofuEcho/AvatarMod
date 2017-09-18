package com.crowsofwar.avatar.common.bending.air;

import com.crowsofwar.avatar.common.bending.Abilities;
import com.crowsofwar.avatar.common.bending.Ability;
import com.crowsofwar.avatar.common.bending.earth.Earthbending;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

public class AbilitySlipstream extends Ability {
    public AbilitySlipstream() {
        super(Airbending.ID, "slipstream");
    }

    @Override
    public void execute(AbilityContext ctx) {
        BendingData data = ctx.getData();
        EntityLivingBase entity = ctx.getBenderEntity();
        Bender bender = ctx.getBender();
        World world = ctx.getWorld();
        if (bender.consumeChi(STATS_CONFIG.chiSlipstream)) {
            AbilityData abilityData = data.getAbilityData(this);
                entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100));
                data.getAbilityData("cleanse").addXp(6);


                if (abilityData.getLevel() == 2) {
                    entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 100));
                    entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 1));

                    data.getAbilityData("cleanse").addXp(5);
                    if (abilityData.getLevel() == 3) {
                        entity.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 100));
                        entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 100, 1));
                        entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 2));
                        data.getAbilityData("cleanse").addXp(4);
                        if (abilityData.isMasterPath(AbilityData.AbilityTreePath.FIRST)) {
                            entity.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 100, 1));
                            entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 100, 1));
                            entity.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 100, 1));
                            entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 100, 1));
                            entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 100, 1));
                            if (abilityData.isMasterPath(AbilityData.AbilityTreePath.SECOND)) {
                                entity.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 200, 2));
                                entity.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 200, 2));
                                entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 200, 3));

                            }

                        }
                    }
                }
            }
        }
    }


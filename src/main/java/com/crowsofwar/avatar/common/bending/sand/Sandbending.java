package com.crowsofwar.avatar.common.bending.sand;

import com.crowsofwar.avatar.common.bending.BendingStyle;
import com.crowsofwar.avatar.common.bending.earth.Earthbending;
import com.crowsofwar.avatar.common.gui.BendingMenuInfo;
import com.crowsofwar.avatar.common.gui.MenuTheme;

import java.awt.*;
import java.util.UUID;

/**
 * @author CrowsOfWar
 */
public class Sandbending extends BendingStyle {

	public static final UUID ID = UUID.fromString("37d96050-0532-4030-8022-0f302acb7fda");

	private final BendingMenuInfo radialMenu;

	public Sandbending() {

		super(Earthbending.ID);

		addAbility("sand_prison");
		addAbility("sandstorm");

		Color light = new Color(225, 225, 225);
		Color brown = new Color(79, 57, 45);
		Color gray = new Color(90, 90, 90);
		Color lightBrown = new Color(255, 235, 224);
		MenuTheme.ThemeColor background = new MenuTheme.ThemeColor(lightBrown, brown);
		MenuTheme.ThemeColor edge = new MenuTheme.ThemeColor(brown, brown);
		MenuTheme.ThemeColor icon = new MenuTheme.ThemeColor(gray, light);
		radialMenu = new BendingMenuInfo(new MenuTheme(background, edge, icon, 0xB09B7F), this);
	}

	@Override
	public BendingMenuInfo getRadialMenu() {
		return radialMenu;
	}

	@Override
	public String getName() {
		return "sandbending";
	}

	@Override
	public UUID getId() {
		return ID;
	}
}

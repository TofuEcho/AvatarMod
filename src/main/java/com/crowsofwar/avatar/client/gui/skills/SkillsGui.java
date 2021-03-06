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
package com.crowsofwar.avatar.client.gui.skills;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import com.crowsofwar.avatar.AvatarMod;
import com.crowsofwar.avatar.client.gui.AvatarUiTextures;
import com.crowsofwar.avatar.client.uitools.*;
import com.crowsofwar.avatar.common.bending.*;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.gui.*;
import com.crowsofwar.avatar.common.network.packets.PacketSUseScroll;
import com.crowsofwar.gorecore.format.*;
import org.lwjgl.input.*;

import java.io.IOException;
import java.util.*;

import static com.crowsofwar.avatar.client.uitools.Measurement.fromPixels;
import static com.crowsofwar.avatar.client.uitools.ScreenInfo.*;
import static net.minecraft.client.Minecraft.getMinecraft;
import static org.lwjgl.input.Keyboard.KEY_ESCAPE;

/**
 * @author CrowsOfWar
 */
public class SkillsGui extends GuiContainer implements AvatarGui {

	private static final FormattedMessage MSG_TITLE = FormattedMessage.newChatMessage("avatar.ui.skillsMenu", "bending");

	private final UUID bendingId;

	private AbilityCard[] cards;
	private ComponentBendingTab[] tabs;
	private int scroll;

	private WindowAbility window;
	private Frame frame;

	private ComponentInventorySlots inventory, hotbar;
	private ComponentText title;
	private ComponentImageNonSquare background;
	private UiComponentHandler handler;

	public SkillsGui(UUID guiBending) {
		super(new ContainerSkillsGui(getMinecraft().player, guiBending));
		bendingId = guiBending;

		ContainerSkillsGui skillsContainer = (ContainerSkillsGui) inventorySlots;
		BendingData data = BendingData.get(getMinecraft().player);

		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

		xSize = res.getScaledWidth();
		ySize = res.getScaledHeight();

		ScreenInfo.refreshDimensions();

		BendingStyle controller = BendingStyles.get(guiBending);
		List<Ability> abilities = controller.getAllAbilities();
		cards = new AbilityCard[abilities.size()];
		for (int i = 0; i < abilities.size(); i++) {
			cards[i] = new AbilityCard(abilities.get(i), i);
		}

		handler = new UiComponentHandler();

		UUID[] types = data.getAllBending().stream()//
						.map(BendingStyle::getId)//
						.sorted((id1, id2) -> {
							BendingStyle c1 = BendingStyles.get(id1);
							BendingStyle c2 = BendingStyles.get(id2);
							return c1.getName().compareTo(c2.getName());
						})//
						.toArray(UUID[]::new);

		tabs = new ComponentBendingTab[types.length];
		for (int i = 0; i < types.length; i++) {
			float scale = 1.4f;
			BendingStyle style = BendingStyles.get(types[i]);
			tabs[i] = new ComponentBendingTab(style, types[i] == guiBending);
			tabs[i].setPosition(StartingPosition.MIDDLE_BOTTOM);
			tabs[i].setOffset(Measurement.fromPixels(24 * scaleFactor() * (i - types.length / 2) * scale, 0));
			tabs[i].setScale(scale);
			handler.add(tabs[i]);
		}

		inventory = new ComponentInventorySlots(inventorySlots, 9, 3, skillsContainer.getInvIndex(), skillsContainer.getInvIndex() + 26);
		inventory.useTexture(AvatarUiTextures.skillsGui, 0, 54, 169, 83);
		inventory.setPosition(StartingPosition.BOTTOM_RIGHT);
		inventory.setPadding(fromPixels(7, 7));
		inventory.setVisible(false);

		hotbar = new ComponentInventorySlots(inventorySlots, 9, 1, skillsContainer.getHotbarIndex(), skillsContainer.getHotbarIndex() + 8);
		hotbar.setPosition(StartingPosition.BOTTOM_RIGHT);
		hotbar.setVisible(false);

		title = new ComponentText(TextFormatting.BOLD + FormattedMessageProcessor
						.formatText(MSG_TITLE, I18n.format("avatar.ui.skillsMenu"), BendingStyles.get(guiBending).getName().toLowerCase()));
		title.setPosition(StartingPosition.TOP_CENTER);
		title.setOffset(Measurement.fromPixels(0, 10));
		handler.add(title);

		ResourceLocation bgTexture = AvatarUiTextures.getBendingBackgroundTexture(guiBending);
		int bgWidth = (int) AvatarUiTextures.getBendingBackgroundWidth(guiBending);
		int bgHeight = (int) AvatarUiTextures.getBendingBackgroundHeight(guiBending);
		background = new ComponentImageNonSquare(bgTexture, bgWidth, bgHeight);
		background.setZLevel(-1);
		handler.add(background);

	}

	@Override
	public void initGui() {
		super.initGui();
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

		ScreenInfo.refreshDimensions();

	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		ContainerSkillsGui container = (ContainerSkillsGui) inventorySlots;
		ItemStack scroll = container.getSlot(0).getStack();

	}

	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		scroll += Mouse.getDWheel() / 3;

		if (Mouse.isButtonDown(0) && !isWindowOpen()) {

			int mouseX = Mouse.getX(), mouseY = screenHeight() - Mouse.getY();

			for (AbilityCard card : cards) {
				if (card.isMouseHover(mouseX, mouseY, scroll)) {
					openWindow(card);
					break;
				}
			}

		}

	}

	@Override
	protected void actionPerformed(GuiButton button) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

		BendingData data = BendingData.get(mc.player);

		// Update background to have the correct scaling based on screen dimensions
		float imageWidth = AvatarUiTextures.getBendingBackgroundWidth(bendingId);
		float imageHeight = AvatarUiTextures.getBendingBackgroundHeight(bendingId);
		float scaleX = width / imageWidth, scaleY = height / imageHeight;
		background.setScale(Math.max(scaleX, scaleY));

		// Offset image so it is still centered
		ScaledResolution res = new ScaledResolution(mc);
		float offsetX = (width * res.getScaleFactor() - background.width()) / 2;
		float offsetY = (height * res.getScaleFactor() - background.height()) / 2;
		background.setOffset(Measurement.fromPixels(offsetX, offsetY));

		handler.draw(partialTicks, mouseX, mouseY);

		if (isWindowOpen()) {
			window.draw(partialTicks);
		} else {

			for (AbilityCard card : cards) {
				card.draw(partialTicks, scroll, mouseX, mouseY);
			}

		}

		inventory.setVisible(isWindowOpen());
		inventory.draw(partialTicks, mouseX, mouseY);
		hotbar.draw(partialTicks, mouseX, mouseY);

	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {

		if (isWindowOpen()) {
			KeyBinding invKb = mc.gameSettings.keyBindInventory;

			if (window.isEditing() && keyCode == KEY_ESCAPE) {
				window.keyTyped(keyCode);
			} else if (keyCode == 1 || invKb.isActiveAndMatches(keyCode)) {
				closeWindow();
			} else {
				window.keyTyped(keyCode);
			}
		} else {
			handler.type(keyCode);
			if (keyCode == Keyboard.KEY_A || keyCode == Keyboard.KEY_LEFT) {
				scroll += 25;
			} else if (keyCode == Keyboard.KEY_D || keyCode == Keyboard.KEY_RIGHT) {
				scroll -= 25;
			} else {
				super.keyTyped(typedChar, keyCode);
			}
		}

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (window != null) {
			window.mouseClicked(mouseX, mouseY, mouseButton);
		} else {
			handler.click(mouseX, mouseY, mouseButton);
		}
	}

	@Override
	protected void renderToolTip(ItemStack stack, int x, int y) {
		// Prevent rendering tooltip of ItemStacks if they are in a slot that
		// needs a tooltip
		if (!isWindowOpen() || window.canRenderTooltip(stack)) {
			super.renderToolTip(stack, x, y);
		}
	}

	private boolean isWindowOpen() {
		return window != null;
	}

	private void openWindow(AbilityCard card) {
		window = new WindowAbility(card.getAbility(), this);
		inventory.setVisible(true);
		hotbar.setVisible(true);
	}

	public void openWindow(Ability ability) {
		for (AbilityCard card : cards) {
			if (card.getAbility() == ability) {
				openWindow(card);
				break;
			}
		}
	}

	public void closeWindow() {
		window.onClose();
		window = null;
		inventory.setVisible(false);
		hotbar.setVisible(false);

	}

	/**
	 * Called when the 'use scroll' button is clicked
	 */
	public void useScroll(Ability ability) {
		ContainerSkillsGui container = (ContainerSkillsGui) inventorySlots;

		if (container.getSlot(0).getHasStack() || container.getSlot(1).getHasStack()) {
			AvatarMod.network.sendToServer(new PacketSUseScroll(ability));
		}

	}

}

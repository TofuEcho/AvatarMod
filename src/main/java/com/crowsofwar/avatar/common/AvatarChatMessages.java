package com.crowsofwar.avatar.common;

import static crowsofwar.gorecore.chat.ChatSender.newChatMessage;

import crowsofwar.gorecore.chat.ChatMessage;
import crowsofwar.gorecore.chat.MessageConfiguration;

public class AvatarChatMessages {
	
	public static final MessageConfiguration CFG = new MessageConfiguration();
	public static final ChatMessage MSG_BENDING_BRANCH_INFO = newChatMessage(CFG, "avatar.cmd.bending");
	public static final ChatMessage MSG_BENDING_LIST_NO_DATA = newChatMessage(CFG, "avatar.cmd.bending.list.noData",
			"player");
	public static final ChatMessage MSG_BENDING_LIST_NONBENDER = newChatMessage(CFG, "avatar.cmd.bending.list.nonbender",
			"player");
	public static final ChatMessage MSG_BENDING_LIST_ITEM = newChatMessage(CFG, "avatar.cmd.bending.list.item", "bending");
	public static final ChatMessage MSG_BENDING_LIST_TOP = newChatMessage(CFG, "avatar.cmd.bending.list.top",
			"player", "amount");
	
	public static final ChatMessage MSG_EARTHBENDING = newChatMessage(CFG, "avatar.earthbending");
	public static final ChatMessage MSG_FIREBENDING = newChatMessage(CFG, "avatar.firebending");
	public static final ChatMessage MSG_WATERBENDING = newChatMessage(CFG, "avatar.waterbending");
	
	
}

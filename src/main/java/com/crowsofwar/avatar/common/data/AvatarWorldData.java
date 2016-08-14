package com.crowsofwar.avatar.common.data;

import com.crowsofwar.gorecore.data.GoreCorePlayerData;
import com.crowsofwar.gorecore.data.GoreCoreWorldDataPlayers;
import com.crowsofwar.gorecore.data.PlayerDataFetcherServer.WorldDataFetcher;

import net.minecraft.world.World;

public class AvatarWorldData extends GoreCoreWorldDataPlayers<AvatarPlayerData> {
	
	public static final WorldDataFetcher<AvatarWorldData> FETCHER = new WorldDataFetcher<AvatarWorldData>() {
		@Override
		public AvatarWorldData fetch(World world) {
			return getDataFromWorld(world);
		}
	};
	
	public static final String WORLD_DATA_KEY = "Avatar";
	
	public AvatarWorldData() {
		super(WORLD_DATA_KEY);
		// TODO Auto-generated constructor stub
	}
	
	public AvatarWorldData(String key) {
		super(WORLD_DATA_KEY);
	}
	
	@Override
	public Class<? extends GoreCorePlayerData> playerDataClass() {
		return AvatarPlayerData.class;
	}
	
	public static AvatarWorldData getDataFromWorld(World world) {
		return getDataForWorld(AvatarWorldData.class, WORLD_DATA_KEY, world, false);
	}
	
}

package com.tyanns.luckyvalue;

import com.tyanns.luckyvalue.command.LuckyValueCommand;
import com.tyanns.luckyvalue.network.LuckyValueS2CPayload;
import com.tyanns.luckyvalue.server.DayChangeHandler;
import com.tyanns.luckyvalue.server.LuckyValueManager;
import com.tyanns.luckyvalue.server.PlayerJoinHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LuckyValue implements ModInitializer {
	public static final String MOD_ID = "lucky-value";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			LuckyValueManager.load();
		});
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			LuckyValueManager.save();
		});
		PayloadTypeRegistry.playS2C().register(LuckyValueS2CPayload.ID, LuckyValueS2CPayload.CODEC);
		DayChangeHandler.register();
		PlayerJoinHandler.register();
		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) ->
						LuckyValueCommand.register(dispatcher));
		LOGGER.info("Hello lucky world!");
	}
}
package com.planningproject;

import com.google.inject.Provides;
import javax.inject.Inject;

import joptsimple.internal.Strings;
import lombok.extern.slf4j.Slf4j;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.game.ItemManager;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

@Slf4j
@PluginDescriptor(
	name = "Planning Project"
)
public class PlanningProjectPlugin extends Plugin
{
	public static final String CONFIG_GROUP = "planningproject";
	public static final String CONFIG_KEY = "tasks";

	@Inject
	private Client client;

	@Inject
	private PlanningProjectConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ConfigManager configManager;

	@Inject
	private Gson gson;

	private NavigationButton navButton;
	private PlanningTaskListManager taskListManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Planning Project started!");

		taskListManager = new PlanningTaskListManager(this, loadConfig());

		final BufferedImage sidebarIcon = ImageUtil.getResourceStreamFromClass(getClass(), "/sidebar_icon.png");
		navButton = NavigationButton.builder()
				.tooltip("Planning Project")
				.icon(sidebarIcon)
				.priority(10)
				.panel(taskListManager.getPanel())
				.build();
		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	PlanningProjectConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PlanningProjectConfig.class);
	}

	private Stream<PlanningTask> loadConfig()
	{
		String json = configManager.getConfiguration(CONFIG_GROUP, CONFIG_KEY);

		if(Strings.isNullOrEmpty(json))
		{
			return Stream.empty();
		}

		log.info("Loaded some config!");
		final List<PlanningTask> planningTaskData = gson.fromJson(json,
				new TypeToken<ArrayList<PlanningTask>>(){}.getType());

		return planningTaskData.stream().filter(Objects::nonNull);
	}
}

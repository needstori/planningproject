package com.planningproject;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PlanningProjectPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(PlanningProjectPlugin.class);
		RuneLite.main(args);
	}
}
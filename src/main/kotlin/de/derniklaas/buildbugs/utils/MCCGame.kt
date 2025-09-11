package de.derniklaas.buildbugs.utils

import de.derniklaas.buildbugs.Constants

enum class MCCGame(val displayName: String, val types: Set<String>) {
    PARKOUR_WARRIOR("Parkour Warrior", setOf(Constants.PARKOUR_WARRIOR)),
    HOLE_IN_THE_WALL("HITW", setOf(Constants.HOLE_IN_THE_WALL, Constants.HOLE_IN_THE_WALL_EVENT)),
    TO_GET_TO_THE_OTHER_SIDE("TGTTOS", setOf(Constants.TO_GET_TO_THE_OTHER_SIDE)),
    BATTLE_BOX("Battle Box", setOf(Constants.BATTLE_BOX)),
    SKY_BATTLE("Sky Battle", setOf(Constants.SKY_BATTLE)),
    DYNABALL("Dynaball", setOf(Constants.DYNABALL)),
    ROCKET_SPLEEF_RUSH("Rocket Spleef", setOf(Constants.ROCKET_SPLEEF_RUSH)),
    ACE_RACE("Ace Race", setOf(Constants.ACE_RACE)),
    RAILROAD_RUSH("Railroad Rush", setOf(Constants.RAILROAD_RUSH)),
    SANDS_OF_TIME("", setOf(Constants.SANDS_OF_TIME)),
    SURVIVAL_GAMES("Survival Games", setOf(Constants.SURVIVAL_GAMES)),
    PARKOUR_TAG("Parkour Tag", setOf(Constants.PARKOUR_TAG)),
    MELTDOWN("", setOf(Constants.MELTDOWN)),
    GRID_RUNNERS("Grid Runners", setOf(Constants.GRID_RUNNERS)),
    BINGO_BUT_FAST("Bingo But Fast", setOf(Constants.BINGO_BUT_FAST)),
    BUILD_MART("Build Mart", setOf(Constants.BUILD_MART)),
    HUB("Hub", setOf(Constants.HUB)),
    LIMBO("", setOf(Constants.LIMBO)),
    UNKNOWN("", setOf(Constants.UNKNOWN));
}
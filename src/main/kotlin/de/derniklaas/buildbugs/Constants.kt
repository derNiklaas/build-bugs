package de.derniklaas.buildbugs

object Constants {
    /* Game types */
    const val LOBBY = "lobby"
    const val PARKOUR_WARRIOR = "parkour-warrior"
    const val HOLE_IN_THE_WALL = "hitw"
    const val TO_GET_TO_THE_OTHER_SIDE = "tgttos"
    const val BATTLE_BOX = "battle-box"
    const val SKY_BATTLE = "sky-battle"
    const val DYNABALL = "dynaball"
    const val ROCKET_SPLEEF_RUSH = "rocket-spleef"
    const val LIMBO = "limbo"

    /* Event Types */
    const val HUB = "hub"
    const val ACE_RACE = "ace-race"
    const val RAILROAD_RUSH = "railroad-rush"
    const val SANDS_OF_TIME = "sands-of-time"
    const val SURVIVAL_GAMES = "survival-games"
    const val PARKOUR_TAG = "parkour-tag"
    const val MELTDOWN = "meltdown"
    const val GRID_RUNNERS = "grid-runners"
    const val BINGO_BUT_FAST = "bingo-but-fast"
    const val BUILD_MART = "build-mart"
    const val HOLE_IN_THE_WALL_EVENT = "hole-in-the-wall"
    const val DODGEBOLT = "dodgebolt"

    /* Parkour Warrior subtypes */
    const val PARKOUR_WARRIOR_DAILY = "daily"
    const val PARKOUR_WARRIOR_DOJO = "main"
    const val PARKOUR_WARRIOR_SURVIVOR = "survival"

    /* Phases */
    const val PODIUM_PHASE = "podiumPhase"

    /* URLs */
    const val BUG_REPORT_URL = "https://p.nox.gs/"

    /* Groups */
    val LOBBIES = listOf(LOBBY, HUB, LIMBO)
}

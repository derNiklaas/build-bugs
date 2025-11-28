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

    /* Fishing Islands & Grottos */
    const val VERDANT_WOODS = "temperate_1"
    const val FLORAL_FOREST = "temperate_2"
    const val DARK_GROVE = "temperate_3"
    const val SUNKEN_SWAMP = "temperate_grotto"

    const val TROPICAL_OVERGROWTH = "tropical_1"
    const val CORAL_SHORES = "tropical_2"
    const val TWISTED_SWAMP = "tropical_3"
    const val MIRRORED_OASIS = "tropical_grotto"

    const val ANCIENT_SANDS = "barren_1"
    const val BLAZING_CANYON = "barren_2"
    const val ASHEN_WASTES = "barren_3"
    const val VOLCANIC_SPRINGS = "barren_grotto"

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

    /* Game subtypes */
    const val PARKOUR_WARRIOR_DAILY = "daily"
    const val PARKOUR_WARRIOR_DOJO = "main"
    const val PARKOUR_WARRIOR_SURVIVOR = "survival"
    const val BATTLE_BOX_ARENA = "arena"

    /* Phases */
    const val PODIUM_PHASE = "podiumphase"

    /* URLs */
    const val BUG_REPORT_URL = "https://p.nox.gs/"

    /* Misc */
    const val UNKNOWN = "Unknown"

    /* Groups */
    val LOBBIES = listOf(LOBBY, HUB, LIMBO)
    val GAMES = listOf(
        ACE_RACE,
        RAILROAD_RUSH,
        SANDS_OF_TIME,
        SURVIVAL_GAMES,
        PARKOUR_TAG,
        MELTDOWN,
        GRID_RUNNERS,
        BINGO_BUT_FAST,
        BUILD_MART,
        HOLE_IN_THE_WALL,
        HOLE_IN_THE_WALL_EVENT,
        DODGEBOLT,
        TO_GET_TO_THE_OTHER_SIDE,
        BATTLE_BOX,
        PARKOUR_WARRIOR,
        SKY_BATTLE,
        DYNABALL,
        ROCKET_SPLEEF_RUSH
    )
    val ALL_TYPES = LOBBIES + GAMES
}

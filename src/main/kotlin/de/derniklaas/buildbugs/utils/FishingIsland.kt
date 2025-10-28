package de.derniklaas.buildbugs.utils

import de.derniklaas.buildbugs.Constants

enum class FishingIsland(val displayName: String, val type: String) {
    VERDANT_WOODS("Verdant Woods", Constants.VERDANT_WOODS),
    FLORAL_FOREST("Floral Forest", Constants.FLORAL_FOREST),
    DARK_GROVE("Dark Grove", Constants.DARK_GROVE),
    SUNKEN_SWAMP("Sunken Swamp", Constants.SUNKEN_SWAMP),
    TROPICAL_OVERGROWTH("Tropical Overgrowth", Constants.TROPICAL_OVERGROWTH),
    CORAL_SHORES("Coral Shores", Constants.CORAL_SHORES),
    TWISTED_SWAMP("Twisted Swamp", Constants.TWISTED_SWAMP),
    MIRRORED_OASIS("Mirrored Oasis", Constants.MIRRORED_OASIS),
    ANCIENT_SANDS("Ancient Sands", Constants.ANCIENT_SANDS),
    BLAZING_CANYON("Blazing Canyon", Constants.BLAZING_CANYON),
    ASHEN_WASTES("Ashen Wastes", Constants.ASHEN_WASTES),
    VOLCANIC_SPRINGS("Volcanic Springs", Constants.VOLCANIC_SPRINGS),
    UNKNOWN("", Constants.UNKNOWN);
}
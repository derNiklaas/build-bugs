package de.derniklaas.buildbugs

import java.io.FileInputStream
import java.io.FileOutputStream
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.fabricmc.loader.api.FabricLoader

@Serializable
data class BuildBugsConfig(
    var version: Int, var debugMode: Boolean, var copyToClipboard: Boolean, var eventIP: String
) {
    companion object {
        private val DEFAULT =
            BuildBugsConfig(version = 2, debugMode = false, copyToClipboard = false, eventIP = "example.com")
        private val path = FabricLoader.getInstance().configDir.resolve("build-bugs.json")

        @OptIn(ExperimentalSerializationApi::class)
        fun fromFile(): BuildBugsConfig {
            if (path.toFile().exists()) {
                try {
                    return FileInputStream(path.toFile()).use {
                        Json.decodeFromStream<BuildBugsConfig>(it)
                    }
                } catch (ex: Exception) {
                    DEFAULT.saveConfig()
                    return fromFile()
                }
            } else {
                DEFAULT.saveConfig()
                return fromFile()
            }
        }

        fun createDefaultConfig() {
            if (path.toFile().exists()) return
            DEFAULT.saveConfig()
        }
    }

    /**
     * Sets the debug mode and saves the config file.
     */
    fun setDebug(newValue: Boolean) {
        debugMode = newValue
        saveConfig()
    }

    /**
     * Sets the copy to clipboard mode and saves the config file.
     */
    fun setCopy(newValue: Boolean) {
        copyToClipboard = newValue
        saveConfig()
    }

    fun setEventAddress(newValue: String) {
        eventIP = newValue
        saveConfig()
    }

    /**
     * Saves the config file to disk.
     */
    @OptIn(ExperimentalSerializationApi::class)
    private fun saveConfig() {
        FileOutputStream(path.toFile()).use {
            Json.encodeToStream(this, it)
        }
    }
}

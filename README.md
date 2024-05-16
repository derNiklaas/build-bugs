# Build Bugs

> A simple utility to speed up (build-) bug reports.

### Requirements

You need Minecraft 1.20.6 and at least [Noxesium 2.0.4](https://modrinth.com/mod/noxesium/version/2.0.4).

### Usage

This mod adds the ``/buildbug`` command and two hotkeys, the first hotkey (default ``U``) is used to quickly gather
information about your location.
The second hotkey (default ``I``) is used to quickly generate a /bugreport.

- ``/buildbug`` - Prints out location information, same as the hotkey.
- ``/buildbug config clipboard <true/false>`` (default: ``false``) - If set to true, automatically copies
  any ``/bugreport`` links or ``/buildbug`` coordinates to the clipboard.
- ``/buildbug config debug <true/false>`` (default: ``false``) - If set to true, prints out any incoming noxesium packet
  and information about the current internal state.
- ``/buildbug config eventip <IP>`` (default:``example.com``) - Allows the mod to also function on the specified server.
- ``/buildbug force_state <type> <subtype> <map>`` - Forces the internal state of the mod.
- ``/buildbug reset_state`` - Resets the internal state of the mod.
- ``/buildbug version`` - Prints out the version of the mod.

![](images/example.png)

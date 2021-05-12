# NaturalVanish
NaturalVanish is a plugin intended to help your staff members watch players on the server.
One of the main ideas NaturalVanish was created with in mind was to have highly configurable
messages, as well as have highly configurable options. Some of these options include setting
the flight speed of vanished players, giving them night vision, and much more.

---
## Downloads
[Download from Spigot](https://www.spigotmc.org/resources/naturalvanish.91617/)

---
## General Features
* Very configurable
* Lots of different vanish settings
* * Ex: Being able to set the flight speed of vanish players
* Supports offline players    

---

## Commands
#### () = required argument
| Command | Default Permission | Description |
| ---------- | -------------- | -------------- |
| /vanish | naturalvanish.use | Toggles your vanish |
| /vanish help | naturalvanish.use | Shows a help message |
| /vanish toggle (player) | naturalvanish.toggle.others | Toggle the arguments vanish |
| /vanish setlevel (level) | naturalvanish.setlevel | Set your vanish level |
| /vanish setlevel (player) (level) | naturalvanish.setlevel.others | Set the vanish level of a player |
| /vanish checklevel | naturalvanish.checklevel | Check your current vanish level |
| /vanish checklevel (player) | naturalvanish.checklevel.others | Check the vanish level of another player |
| /vanish check | naturalvanish.check | Check if you are vanished or not |
| /vanish check (player) | naturalvanish.check.others | Check if another player is vanished |
| /vanish list | naturalvanish.list | List all the players who are vanished, as long as the listed player's levels are less than your level |
| /vanish list | naturalvanish.list.all | List **all** the players who are vanished, no matter the vanish level |
| /vanish reload | naturalvanish.reload | Reload the config and data |

## Event Permissions
#### This is stuff that happens when a player does something, for example right clicking
| Event | Default Permission | Description |
| ---------- | -------------- | -------------- |
| Item Pickup Bypass | naturalvanish.itempickup.bypass | Allows a player to pick up an item while vanished, even when it should be cancelled |
| Item Drop Bypass | naturalvanish.itemdrop.bypass | Allows a player to drop items while vanished, even if it would normally be cancelled |
---

## FAQ
> What is the "see-self" option?

This just adds invisibility to the player, along with vanishing them. The intended purpose
of this is to allow vanished players to more easily tell if they are vanished or not.

> What is the "skull-head" option?

This only works if the "see-self" option is set to false. This sets the player's helmet
to their skull, meaking it easy to see where they are (only visible to players with vanish
levels higher than or equal to the player's level).

> I found a bug/I have a suggestion! Where should I put it?

If you found a bug, please open an issue on the GitHub, describe what the bug is, and add
the `bug` label, as well as what severity you think the bug is. For suggestions, the process
is similar to that of bugs, however please just add the `suggestion` label instead of 
the `bug` label.
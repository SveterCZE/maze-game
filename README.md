<h1>Introduction</h1>
My first large project built in Java, a 2D tile-based world genration and exploration game. It is based on the final project of CS61B, Berkeley's data structures class (see https://sp19.datastructur.es/materials/proj/proj3/proj3).

The rendering engine, some starter code as well as the random numbers generator were provided by Berkeley team. However, I wrote all the code for world generation and the interaction between the player and the environment. I did not attend the class and followed just its on-line version.

<h1>Installation</h1>
Download all the files and simply run Main.java.

<h1>Features</h1>
Once the program is launched, a menu window will be displayed.

<img src="https://user-images.githubusercontent.com/46304018/143784440-713240e0-979e-4d96-b853-18282c75db2b.png" width="35%" height="35%">

The following options are available:
* Press 'N' or 'n' to start a new game.
* Press 'L' or 'l' to load a saved game.
* Press 'Q' or 'q' to quit the game.

After selecting a new game, another window will be displayed, asking the user to enter a seed. The seed is a positive number up to 9,223,372,036,854,775,807 and is used by the random generator to generate a unique game world. That means that if you enter the same seed, the identical world be generated.

<img src="https://user-images.githubusercontent.com/46304018/143784599-bc2f7d44-a8df-448f-9ad1-23f44a200a31.png" width="35%" height="35%"> <img src="https://user-images.githubusercontent.com/46304018/143784707-82db83c9-ea30-4938-a28b-e9b5603ecec1.png" width="35%" height="35%">


Once the seed is entered, press 'S' or 's' to generate the world. Only the player avatar (@) and its surroundings are visible, while the rest of the map is covered by a fog of war. Also, a status bar is diplayed on the top of the window, displaying the level number, identification of tile over which a mouse cursor is hovering, player's health and its inventory.
![image](https://user-images.githubusercontent.com/46304018/143784905-bb63ae86-07d3-4772-9231-bcf4a496ecfb.png)

The world looks something like this - it consist of uniquely generated rooms and corridors connecting them together. Various items are randomly generated around the map.
![image](https://user-images.githubusercontent.com/46304018/143785043-7ad43fa5-1de0-4014-9933-05fb777f9096.png)

<h2>Game items</h2>
The following items can be found in the world:

| Game item | Symbol | Description |
| --- | --- | --- |
| Avatar | @ | Player's avatar. Press 'W', 'A', 'S', 'D' to move it around the world. If the player's health drops to 0, it dies. |
| Ghost | ![ghost](https://user-images.githubusercontent.com/46304018/143785600-01b057a7-b8cf-45a2-a924-73d15f46c6b5.png) | Ghost. Uses a pathfinding algorithm to chase the player around the map. If the ghost catches the player, the player dies unless it equipped a magic potion, in which case the ghost dies. |
| Anti-ghost potion | ☕ | A magic potion. If equipped by the player, it will survive an encounter with the ghost. |
| World map | ![map](https://user-images.githubusercontent.com/46304018/143785699-e164a252-b2af-443e-8cce-87688aa854cc.png) | A map that removes fog of war and displays the whole map. |
| Marker | ✎ | If equipped by the player, it marks the tiles that have been visited by it to aid in the navigation around the world. Very helpful if the player does not have a map. |
| Poison | ☠ | Poisons the player, causing it to lose 35 health points. |
| Radioactivity | ☢ | Exposes the player to radioactivity. It loses 25 health points immediatly and 1 additional point per turn. |
| First aid kit | **+** | Restores 30 health points if the player was not exposed to radioactivity. If it was exposed, the first aid kit removes the effects of radioactivity, but does not restore any health points. |
| Key | ⚿ | When equipped, opens the door to allow the player to enter the next level. |
| Locked door | █ | Door to the next level. A key must be equipped to open the door. |
| Open door | ▢ | Door to the next level. Once reached by the player, a new level will start. |

<h2>Game objectives</h2>
In each level, the player must:
* Find the key.
* Enter the door to the next level.
* Avoid being killed by the ghost, poisons or radioactivity..

There are 9,223,372,036,854,775,807 unique levels available, so each player should have some fun for a while.

<h2>Game controls</h2>

* 'W', 'A', 'S', 'D' – Movement of player around the world
* 'Q' – Quit the game. The game is saved and can be loaded from the main menu.

<h1>Known issues</h1>

* Hard coded resolution for a 4k monitor. Might not work properly on monitors with a lower resolution.
* A minor bug appears when the ghost navigating through rooms of certain shape.

<h1>TODO</h1>

* Improve the fog of war, so that it shows only the room in which the player is located.
* Introduce additional game items and mechanics.

<h1>Testimonials</h1>
"Much better game than Grand Theft Auto: The Trilogy – The Definitive Edition" – every game journalist

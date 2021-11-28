<h1>Introduction</h1>
My first large project built in Java, a 2D tile-based world genration and exploration game. It is based on the final project of CS61B, Berkeley's data structures class (see https://sp19.datastructur.es/materials/proj/proj3/proj3).

The rendering engine, some starter code as well as the random numbers generator were provided by Berkeley team. However, I wrote all the code for world generation and the interaction between the player and the environment.

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


Once the seed is entered, press 'S' or 's' to generate the world.






<h1>Known issues</h1>
* Hard coded resolution for a 4k monitor. Might not work properly on monitors with a lower resolution.
* A minor bug appears when the ghost navigating through rooms of certain shape.

<h1>TODO</h1>

=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: anish001
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays -- I used a 2D array to hold all the bricks constructed on the GameCourt. This was
     best handled by a 2D array since I needed to have an ordered set of the objects in a 2D space
     since I had rows and columns of bricks. I needed to be able to access them to handle score,
     removal, and game physics for how the ball interacted with individual bricks which became simple
     by using a 'for' loop.

  2. Collections -- I used a collection for the leaderboard functionality. I wanted to store a user's
     username and their score to a leaderboard which ranked the top players and showed their name and
     scores. Thus, a Map was best as I could have no duplicate users and each have exactly one score
     mapped to them, their best score (handled when putting value into map). Once I had a Map I had to
     convert it to a list so that I could operate on it via a sort. Then I implemented a loop to
     repeatedly print out each value in order thus having the best players on the top and worst at the
     bottom. Map was best since I had no duplicate users and each had exactly one score attached to it
     since I only care about the singular best score. List was used for its ability to be sorted.

  3. Inheritance and Subtyping -- I created a generic Brick class for the basic blue bricks and then
     utilized inheritance and subtyping for special Bricks. These Bricks are identical to a normal Brick
     aside from color and their behavior with the ball. Thus, I implemented an interact() method in the
     Brick class that is overridden in the special Brick subclasses with new implementations of interact(),
     such as increasing the ball speed, decreasing the ball speed, and increasing the 'strength' of a
     brick. Since all Bricks are stored in an Array of type Brick, subtyping was used to include various
     different kinds of Bricks in the array and when interact is called, the subclass will override. This
     made the most sense since it minimized repeated code drastically and made my code more readable.

  4. File I/O -- I implemented File I/O for a game save functionality. I added two buttons to do so. One
     is 'Save' which writes to the gameState.txt file all the necessary info about the game state. This is
     done via a loop and fileWriter. The second button is 'Load' which uses FileReader to go through the
     file and set up the game board based on the file parameters. It prompts the user for a file name too.
     This function was best applied for save/load since I needed a file to write and save stuff too which
     must be stored outside the class.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  GameCourt (extends JPanel) -- class that handles gameplay logic from drawing elements, score, leaderboard,
  gamestate, interactions, cases etc. It is responsible for setting up and handling the game
  board before, during, and after gameplay. It is a JPanel and handles how it looks.

  RunBrickBreaker (implements Runnable) -- handles setting up the complete GUI with buttons, JPanels,
  (including the GameCourt) and panes.

  Game -- File used to run the game.

  GameObj -- superclass that handles the various game pieces. It contains the parameter that
  control color, position, speed, size, and contact between game objects.

    Ball -- subclass of GameObj that generates a circle that moves the way the ball on the court
    moves (via auto detection). This is a GameObj but circular with a unique color.

    Paddle -- subclass of GameObj that generates a rectangle that moves the way the paddle on the court
    moves (via input). This is a GameObj but rectangular with a unique color.

    Brick -- subclass of GameObj that generates a rectangle that does NOT move. This is a GameObj but
    rectangular with a unique color. It also adds an interact method which controls how the brick,
    game state and ball are affected depending on interaction between ball and brick.

        SpeedBrick -- subclass of Brick with a unique color and interact method. This interact method
        accesses the ball's speed and increments it if it detects a live collision between itself and
        the ball.

        SlowBrick -- subclass of Brick with a unique color and interact method. This interact method
        accesses the ball's speed and decrements it if it detects a live collision between itself and
        the ball.

        DoubleBrick -- subclass of Brick with a unique color and interact method. This interact method
        has a two-step process. Upon first collision the brick remains live but weakens, requiring a
        second collision between the ball and brick to be fully destroyed. This interact method handles
        this while also changing the color to display the full strength, half strength and dead brick.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  One issue I came across was handling the bricks. I needed to be able to have them disappear from
  the screen and become inactive for game physics, however could not simply remove them as they were
  ordered, and I needed the spacing to remain. Thus, merely changing the color of the brick to blend
  in became a simple solution. I also had an issue related to the ball because of this, since the ball
  would go behind the bricks. I initially tried a lot of complex solutions that did not work, until I
  realized I could just draw the ball after the bricks.

  I had to refactor the brick class a handful of times to be easily used as a superclass and this
  ultimately boiled down to an interact method with two carefully chosen parameters, the gamecourt
  and the ball. I had to make all the interact methods compatible with each other and the logic in
  the gamecourt file.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  Function is very well separated in my opinion and the private state has maintained invariants
  and encapsulation throughout the program.

  I think I would refactor the handling of the brick array and make a new method to be used
  in many places as I now notice every time I added a new feature regarding the array, some
  portion of the loop is re-written.

  I also think my handling of colors could be simplified since I have all the colors as parameters
  in the GameCourt file, so I could easily mess around and change them. I now think putting the colors
  in the actual object classes is better since I could remove some getters/setters and have solidified
  my color choices which I no longer need easy access to.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used
  while implementing your game.

  none.

Strike Base Commander
=====================
Realtime Tower Defense Strategy
-------------------------------

Repository for the Game and Engine for the "Strike Base Commander" project.

Engine Simulation details
-------------------------

The Engine follows the **Game Loop** pattern. As many times per second as possible, the controller updates the game state, and then draws
to the screen.

### The Screen & GameObjects

A Screen is the most basic controller of the game. A screen manages a set of states and keeps a Map of GameObject instances. If a 
GameObject is not registered with the Screen, it can't update nor draw it. Below is an overview of how the game is structured:

<img src="https://docs.google.com/drawings/d/1UN6XVimASkeSNaoE2z4galPSptYWZjNzO3QqA-vyryI/pub?w=838&amp;h=441">

As can be seen, a Screen keeps multiple GameObjects, and each GameObject keeps multiple components. Those components give the GameObject 
different behaviors. From drawing to following another GameObject, to listening for player Input. All of these actions are encapsulated 
in their respective components.

Components can also talk to each other. For example, a GraphicsComponent might pull the drawing position and rotation from the 
PhysicsComponent.
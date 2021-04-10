# OthelloAI
Intelligent Othello player implemented in Java

This OthelloAI implementation has two modes:
playMode(): a real user can play the Othello game with the AI agent
testMode(): the AI agent can be tested against itself or a "dummy player" that chooses a random move at each step (90% average success rate against a dummy player).

Arguments 
1) Mode - valid options are "play" or "test"
**Following arguments are only valid for the test mode**
2) First player (string) - valid options are "AI" or "Dummy"
3) Second player (string) - valid options are "AI" or "Dummy"
4) Number of rounds (Integer) - considering the time constraints, around 100 rounds would give a good estimate of the AI's skills 
5) Printed Information (Boolean) - this could be turned on to see which player is winning at each round. However, for large number of rounds, this should be turned off to speed up the process.

After running the program, for either mode, the user must input a depth limit for the AI agent's search. A reasonable depth limit would be between 5-10. 

The AI implementation uses H-MiniMax with Alpha-Beta pruning.
The heuristic function considers the number of pieces on board and their positional value.
Other details are explained with comments in source files.

# EnigmaMachine
This program simulates a three-rotor Enigma machine, as used by German forces in WWII, to encrypt the message. In short, the machine reads in an input message from a file, encrypts the message, and writes the encrypted message or cipher text to an output file.

## More on How it Works
The Enigma machine has three rotors and a backplate; rotors 1, 2, and 3 (in to out) can rotate clockwise, while the backplate (the outer wheel) is stationary. The command line arguments determine each of rotor's initial position (from where to start encryption). 

For each character in the text file, the machine first locates the position of the initial character on rotor 1. The backplate value at the same index (as rotor 1) determines the character to find on rotor 2. This process repeats for rotor 3. The backplate value at the rotor 3 position is the encrypted character.

After each encryption of one letter, rotor 1 rotates clockwise by one position. When rotor 1 completes a full revolution by returning to its original positioning, rotor 2 advances one position in the clockwise direction. Similarly, rotor 3 increments by one in the clockwise direction when rotor 2 completes a full rotation. (This is similar to how the minutes and hours advance on a clock.)

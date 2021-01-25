/**
  * Implements all methods to carry out an enigma machine simulation
  * Originally used four global variables: one for the output file and the three others for the rotors,
  * which are initialized and rotated as necessary (added another for an additional rotor).
  * Used ArrayList methods to maneuver between rotor indexes and values
  * Used file methods to read and write files
  * @author Rebecca Lin
  * @version 2.0
  */

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class EnigmaMachine
{
  // Global variables for the rotors of the Enigma machine
  public static ArrayList<Integer> rotor1 = new ArrayList<Integer>();
  public static ArrayList<Integer> rotor2 = new ArrayList<Integer>();
  public static ArrayList<Integer> rotor3 = new ArrayList<Integer>(); // Added global variable for a 3rd rotor
  public static ArrayList<Integer> backplate = new ArrayList<Integer>();

  // Global variable for the name of the output file
  public static String output;

  public static void main(String [] args)
  {
    // Read in the command line arguments
		String inputFile = args[0];
		output = args[1];
		String strR1 = args[2];
		String strR2 = args[3];
    		String strR3 = args[4];
		String strRB = args[5];

		// Convert strings to ints for rotors starting values
		int R1 = Integer.parseInt(strR1);
		int R2 = Integer.parseInt(strR2);
    		int R3 = Integer.parseInt(strR3);
		int RB = Integer.parseInt(strRB);

		// Declare the input array
		ArrayList<Integer> input = new ArrayList<Integer> ();

		// Call readFile
		readFile(inputFile, input);

		// Initialize the rotors
		initializeRotors(R1, R2, R3, RB);

		// Encrypt and append to file
		encryptFile(input);
  }

  /** Initializes the three rotors with the specified starting ASCII values
    * @param s1 The starting ASCII value for rotor 1
    * @param s2 The starting ASCII value for rotor 2
    * @param s3 The starting ASCII value for rotor 3
    * @param s4 The starting ASCII value for the backplate
    */
  public static void initializeRotors(int s1, int s2, int s3, int s4)
  {
    initializeRotor(rotor1, s1);
    initializeRotor(rotor2, s2);
    initializeRotor(rotor3, s3);
    initializeRotor(backplate, s4);
  }

  /** Fills in the given ArrayList with all printable characters in ASCII value form, from 32 to 126 (clockwise)
    * For example: if s1 is 35, then rotor 1 will have ASCII values from 35, 36, 37, …, 126, 32, 33, 34
    * This is a helper method for the intitializeRotors method
    * @param rotor The ArrayList representing a rotor
    * @param start The ASCII value the rotor will start with
    */
  public static void initializeRotor(ArrayList<Integer> rotor, int start)
  {
    int count = start;

    // Start at the given start (ASCII) value and increment by one until the end value, 126
		while (count <= 126)
		{
			rotor.add(count);
			count ++;
		}

    count = 32;

    // Starting at 32 (if given start value isn't 32), add numbers up to the start value into the ArrayList
		while (count < start)
		{
			rotor.add(count);
			count ++;
		}
  }

  /** Moves the rotor clockwise one position
    * @param rotor The rotor to be shifted once in the clockwise direction
    */
  public static void advanceRotor(ArrayList<Integer> rotor)
  {
    // Remove the last integer in the ArrayList and add it to the front
    rotor.add(0, rotor.remove(94));
  }

  /** Encrypts the innput ArrayList and writes to files
    * @param in The ArrayList that contains the contents, stored in ASCII values, of the input file
    */
  public static void encryptFile(ArrayList<Integer> in)
  {
    int rotateCounter1 = 0;
    int rotateCounter2 = 0;

    for (int i = 0; i < in.size(); i++)
    {
      // Find the plaintext character on rotor 1
      int plaintextValue = in.get(i);

      // Get the position of the plaintext character
      // by finding the rotor1 index of the given ASCII value from the input ArrayList
      int index1 = findIndex(rotor1, plaintextValue);

      // Use the rotor1 index to find the backplate value at the same position
      int backplateValue = backplate.get(index1);

      // Get the backplate character on rotor 2 and determine its index on rotor2
      int index2 = findIndex(rotor2, backplateValue);

      // Use the rotor2 index to find the backplate value at the same position
      backplateValue = backplate.get(index2);

      // Get the backplate character on rotor 3 and determine its index on rotor2
      int index3 = findIndex(rotor3, backplateValue);

      // ciphertext is the character on the backplate at the same position as the position of
			// the backplate character on rotor 3
			int ciphertext = backplate.get(index3);

			// Rotate rotor 1 one position to the right
			advanceRotor(rotor1);

      // Increment the rotation counter to keep track of rotations for rotor 1
			rotateCounter1 ++;

			// When rotor 1 completes a full revolution, advance rotor 2 one position in the clockwise direction
			// 95 is the number of values between 32 - 126 inclusive
			if (rotateCounter1 == 95)
			{
				rotateCounter1 = 0;
				advanceRotor(rotor2);

        // Increment the rotation counter to keep track of rotations for rotor 2
        rotateCounter2 ++;

        // When rotor 2 completes a full revolution, advance rotor 3 one position in the clockwise direction
        if (rotateCounter2 == 95)
        {
          rotateCounter2 = 0;
  				advanceRotor(rotor3);
        }
			}

			// Convert ASCII values into chars and write them into file
			char cipherChar = (char) ciphertext;
			writeToFile(cipherChar);
    }
  }

  /** Finds the index of the same given value on the given rotor
	  * @param rotor The rotor on which the index of the given value will be found
	  * @param value The int value whose index will be found on the given rotor
	  * @return index The index of the same given value but on the given rotor
	  */
  public static int findIndex(ArrayList<Integer> rotor, int value)
  {
    int index = 0;
    int count = 0;
    while (count < rotor.size() && rotor.get(count) != value)
    {
      count ++;
      index ++;
    }
    return index;
  }

	/** Reads the contents of the input file and stores ASCII values the passed in ArrayList.
	  * @param name The filename of the input file.
	  * @param list The ArrayList into which ASCII values will be stored.
	  */
	public static void readFile(String name, ArrayList<Integer> list)
	{
		File file = new File(name);
		Scanner input = null;
		try
		{
			input = new Scanner(file);
		}
		catch (FileNotFoundException ex)
		{
			System.out.println(" Cannot open " + name);
			System.exit(1);
		}

		while (input.hasNextLine())
		{
			String s = input.nextLine();
			for (int i = 0; i < s.length(); i++)
      {
				list.add((int)s.charAt(i));
      }
		}
	}

	/** Writes the given character to the output file. If the file does not exist,
	  * it is created. If it does exist, the character is appended.
	  * @param ch The character to be written.
	  */
	public static void writeToFile(char ch)
	{
		String pathname = output;
		Writer writer = null;

		try
		{
			writer = new FileWriter(pathname, true);
		}
		catch (IOException ex)
		{
			System.out.println("Cannot create/open " + pathname );
			System.exit(1); //quit the program
		}

		PrintWriter output = new PrintWriter(writer);
		output.print(ch);
		output.close();
	}
}

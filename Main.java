/**********************************************************
* Title:	 		Choose Your Destiny
* Filename:			Main.java
* Author:  			Sebastian Desaulniers
* Teacher:  		Mr. Ostropolec
* Course:			ICS3U
* Due Date: 		End of Unit 3
* Description:		fun game I guess
* Creation date:	23/10/2017 
* Notes:
*		- have fun !!
***********************************************************/

import java.util.Scanner; // for input
import java.io.*; // for files
import info.debatty.java.stringsimilarity.Cosine; // Cosine similarity algorithm

import java.util.*; // ArrayList, HashMap, Map, List

public class Main
{
	// the player's name
	private static String name;
	// for autocorrect
	private static int index = 0;

	// for user input
	private static Scanner input = null;

	// needed for the previous string as input
	private static String previousString = "";

	// needed for cosine similarity
	private static Cosine cos = new Cosine();

	// for storing data, such as strings from the file
	private static List<Map<String, String>> map = new ArrayList<>();
	private static Map<String, String> strings = new HashMap<String, String>();

	// used for dialog retrieval
	private static void sleep(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}
	private static void print(String string)
	{
		if (string.isEmpty())
			return;

		while (string.contains("{NAME}"))
			string = string.replace("{NAME}", name);
		while (string.contains("{NEWLINE}"))
			string = string.replace("{NEWLINE}", "\n");

		for (char c : string.toCharArray())
		{
			System.out.print(c);
			int amt = 30;
			if (c == ',')
				amt += 280;
			else if (c == '.' || c == '!' || c == '?' || c == ';')
				amt += 500;

			sleep(amt);
		}
	}

	private static double similarity(String str1, String str2)
	{
		return cos.similarity(str1, str2);
	}

	private static String getChoice(String in)
	{
		if (in.isEmpty())
			return null;
		print("{NEWLINE}: ");
		String choice = input.nextLine();
		String newChoice = "";

		// System.out.println(in);
		double sim = 0;
		int i = 0, max = -1, ind = 0, bind = 0;;
		// while (sim < 0.	5)
		for (Map<String, String> h : map)
		{
			i = 0;
			for (String s : h.keySet())
			{
				i++;
				// if (i != index - 1 && i != index && i != index + 1)
					// continue;

				sim = similarity(choice, s);
				// System.out.println(sim);
				// System.out.println(choice);
				// System.out.println(s);

				if (sim > max && sim > 0.5)
				{
					max = i;
					newChoice = s;
					ind = bind;
				}
			}
			bind++;
		}
		if (max == -1)
			return null;

		// System.out.println(map.get(max).get(newChoice));
		// System.out.println(max);
		// return ind;
		return map.get(ind).get(newChoice);
	}
	
	private static void story(String stringInput) // Doing it recursively for fun
	{
		/* the redo input */
		if (stringInput.equals("playAgain"))
		{
			print("Would you like to play again?");
			String choice = input.nextLine().toLowerCase();
			if (choice.equals("yes"))
				story("intro");
			else
				return;
		}

		print(strings.get(stringInput));
		String res = getChoice(stringInput);
		while (res == null)
		{
			print("invalid");
			res = getChoice(stringInput);
		}
		index++;
		story(res);
	}
	private static void parseLine(String line)
	{
		if (line.isEmpty())
			return;
		char c = line.charAt(0);
		if (c != '#' && c != '\t')
			return;
		else if (c == '\t')
		{
			while (line.contains("\"")) // remove all quotes
			line = line.replace("\"", "");
			strings.put(previousString, line.substring(1));
		}
		else if (c == '#')
		{
			Map<String, String> mp = new HashMap<String, String>();
			String tmp = line.substring(2, line.indexOf(':'));
			previousString = tmp;
			String[] func;
			// mp.put(tmp, previousString);
			// map.add(mp);

			for (int i = 0; i < 2; i++)
			{
				func = line.substring(line.indexOf('[') + 1, line.indexOf(']')).split(",");
				for (String choice : func)
				{
					String bracket = line.substring(line.indexOf('(') + 1, line.indexOf(')')).trim();
					choice = choice.trim();
					mp = new HashMap<String, String>();
					mp.put(choice, bracket);
					// System.out.println("Choice: " + choice);
					map.add(mp);
				}
				line = line.substring(line.indexOf('|') + 1);
			}
		}
	}

	private static void loadFile(String filename)
	{
		Scanner file = null;
		try
		{
			file = new Scanner(new File(filename));
			while (file.hasNextLine())
				parseLine(file.nextLine());
		}	
		catch (FileNotFoundException e)
		{
			print("File not found.");
		}
		finally
		{
			file.close();
		}
	}

	private static void initialize()
	{
		// Get the user's name
		loadFile("story.txt");

		print("Enter your name: ");
		name = input.nextLine().trim();

		// Call the custom print method
		// print("Have fun playing my game. Oh, and try not to die, {NAME}.\n");
		story("intro");
	}

	private static void game()
	{
		input = new Scanner(System.in);

		initialize();

		input.close();
	}

	public static void main(String[] args)
	{
		game();
	}
}
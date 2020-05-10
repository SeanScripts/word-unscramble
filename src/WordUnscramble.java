import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class WordUnscramble {
	public static void main(String[] args) {
		System.out.println("Letters?");
		Scanner s = new Scanner(System.in);
		String lets = s.nextLine();
		s.close();
		new WordUnscramble(lets);
	}
	
	String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String letters;
	int size;
	ArrayList<String> results;
	ArrayList<String> wordlist;
	
	public WordUnscramble(String letters) {
		this.letters = letters.toUpperCase();
		size = letters.length();
		results = new ArrayList<String>();
		System.out.println("Loading...");
		getWordList();
		System.out.println("Running...");
		run();
		System.out.println("Done.");
		System.out.println("Found "+results.size()+" results.");
		results.sort(null);
		for (String res : results) {
			System.out.println(res);
		}
	}
	
	public void getWordList() {
		wordlist = new ArrayList<String>();
		try {
			File file = new File("wordlist.txt");
			Scanner ws = new Scanner(file);
			while (ws.hasNextLine()) {
				String word = ws.nextLine();
				if (word.length() <= size) {
					wordlist.add(word);
				}
			}
			ws.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println("Wordlist has "+wordlist.size()+" words of length at most "+size+".");
	}
	
	public void run() {
		//Goal: Find all words that can be made from these letters, without repeats
		
		//Simplify dictionary to optimize search
		for (int i = 0; i < size; i++) {
			String letter = letters.substring(i, i+1);
			//Remove from alphabet
			if (alphabet.contains(letter)) {
				alphabet = alphabet.replace(letter, "");
			}
			//Remove any words that don't have these letters
			/*
			for (int j = wordlist.size()-1; j >= 0; j--) {
				if (!wordlist.get(j).contains(letter)) {
					wordlist.remove(j);
				}
			}
			*/
		}
		//System.out.println(alphabet);
		for (int i = 0; i < alphabet.length(); i++) {
			for (int j = wordlist.size()-1; j >= 0; j--) {
				String letter = alphabet.substring(i, i+1);
				if (wordlist.get(j).contains(letter)) {
					wordlist.remove(j);
				}
			}
		}
		System.out.println("Reduced wordlist has "+wordlist.size()+" words.");
		/*
		for (String word : wordlist) {
			System.out.println(word);
		}
		*/
		for(Iterator<int[]> it = new PermIterator(size); it.hasNext(); ) {
			String target = PermIterator.print(letters, it.next());
			for (String word : wordlist) {
				if (target.contains(word)) {
					boolean valid = true;
					for (int i = 0; i < results.size(); i++) {
						if (results.get(i).equals(word)) {
							valid = false;
						}
					}
					if (valid) {
						results.add(word);
					}
				}
			}
		}
	}
}

package net.davekirkwood.wordlesolver.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.davekirkwood.wordlesolver.data.Result.Element;

public class Dictionary {
	
	private static final String CANDIDATES_FILE = "resources/candidates.txt";
	private static final String ALLWORDS_FILE = "resources/allwords.txt";
	
	List<String> candidates = new ArrayList<>();
	List<String> allWords = new ArrayList<>();
	
	private int treeDepth = 0;
	
	public Dictionary() {
		readFile(new File(CANDIDATES_FILE), candidates);
		readFile(new File(ALLWORDS_FILE), allWords);
	}
	
	private void readFile(File file, List<String> list) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			reader.lines().forEach(s -> {
				String[] words = s.split(",");
				for(String word : words) {
					list.add(word.replaceAll("\"", ""));
				}
			});
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public List<String> getCandidates() {
		return candidates;
	}

	/**
	 * @return the treeDepth
	 */
	public int getTreeDepth() {
		return treeDepth;
	}

	/**
	 * Gets the Wordle result for the attempted word if the correct
	 * answer is answer.
	 * @param attempt The attempted word.
	 * @param answer The answer to test against.
	 * @return
	 */
	private Result checkWord(String attempt, String answer) {
		Element[] resultElements = new Element[5];
		
		List<Character> misses = new ArrayList<>();
		for(int i=0; i<5; i++) {
			char c = attempt.charAt(i);
			if(answer.charAt(i) == c) {
				resultElements[i] = Element.HIT;
			} else {
				misses.add(answer.charAt(i));
				resultElements[i] = Element.MISS;
			}
		}
		for(Character miss : misses) {
			for(int i=0; i<5; i++) {
				if(resultElements[i] == Element.MISS) {
					if(attempt.charAt(i) == miss) {
						resultElements[i] = Element.LETTER;
						break;
					}
				}
			}
		}
		return new Result(resultElements);
	}
	
	/**
	 * Find the word that has the shallowest tree in the remaining set of words.
	 * @return
	 */
	public String getWord() {

		int minMaxTreeDepth = Integer.MAX_VALUE;
		String bestWord = null;
		
		for(String word : allWords) {
			
			Map<Result, Integer> treeDepths = new HashMap<>();
			int maxTreeDepth = 0;
			
			for(String candidate : candidates) {
				
				Result r = checkWord(word, candidate);
				
				if(treeDepths.containsKey(r)) {
					treeDepths.put(r, treeDepths.get(r) + 1);
				} else {
					treeDepths.put(r, 1);
				}
				
				maxTreeDepth = Math.max(maxTreeDepth, treeDepths.get(r));
				
			}
			
			if(maxTreeDepth < minMaxTreeDepth || (maxTreeDepth == minMaxTreeDepth && candidates.contains(word))) {
				minMaxTreeDepth = maxTreeDepth;
				bestWord = word;
			}
			
		}
		
		treeDepth = minMaxTreeDepth;
		return bestWord;
		
	}

	/**
	 * Remove all words from the list that do not match the specified result.
	 * @param result
	 */
	public void applyResult(String attemptedWord, Result result) {

		List<String> wordsToDelete = new ArrayList<>();
		
		for(String candidate : candidates) {
			if(!checkWord(attemptedWord, candidate).equals(result)) {
				wordsToDelete.add(candidate);
			}
		}
		
		for(String wordToDelete : wordsToDelete) {
			candidates.remove(wordToDelete);
		}
		
	}
	
	
}

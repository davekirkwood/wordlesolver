package net.davekirkwood.wordlesolver;

import net.davekirkwood.wordlesolver.data.Dictionary;
import net.davekirkwood.wordlesolver.data.Result;
import net.davekirkwood.wordlesolver.ui.WordleSolverUI;

public class WordleSolver {

	private WordleSolverUI ui;
	private Dictionary dictionary;
	
	private String currentWord = null;
	
	public WordleSolver() {
		ui = new WordleSolverUI(this);
		dictionary = new Dictionary();
		attempt();
	}
	
	public void attempt() {
		currentWord = dictionary.getWord();
		ui.attemptWord(currentWord, dictionary.getCandidates().size(), dictionary.getTreeDepth());
	}

	public void processResult(Result result) {
		dictionary.applyResult(currentWord, result);
		attempt();	
	}
	
	public static void main(String[] args) {
		new WordleSolver();
	}
	
}

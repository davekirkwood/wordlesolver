package net.davekirkwood.wordlesolver.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.davekirkwood.wordlesolver.WordleSolver;
import net.davekirkwood.wordlesolver.data.Result;
import net.davekirkwood.wordlesolver.data.Result.Element;

public class WordleSolverUI extends JFrame {

	private static Color colourMiss = Color.BLACK;
	private static Color colourLetter = Color.ORANGE;
	private static Color colourHit = Color.GREEN;

	private JButton[] letters;
	
	private JButton submitButton = new JButton("Submit");
	
	private JLabel candidatesLabel = new JLabel();
	private JLabel treeDepthLabel = new JLabel();
		
	private int candidates;
	
	public WordleSolverUI(WordleSolver solver) {
		
		this.setLayout(new GridLayout(2, 5));
		this.setSize(500, 200);
		this.setVisible(true);
	
		letters = new JButton[5];
		for(int i=0; i<5; i++) {
			letters[i] = new JButton();
			letters[i].setFont(new Font("Dialog", Font.BOLD, 45));
			letters[i].setForeground(Color.WHITE);
			this.add(letters[i]);
			letters[i].addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if(candidates > 1) {
						JButton button = (JButton)e.getSource();
						if(button.getBackground() == colourMiss) {
							button.setBackground(colourLetter);
						} else if(button.getBackground() == colourLetter) {
							button.setBackground(colourHit);
						} else if(button.getBackground() == colourHit) {
							button.setBackground(colourMiss);
						}
					}
				}
				
			});
		}
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						solver.processResult(new Result(
								new Element[] {
										letters[0].getBackground() == colourHit ? Element.HIT : letters[0].getBackground() == colourLetter ? Element.LETTER : Element.MISS,
										letters[1].getBackground() == colourHit ? Element.HIT : letters[1].getBackground() == colourLetter ? Element.LETTER : Element.MISS,
										letters[2].getBackground() == colourHit ? Element.HIT : letters[2].getBackground() == colourLetter ? Element.LETTER : Element.MISS,
										letters[3].getBackground() == colourHit ? Element.HIT : letters[3].getBackground() == colourLetter ? Element.LETTER : Element.MISS,
										letters[4].getBackground() == colourHit ? Element.HIT : letters[4].getBackground() == colourLetter ? Element.LETTER : Element.MISS
									}								
								));
					}
					
				}).start();
				
			}
			
		});
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		
		this.add(submitButton);
		this.add(new JPanel());
		this.add(treeDepthLabel);
		this.add(new JPanel());
		this.add(candidatesLabel);
		
		
	}
		
	public void attemptWord(String word, int candidates, int treeDepth) {
		
		this.candidates = candidates;
		submitButton.setEnabled(candidates > 1);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				candidatesLabel.setText(candidates + " words.");
				treeDepthLabel.setText("Tree depth: " + treeDepth);
				for(int i=0; i<5; i++) {
					letters[i].setBackground(candidates == 1 ? colourHit : colourMiss);
					letters[i].setText(word.substring(i, i+1));
				}
			}
		});
		
	}
	
	
	
}

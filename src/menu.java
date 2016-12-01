import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

public abstract class menu {
	private int choiceNum;
	private String choicePrompt;
	public ArrayList<choice> choiceList = new ArrayList<choice>();
	
	
	public void addChoices(choice c) {
		this.choiceList.add(c);
	}
	
	// Prompt the user to choose a choice
	public void displayChoices() {
		System.out.println("Please choose an option (Enter number)");
		for(Iterator<choice> c = choiceList.iterator(); c.hasNext(); ) {
			choice current = c.next();
			System.out.println(current.choiceNum + ". " + current.choicePrompt);
		}
	}
	
	// Get user choice
	public choice getResponse() {
		Scanner s = new Scanner(System.in);

		String resp = s.nextLine();
		for (int i = 0; i < this.choiceList.size(); i++) {
			if (Integer.toString(this.choiceList.get(i).choiceNum).equals(resp)) {
				return this.choiceList.get(i);
			}
		}
		return null;
	}
}
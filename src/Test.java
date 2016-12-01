import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Test extends Survey{
	
	ArrayList<Question> questions = new ArrayList();
	
	public Test() {
		
	}
	
	// Create questions for test
	public ArrayList<Question> Create() {
		QuestionMenu qm = new QuestionMenu(this.questions, "Test");
		return this.questions;
	}
	
	public ArrayList<Question> Modify(ArrayList<Question> questions) {
		System.out.println("Enter the question that you want to modify (Enter the prompt exactly): ");
		Scanner s = new Scanner(System.in);
		String modify = s.nextLine();
		for (int i = 0; i < questions.size(); i++) {
			if (questions.get(i).prompt.equals(modify)) {
				questions.get(i).modify("Test");
			}
		}
		return questions;
	}
	
	public void Grade(ArrayList<response> responses) {
		File folder = new File("./src/tests/");
		File[] listOfFiles = folder.listFiles();
		// prompt user to enter file to load
		System.out.println("Please choose a test to grade (Enter the name of the file w/ extension): ");
		for (int i = 0; i < listOfFiles.length; i++) {
			System.out.println(listOfFiles[i].getName());
		}
		Scanner s = new Scanner(System.in);
		String filename = s.nextLine();
		File f = new File("./src/tests/" + filename);
		if (!f.exists()) { // Cannot find file that user entered
			System.out.println("The file " + filename + " does not exist. Please start over!");
			return;
		}
		
		// Deserialize correct responses from test file
		ArrayList<response> correctResponses = getAnswers(f);
		if (correctResponses == null) {
			System.out.println("Something went wrong! Please start over.");
			return;
		}
		
		int correct = 0;
		int total = 0;
		for (int i = 0; i < correctResponses.size(); i++) {
			if (correctResponses.get(i).getClass().toString().equals("class hashMapResponse")) {
				hashMapResponse correctHMR = (hashMapResponse)correctResponses.get(i);
				hashMapResponse userHMR = (hashMapResponse)responses.get(i);
				
				if (userHMR.value.equals(correctHMR.value))
					correct = correct + 1;
			}
			else {
				stringresponse correctSR = (stringresponse)correctResponses.get(i);
				stringresponse userSR = (stringresponse)responses.get(i);
				
				if (userSR.value.equals(correctSR.value))
					correct = correct + 1;
			}
			total = total + 1;
		}
		
		System.out.println("You got " + correct + " out of " + total + " correct.");
	}
	
	public ArrayList<response> getAnswers(File f) {
		ArrayList<response> correctResponses = new ArrayList<response>();
		
		try {
			// Create document builder factory for file
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.parse(f);
			Element Questions = doc.getDocumentElement();
			NodeList allQuestions = Questions.getElementsByTagName("question");
			
			for (int i = 0; i < allQuestions.getLength(); i++) { 
				Element question = (Element)allQuestions.item(i);
				if (question.getAttribute("type").equals("R")) { // Rank question
					HashMap<String, String> c = new HashMap<String, String>();
					Element choices = (Element)question.getElementsByTagName("choices").item(0);
					NodeList allChoices = choices.getElementsByTagName("choice");
					for (int x = 0; x < allChoices.getLength(); x++) {
						Element option = (Element)allChoices.item(x);
						Element r = (Element)option.getElementsByTagName("rank").item(0);
						String rank = r.getTextContent();
						String choice = ((Element)allChoices.item(x)).getTextContent();
						c.put(rank, choice);
					}
					hashMapResponse hmr = new hashMapResponse(c);
					correctResponses.add(hmr);
				}
				else if (question.getAttribute("type").equals("M")) { // Matching question
					HashMap<String, String> c = new HashMap<String, String>();
					Element choices = (Element)question.getElementsByTagName("choices").item(0);
					NodeList allChoices = choices.getElementsByTagName("choice");
					for (int x = 0; x < allChoices.getLength(); x++) {
						Element option = (Element)allChoices.item(x);
						Element l = (Element)option.getElementsByTagName("letter").item(0);
						String letter = l.getTextContent();
						String choice = ((Element)allChoices.item(x)).getTextContent();
						c.put(letter, choice);
					}
					hashMapResponse hmr = new hashMapResponse(c);
					correctResponses.add(hmr);
				}
				else { // All other questions
					if (!question.getAttribute("type").equals("E")) { // Ignore essay questions
						String answer = ((Element)question.getElementsByTagName("answer").item(0)).getTextContent();
						stringresponse sr = new stringresponse(answer);
						correctResponses.add(sr);
					}
				}
			}
		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		
		return correctResponses;
	}
	
}
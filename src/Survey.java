import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Survey {
	public String current;
	
	ArrayList<Question> questions = new ArrayList();
	
	public Survey() {
		
	}
	
	// Create new survey
	public ArrayList<Question> Create() {
		QuestionMenu qm = new QuestionMenu(this.questions, "Survey");
		return this.questions;
	}
	
	// Display all questions on test or survey
	public void Display(ArrayList<Question> questions) {
		String outputType = "";
		while (!outputType.equals("text") && !outputType.equals("speech")) {
			System.out.println("Do you want to display using text or speech? (Type 'text' or 'speech'): ");
			Scanner s = new Scanner(System.in);
			outputType = s.nextLine();
		}
		
		if (outputType.equals("text")) {
			consoleIO cio = new consoleIO();
			
			for (int i = 0; i < questions.size(); i++) {
				questions.get(i).display(cio, "display");
			}
		}
		else {
			speechIO sio = new speechIO("kevin");
			
			for (int i = 0; i < questions.size(); i++) {
				questions.get(i).display(sio, "display");
			}
		}
	}
	
	public ArrayList<Question> Modify(ArrayList<Question> questions) {
		System.out.println("Enter the question that you want to modify (Enter the prompt exactly): ");
		Scanner s = new Scanner(System.in);
		String modify = s.nextLine();
		for (int i = 0; i < questions.size(); i++) {
			if (questions.get(i).prompt.equals(modify)) {
				questions.get(i).modify("Survey");
			}
		}
		return questions;
	}
	
	public ArrayList<Question> Load(String type) {
		File folder;
		if (type.equals("Survey"))
			folder = new File("./src/surveys/");
		else
			folder = new File("./src/tests/");
		File[] listOfFiles = folder.listFiles();
		// prompt user to enter file to load
		System.out.println("Please choose a file to load (Enter the name of the file w/ extension): ");
		for (int i = 0; i < listOfFiles.length; i++) {
			System.out.println(listOfFiles[i].getName());
		}
		Scanner s = new Scanner(System.in);
		String filename = s.nextLine();
		File f;
		if (type.equals("Survey")) {
			f = new File("./src/surveys/" + filename);
		}
		else {
			f = new File("./src/tests/" + filename);
		}
		
		if (!f.exists()) {
			System.out.println("That file doesn't seem to exist!");
			return null;
		}
		
		try {
			// Create document builder factory for file
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.parse(f);
			// Deserialize all questions based on type
			deserializeTF(questions, doc, this.getClass().toString());
			deserializeMC(questions, doc, this.getClass().toString());
			deserializeSA(questions, doc, this.getClass().toString());
			deserializeEssay(questions, doc, this.getClass().toString());
			deserializeRank(questions, doc, this.getClass().toString());
			deserializeMatch(questions, doc, this.getClass().toString());

		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		current = filename;
		return questions;
	}
	
	public void Save(ArrayList<Question> questions, String type) {
		// prompt user to name file
		System.out.println("What would you like to name the file? If you are modifying a file then re-type the name of the file (enter name w/o extension): ");
		Scanner s = new Scanner(System.in);
		String filename = s.nextLine();
		try {
			// Create document builder factory
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
		 
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("questions");
			doc.appendChild(rootElement);
			
			// loop through each question and serialize it to document
			for (int i = 0; i < questions.size(); i++) {
				questions.get(i).serialize(doc);
			}
			
			// create xml document
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			File f;
			if (type.equals("Survey")) {
				f = new File("./src/surveys/" + filename + ".xml");
			}
			else {
				f = new File("./src/tests/" + filename + ".xml");
			}
			StreamResult result = new StreamResult(f);

			// save document
			transformer.transform(source, result);
		}	
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<response> Take(ArrayList<Question> questions) {
		ArrayList<response> responses = new ArrayList<response>();
		
		String outputType = "";
		while (!outputType.equals("text") && !outputType.equals("speech")) {
			System.out.println("Do you want to display using text or speech? (Type 'text' or 'speech'): ");
			Scanner s = new Scanner(System.in);
			outputType = s.nextLine();
		}
		
		for (int i = 0; i < questions.size(); i++) {
			if (outputType.equals("text")) {
				consoleIO cio = new consoleIO();
				questions.get(i).display(cio, "take");
			}
			else {
				speechIO sio = new speechIO("kevin");
				questions.get(i).display(sio, "take");
			}
			
			if (questions.get(i).getClass().toString().equals("class Rank") || questions.get(i).getClass().toString().equals("class Matching")) {
				HashMap<String, String> RanksMatches = new HashMap<String, String>();
				
				String response1 = "";
				String response2 = "";
				
				if (questions.get(i).getClass().toString().equals("class Rank")) {
					while (!response1.equals("done") && !response2.equals("done")) {
						System.out.println("Enter rank 'done' if done: ");
						Scanner s = new Scanner(System.in);
						response1 = s.nextLine();
						if (response1.equals("done"))
							break;
						System.out.println("Enter option 'done' if done: ");
						Scanner s2 = new Scanner(System.in);
						response2 = s2.nextLine();
						if (response2.equals("done"))
							break;
						if (!response1.equals("done") && !response2.equals("done"))
							RanksMatches.put(response1, response2);
					}
					hashMapResponse hmr = new hashMapResponse(RanksMatches);
					responses.add(hmr);
				}
				else {
					while (!response1.equals("done") && !response2.equals("done")) {
						System.out.println("Enter match1 or 'done' if done: ");
						Scanner s = new Scanner(System.in);
						response1 = s.nextLine();
						if (response1.equals("done"))
							break;
						System.out.println("Enter match2 or 'done' if done: ");
						Scanner s2 = new Scanner(System.in);
						response2 = s2.nextLine();
						if (response2.equals("done"))
							break;
						if (!response1.equals("done") && !response2.equals("done"))
							RanksMatches.put(response1, response2);
					}
					hashMapResponse hmr = new hashMapResponse(RanksMatches);
					responses.add(hmr);
				}
			}
			else {
				if (questions.get(i).getClass().toString().equals("class TrueFalse"))
					System.out.println("Enter your response ('true' or 'false'): ");
				else if (questions.get(i).getClass().toString().equals("class MC"))
					System.out.println("Enter your response (enter integer): ");
				else 
					System.out.println("Enter your response: ");
				
				Scanner s = new Scanner(System.in);
				String responseValue = s.nextLine();
				stringresponse sr = new stringresponse(responseValue);
				if (!questions.get(i).getClass().toString().equals("class Essay"))
					responses.add(sr);
			}
		}
		
		return responses;
	}
	
	public void SaveResponses(ArrayList<response> responses, String current) {
		try {
			// Create document builder factory
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
		 
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("responses");
			doc.appendChild(rootElement);
			for (int i = 0 ; i < responses.size(); i++) {
				if (responses.get(i).getClass().toString().equals("class hashMapResponse")) {
					Element response = doc.createElement("response");
					response.setAttribute("type", "HashMap");
					hashMapResponse hmr = (hashMapResponse)responses.get(i);
					Iterator it = hmr.value.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pair = (Map.Entry)it.next();
				        Element choice = doc.createElement("choice");
				        choice.setAttribute("rank", pair.getKey().toString());
				        choice.setTextContent(pair.getValue().toString());
				        response.appendChild(choice);
					}
					rootElement.appendChild(response);
				}
				else {
					Element response = doc.createElement("response");
					response.setAttribute("type", "String");
					stringresponse sr = (stringresponse)responses.get(i);
					Element value = doc.createElement("value");
					value.setTextContent(sr.value);
					response.appendChild(value);
					rootElement.appendChild(response);
				}
			}
			System.out.println("What would you like to name the file to save the answers? (Enter name w/o extensions): ");
			Scanner s2 = new Scanner(System.in);
			String name = s2.nextLine();
			String filename = name + "_" + current;
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("./src/responses/" + filename));

			// save document
			transformer.transform(source, result);
			
		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Tabulate(String type) {
		File folder;
		if (type.equals("Survey"))
			folder = new File("./src/surveys/");
		else
			folder = new File("./src/tests");
		File[] listOfFiles = folder.listFiles();
		// prompt user to enter file to tabulate
		System.out.println("Please choose a file to tabulate (Enter the name of the file with extension): ");
		for (int i = 0; i < listOfFiles.length; i++) {
			System.out.println(listOfFiles[i].getName());
		}
		Scanner s = new Scanner(System.in);
		String name = s.nextLine();
		File answerFolder = new File("./src/responses/");
		File[] listOfAnswerFiles = answerFolder.listFiles();
		// Get all answer files to tabulate
		ArrayList<File> filesToTabulate = new ArrayList<File>();
		for (int i = 0; i < listOfAnswerFiles.length; i++) {
			if (listOfAnswerFiles[i].getName().endsWith(name)) {
				filesToTabulate.add(listOfAnswerFiles[i]);
			}
		}
		// No files wer found
		if (filesToTabulate.size() == 0) {
			System.out.println("No Files were found for " + name + " Please start over.");
			return;
		}
		
		ShowTabulationResults(filesToTabulate, name, type);
	}
	
	public void ShowTabulationResults(ArrayList<File> filesToTabulate, String testName, String type) {
		ArrayList<Question> questions = getQuestions(testName, type);
		ArrayList<ArrayList<response>> tabulations = new ArrayList<ArrayList<response>>();
		
		for (int i = 0; i < filesToTabulate.size(); i++) {
			ArrayList<response> responses = getResponses(filesToTabulate.get(i));
			tabulations.add(responses);
		}
		
		ArrayList<String> srStrings = new ArrayList<String>();
		ArrayList<HashMap<String, Integer>> allStrings = new ArrayList<HashMap<String, Integer>>();
		for (int i = 0; i < questions.size(); i++) {
			if (!questions.get(i).getClass().toString().equals("class Essay") && !questions.get(i).getClass().toString().equals("class Rank") && !questions.get(i).getClass().toString().equals("class Matching")) {
				HashMap<String, Integer> allStringResponses = new HashMap<String, Integer>();
				for (int x = 0; x < tabulations.size(); x++) {
					stringresponse sr = (stringresponse)tabulations.get(x).get(i);
					if (srStrings.contains(sr.value)) {
						allStringResponses.put(sr.value, allStringResponses.get(sr.value) + 1);
					}
					else {
						allStringResponses.put(sr.value, 1);
						srStrings.add(sr.value);
					}
				}
				allStrings.add(allStringResponses);
			}
		}
		
		ArrayList<HashMap<String, String>> hmrHashMaps = new ArrayList<HashMap<String, String>>();
		ArrayList<HashMap<HashMap<String, String>, Integer>> allHashes = new ArrayList<HashMap<HashMap<String, String>, Integer>>();
		for (int i = 0; i < questions.size(); i++) {
			if (questions.get(i).getClass().toString().equals("class Rank") || questions.get(i).getClass().toString().equals("class Matching")) {
				HashMap<HashMap<String, String>, Integer> allHashResponses = new HashMap<HashMap<String, String>,Integer>();
				for (int x = 0; x < tabulations.size(); x++) {
					if (tabulations.get(x).get(i).getClass().toString().equals("class hashMapResponse")) {
						hashMapResponse hmr = (hashMapResponse)tabulations.get(x).get(i);
						if (hmrHashMaps.contains(hmr.value)) {
							allHashResponses.put(hmr.value, allHashResponses.get(hmr.value) + 1);
						}
						else {
							allHashResponses.put(hmr.value, 1);
							hmrHashMaps.add(hmr.value);
						}
					}
				}
				allHashes.add(allHashResponses);
			}
		}
		
		// Separate questions with string responses and questions with HashMap responses
		ArrayList<Question> stringQuestions = new ArrayList<Question>();
		ArrayList<Question> hashQuestions = new ArrayList<Question>();
		for (int i = 0; i < questions.size(); i++) {
			if (questions.get(i).getClass().toString().equals("class Rank") || questions.get(i).getClass().toString().equals("class Matching"))
				hashQuestions.add(questions.get(i));
			else
				stringQuestions.add(questions.get(i));
		}
		
		consoleIO cio = new consoleIO();
		
		// Tabulate string questions
		for (int i = 0; i < stringQuestions.size(); i++) {
			System.out.println("QUESTION: ");
			stringQuestions.get(i).display(cio, "tabulate");
			System.out.println("\n");
			
			System.out.println("REPLIES: ");
			HashMap<String, Integer> allStringResponses = allStrings.get(i);
			Iterator it = allStringResponses.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				System.out.println(pair.getKey());
			}
			System.out.println("\n");
			
			System.out.println("TABULATION: ");
			Iterator it2 = allStringResponses.entrySet().iterator();
			while (it2.hasNext()) {
				Map.Entry pair2 = (Map.Entry)it2.next();
				System.out.println(pair2.getKey() + ": " + pair2.getValue());
			}
			System.out.println("---------------------------------");
		}
		
		// Tabulate HashMap questions
		for (int i = 0; i < hashQuestions.size(); i++) {
			System.out.println("QUESTION: ");
			hashQuestions.get(i).display(cio, "tabulate");
			System.out.println("\n");
			
			System.out.println("REPLIES: ");
			HashMap<HashMap<String, String>, Integer> allHashResponses = allHashes.get(i);
			Iterator it = allHashResponses.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				HashMap<String, String> hashResponse = (HashMap<String, String>)pair.getKey();
				Iterator it2 = hashResponse.entrySet().iterator();
				while (it2.hasNext()) {
					Map.Entry pair2 = (Map.Entry)it2.next();
					System.out.println(pair2.getKey() + " " + pair2.getValue());
				}
				System.out.println("\n");
			}
			
			System.out.println("TABULATION: ");
			Iterator it3 = allHashResponses.entrySet().iterator();
			while (it3.hasNext()) {
				Map.Entry pair = (Map.Entry)it3.next();
				System.out.println(pair.getValue() + ": ");
				HashMap<String, String> hashResponse = (HashMap<String, String>)pair.getKey();
				Iterator it2 = hashResponse.entrySet().iterator();
				while (it2.hasNext()) {
					Map.Entry pair2 = (Map.Entry)it2.next();
					System.out.println(pair2.getKey() + " " + pair2.getValue());
				}
				System.out.println("\n");
				
			}
			System.out.println("---------------------------------");
		}
	}
	
	public ArrayList<response> getResponses(File f) {
		ArrayList<response> responses = new ArrayList<response>();
		
		try {
			// Create document builder factory for file
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.parse(f);
			Element r = doc.getDocumentElement();
			NodeList allResponses = r.getElementsByTagName("response");
			for (int i = 0; i < allResponses.getLength(); i++) {
				if (allResponses.item(i).getAttributes().getNamedItem("type").getNodeValue().equals("HashMap")) {
					NodeList allChoices = ((Element)allResponses.item(i)).getElementsByTagName("choice");
					HashMap<String, String> ac = new HashMap<String, String>();
					for (int x = 0; x < allChoices.getLength(); x++) {
						String rank = ((Element)allChoices.item(x)).getAttributes().getNamedItem("rank").getNodeValue();
						String value = ((Element)allChoices.item(x)).getTextContent();
						ac.put(rank, value);
					}
					hashMapResponse hmr = new hashMapResponse(ac);
					responses.add(hmr);
				}
				else {
					String resp = ((Element)allResponses.item(i)).getElementsByTagName("value").item(0).getTextContent();
					stringresponse sr = new stringresponse(resp);
					responses.add(sr);
				}
			}

		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responses;
	}
	
	public ArrayList<Question> getQuestions(String filename, String type) {
		File f;
		if (type.equals("Survey")) {
			f = new File("./src/surveys/" + filename);
		}
		else {
			f = new File("./src/tests/" + filename);
		}
		try {
			// Create document builder factory for file
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.parse(f);
			// Deserialize all questions based on type
			deserializeTF(questions, doc, this.getClass().toString());
			deserializeMC(questions, doc, this.getClass().toString());
			deserializeSA(questions, doc, this.getClass().toString());
			deserializeRank(questions, doc, this.getClass().toString());
			deserializeMatch(questions, doc, this.getClass().toString());

		}
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return questions;
	}
	
	public void deserializeTF(ArrayList<Question> questions, Document doc, String type) {
		Element Questions = doc.getDocumentElement();
		NodeList allQuestions = Questions.getElementsByTagName("question");
		// loop through all xml questions and create true false questions
		for (int i = 0; i < allQuestions.getLength(); i++) {
			Element question = (Element)allQuestions.item(i);
			if (question.getAttribute("type").equals("TF")) {
				String prompt = question.getElementsByTagName("prompt").item(0).getTextContent();
				String correctResponse = "";
				if (type.equals("class Test")) { // save correct choice if this is test
					correctResponse = question.getElementsByTagName("answer").item(0).getTextContent();
				}
				// create new true false question and add to questions arraylist 
				TrueFalse tf = new TrueFalse(prompt, type, correctResponse);
				questions.add(tf);
			}
		}
		
	}
	
	public void deserializeMC(ArrayList<Question> questions, Document doc, String type) {
		Element Questions = doc.getDocumentElement();
		NodeList allQuestions = Questions.getElementsByTagName("question");
		// loop through all xml questions and create multiple choice questions
		for (int i = 0; i < allQuestions.getLength(); i++) {
			Element question = (Element)allQuestions.item(i);
			if (question.getAttribute("type").equals("MC")) { 
				String prompt = question.getElementsByTagName("prompt").item(0).getTextContent();
				ArrayList<String> choices = new ArrayList<String>();
				Element choicesNode = (Element)question.getElementsByTagName("choices").item(0);
				NodeList allChoices = choicesNode.getElementsByTagName("choice");
				for (int x = 0; x < allChoices.getLength(); x++) {
					Element choice = (Element)allChoices.item(x);
					String c = choice.getTextContent();
					choices.add(c);
				}
				int correctResponse = 0;
				if (type.equals("class Test")) { // save correct choice if this is test
					correctResponse = Integer.parseInt(question.getElementsByTagName("answer").item(0).getTextContent());
				}
				// create new multiple choice question and add to questions arraylist
				MC mc = new MC(prompt, choices, type, correctResponse);
				questions.add(mc);
			}
		}
	}
	
	public void deserializeSA(ArrayList<Question> questions, Document doc, String type) {
		Element Questions = doc.getDocumentElement();
		NodeList allQuestions = Questions.getElementsByTagName("question");
		// loop through xml questions and save all short answer questions
		for (int i = 0; i < allQuestions.getLength(); i++) {
			Element question = (Element)allQuestions.item(i);
			if (question.getAttribute("type").equals("SA")) {
				String prompt = question.getElementsByTagName("prompt").item(0).getTextContent();
				String correctResponse = "";
				if (type.equals("class Test")) { // save correct answer if this is a test
					correctResponse = question.getElementsByTagName("answer").item(0).getTextContent();
				}
				// create new short answer and add to questions array list
				ShortAnswer sa = new ShortAnswer(prompt, type, correctResponse);
				questions.add(sa);
			}
		}
	}
	
	public void deserializeEssay(ArrayList<Question> questions, Document doc, String type) {
		Element Questions = doc.getDocumentElement();
		NodeList allQuestions = Questions.getElementsByTagName("question");
		// loop through all questions and save all essay questions
		for (int i = 0; i < allQuestions.getLength(); i++) {
			Element question = (Element)allQuestions.item(i);
			if (question.getAttribute("type").equals("E")) {
				String prompt = question.getElementsByTagName("prompt").item(0).getTextContent();
				Essay e = new Essay(prompt);
				questions.add(e);
			}
		}
	}
	
	public void deserializeRank(ArrayList<Question> questions, Document doc, String type) {
		Element Questions = doc.getDocumentElement();
		NodeList allQuestions = Questions.getElementsByTagName("question");
		// loop through all questions and save all ranking questions
		for (int i = 0; i < allQuestions.getLength(); i++) {
			Element question = (Element)allQuestions.item(i);
			if (question.getAttribute("type").equals("R")) {
				String prompt = question.getElementsByTagName("prompt").item(0).getTextContent();
				HashMap<Integer, String> ranks = new HashMap<Integer, String>();
				int correctResponse = 0;
				Element choicesNode = (Element)question.getElementsByTagName("choices").item(0);
				NodeList allChoices = choicesNode.getElementsByTagName("choice");
				for (int x = 0; x < allChoices.getLength(); x++) {
					Element choice = (Element)allChoices.item(x);
					String c = getFirstLevelTextContent(choice);
					if (type.equals("class Test")) { // save correct rank if this is a test
						correctResponse = Integer.parseInt(choice.getElementsByTagName("rank").item(0).getTextContent());
					}
					ranks.put(correctResponse, c);
				}
				// Create new ranking question and add to questions
				Rank rank = new Rank(prompt, ranks, type);
				questions.add(rank);
			}
		}
	}
	
	public static String getFirstLevelTextContent(Node node) {
	    NodeList list = node.getChildNodes();
	    StringBuilder textContent = new StringBuilder();
	    for (int i = 0; i < list.getLength(); ++i) {
	        Node child = list.item(i);
	        if (child.getNodeType() == Node.TEXT_NODE)
	            textContent.append(child.getTextContent());
	    }
	    return textContent.toString();
	}
	
	public void deserializeMatch(ArrayList<Question> questions, Document doc, String type) {
		Element Questions = doc.getDocumentElement();
		NodeList allQuestions = Questions.getElementsByTagName("question");
		// loop through all questions and save all matching questions
		for (int i = 0; i < allQuestions.getLength(); i++) {
			Element question = (Element)allQuestions.item(i);
			if (question.getAttribute("type").equals("M")) {
				String prompt = question.getElementsByTagName("prompt").item(0).getTextContent();
				HashMap<String, String> matches = new HashMap<String, String>();
				String letter = "";
				Element choicesNode = (Element)question.getElementsByTagName("choices").item(0);
				NodeList allChoices = choicesNode.getElementsByTagName("choice");
				for (int x = 0; x < allChoices.getLength(); x++) {
					Element choice = (Element)allChoices.item(x);
					String c = getFirstLevelTextContent(choice);
					letter = choice.getElementsByTagName("letter").item(0).getTextContent();
					matches.put(letter, c);
				}
				// Create new multiple choice and add to questions
				Matching mc = new Matching(prompt, matches, type);
				questions.add(mc);
			}
		}
	}
}
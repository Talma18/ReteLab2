package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.base.types.Event;
import org.yakindu.base.types.Property;
import org.yakindu.sct.model.sgraph.Scope;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		ArrayList<String> variables = new ArrayList<String>();
		ArrayList<String> events = new ArrayList<String>();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof Scope) {
				Scope scope = (Scope) content;
				for (Event e: scope.getEvents()) {
					events.add(e.getName());
				}
				for (Property v: scope.getVariables()) {
					variables.add(v.getName());
				}
				
			}	
		}
		System.out.println(
				"public class RunStatechart {\r\n" + 
				"	\r\n" + 
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		boolean exit = false;\r\n" + 
				"		while (!exit) {\r\n" + 
				"			String input = \"\";\r\n" + 
				"			int c = System.in.read();\r\n" + 
				"			while (c!=10) {\r\n" + 
				"				input+=(char)c;\r\n" + 
				"				c = System.in.read();\r\n" + 
				"			}\r\n" + 
				"			input = input.toLowerCase();\r\n" + 
				"			switch(input) {\r\n" + 
				"			case \"exit\":\r\n" + 
				"				exit = true;\r\n" + 
				"				break;");
		for (String name: events) {
			name = name.substring(0,1).toUpperCase() + name.substring(1);
			System.out.println(
					"			case \"" + name.toLowerCase() + "\":\r\n" + 
					"				s.raise" + name + "();\r\n" + 
					"				break;");
		}
		System.out.println(
				"			default:\r\n" + 
				"				System.out.println(\"Unrecognized command: \" + input);\r\n" + 
				"				break;\r\n" + 
				"			}\r\n" + 
				"			s.runCycle();\r\n" + 
				"			print(s);\r\n" + 
				"		}\r\n" + 
				"		System.exit(0);\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public static void print(IExampleStatemachine s) {");
		for (String name: variables) {
			name = name.substring(0,1).toUpperCase() + name.substring(1);
			System.out.println("		System.out.println(\"" + name.charAt(0) + " = \" + s.getSCInterface().get" + name+ "());");
		}
		System.out.println( 
				"	}\r\n" + 
				"}\r\n" + 
				"");
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}

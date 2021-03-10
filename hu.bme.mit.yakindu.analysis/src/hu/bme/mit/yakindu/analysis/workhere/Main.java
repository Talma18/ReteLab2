package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.sgraph.Statechart;

import java.util.ArrayList;

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
		ArrayList<String> names = new ArrayList<String>();
		System.out.println("Transitions:");
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				if (!state.getName().isEmpty()) {
					names.add(state.getName());
				}
				
				
			} else if(content instanceof Transition) {
				Transition t = (Transition) content;
				System.out.println(t.getSource().getName() + " -> " + t.getTarget().getName());
			}
		}
		System.out.println("\nStates:");
		iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				String extra="";
				if (state.getName().isEmpty()) {
					int index = 0;
					String proposed_name;
					while (true) {
						proposed_name = "State" + index;
						if (!names.contains(proposed_name)) {
							break;
						}
					}
					extra +=" , Empty name! Suggestion: " + proposed_name;
				}
				if (state.getOutgoingTransitions().size() == 0) {
					extra += ", Csapda!";
				}
				System.out.println("Name: " + state.getName() + extra);
			}
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}

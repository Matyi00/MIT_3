package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
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
		int i = 0;
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				i++;
				State state = (State) content;
				if (state.getOutgoingTransitions().isEmpty())
					System.out.print("Csapda: ");
				
				if (!state.getName().equals(""))
					System.out.println(state.getName());
				else System.out.println("Nincs neve, javasol név: state" + i); 
				
			}
			
			if(content instanceof Transition) {
				Transition transition = (Transition) content;
				System.out.println(transition.getSource().getName() + " -> " + transition.getTarget().getName() );
			}
			
			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition) content;
				System.out.println("Event: "+ event.getName());
			}
			
			if(content instanceof VariableDefinition) {
				VariableDefinition variable = (VariableDefinition) content;
				System.out.println("Variable: "+ variable.getName());
			}
			
		}
		
		System.out.println();
		
		printValtozok();
		
		
		System.out.println();
		
		printUtasitasok();
				
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
	
	public static void printValtozok() {
		System.out.println("public static void print(IExampleStatemachine s) {");
		
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		int i = 0;
		while (iterator.hasNext()) {
			EObject content = iterator.next(); 

			if(content instanceof VariableDefinition) {
				VariableDefinition variable = (VariableDefinition) content;
				String name = variable.getName();
				String capitalName =  Character.toString( (char) (name.charAt(0) + 'A' - 'a')) +  name.substring(1);
				System.out.println("System.out.println(\"" +  Character.toString(capitalName.charAt(0)) +" = \" + s.getSCInterface().get" +  capitalName + "());");
			}
		}
		
		System.out.println("}");
	}
	
	public static void printUtasitasok() {
		
		System.out.println("public static void main(String[] args) throws IOException {");
		System.out.println("ExampleStatemachine s = new ExampleStatemachine();");
		System.out.println("s.setTimer(new TimerService());");
		System.out.println("RuntimeService.getInstance().registerStatemachine(s, 200);");
		System.out.println("s.init();");
		System.out.println("s.enter();");
		System.out.println("s.runCycle();");
		System.out.println("print(s);");
			
		System.out.println("Scanner sc = new Scanner(System.in);");
		System.out.println("loop: while (true){");
		System.out.println("String str = sc.nextLine();");
		System.out.println("switch (str) {");
		
		
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		int i = 0;
		while (iterator.hasNext()) {
			EObject content = iterator.next(); 

			if(content instanceof EventDefinition) {
				EventDefinition event = (EventDefinition) content;			
				String name = event.getName();
				String capitalName =  Character.toString( (char) (name.charAt(0) + 'A' - 'a')) +  name.substring(1);
				System.out.println("case \"" + name +  "\": s.raise" + capitalName + "();break;");
			}
		}
		
		
		System.out.println("case \"exit\":break loop;}");
		System.out.println("s.runCycle();");
		System.out.println("print(s);}");
		System.out.println("System.exit(0);}");
		
	}
	
}

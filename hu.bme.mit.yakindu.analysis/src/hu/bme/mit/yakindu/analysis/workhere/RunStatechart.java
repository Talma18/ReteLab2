package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		boolean exit = false;
		while (!exit) {
			String input = "";
			int c = System.in.read();
			while (c!=10) {
				input+=(char)c;
				c = System.in.read();
			}
			input = input.toLowerCase();
			switch(input) {
			case "exit":
				exit = true;
				break;
			case "black":
				s.raiseBlack();
				break;
			case "white":
				s.raiseWhite();
				break;
			case "start":
				s.raiseStart();
				break;
			default:
				System.out.println("Unrecognized command: " + input);
				break;
			}
			s.runCycle();
			print(s);
		}
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}

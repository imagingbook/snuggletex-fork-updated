/* $Id: MinimalExample.java 742 2012-05-07 13:09:53Z davemckain $
 *
 * Copyright (c) 2008-2011, The University of Edinburgh.
 * All Rights Reserved
 */
package wilbur;


import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleSession;
import uk.ac.ed.ph.snuggletex.definitions.TextFlowContext;
import uk.ac.ed.ph.snuggletex.tokens.ArgumentContainerToken;
import uk.ac.ed.ph.snuggletex.tokens.CommandToken;
import uk.ac.ed.ph.snuggletex.tokens.EnvironmentToken;
import uk.ac.ed.ph.snuggletex.tokens.FlowToken;
import uk.ac.ed.ph.snuggletex.tokens.SimpleToken;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.SourceLocator;

/**
 * Example demonstrating a minimal example use of SnuggleTeX.
 * <p>
 * This simply converts a fixed input String of LaTeX to XML. 
 * (In this case, the result is a fragment of MathML.)
 * 
 * see XMLStringOutputExample
 * see WebPageExample
 *
 * @author  David McKain
 * @version $Revision: 742 $
 */
public final class TestWB1 {
	
//	static String inputString = "Math text $$ \\sin(x+1)+2=3 $$";
//	static String inputString = "This is plain text with some $math code1$ and \\($ math code2 \\) in it.";
//	static String inputString = "This is \\emph{emphasized text content} and a \\ref{reference}.";
//	static String inputString = "This is \\verb!verbatim stuff! and some text.";
// 	static String inputString = "This is \\textbf{bold text with \\emph{nested emph} text}.";	// does not nest!!
// 	static String inputString = "This is \\begin{verbatim}verbatim  text \\ref{reFerence} \\end{verbatim} followed by regular text}.";
//	static String inputString = "Two \n\nnewlines";
// 	static String inputString = "Some % comment \nbefore line";	//OK
// 	static String inputString = "Some% comment \nbefore line";	//OK
//	static String inputString = "Some% comment \n before line";	//OK
	static String inputString = "a \\strange command";		// WRONG, doesnt recognize commands at all, except \bf etc!
    
    public static void main(String[] args) throws IOException {
    	
    	System.out.println("input = |" + inputString + "|");
    	
        /* Create vanilla SnuggleEngine and new SnuggleSession */
        SnuggleEngine engine = new SnuggleEngine();
        SnuggleSession session = engine.createSession();
        
        /* Parse some very basic Math Mode input */
        SnuggleInput input = new SnuggleInput(inputString);
        boolean success = session.parseInput(input);
        System.out.println("Parsing success: " + success);
        
        System.out.println("Parsed tokens: ");
        List<FlowToken> tokens = session.getParsedTokens();
        for (FlowToken t : tokens) {
        	//System.out.println("  " + t.toString());
        	//System.out.format("  type=%s, mode=%s class=%s\n", t.getType().toString(), t.getLatexMode(), t.getClass().getSimpleName());
        	System.out.println(toString(t));
        }
        System.out.println();
        
        // -------------------
        
        NodeList nodes = session.buildDOMSubtree();
        
        if (nodes == null) {
        	System.out.println("Could not build DOM Tree!");
        	return;
        }
        
        System.out.println("NodeList length = " + nodes.getLength());
        System.out.println("\nDOM Tree: ");
        for (int i = 0; i < nodes.getLength(); i++) {
        	Node n = nodes.item(i);
			//n.compareDocumentPosition(null);
        	traverse(n, 0);
        }
        
        /* Convert the results to an XML String, which in this case will
         * be a single MathML <math>...</math> element. */
       String xmlString = session.buildXMLString();
       System.out.println("Input " + input.getString() + " was converted to:\n" + xmlString);
    }
    
    // -----------------------------------------------------------------------
    
//	static void traverse(Node n, int level) {
////		System.out.format("%s%s: <%s> id=%d (%s)\n", 
////				makeIndentString(level), 
////				n.getClassName(), 
////				n.stringify(), n.getId(), 
////				(n.getPosition() == null) ? "**NULL**" : n.getPosition().toString());	// wilbur: STRANGE - TextNode has null position!
//		String text = n.getTextContent();
//		
//		System.out.println(makeIndentString(level) + n.toString() + " " + text);
//		NamedNodeMap attributes = n.getAttributes();
//		if (attributes != null) {
//			for (int j = 0; j < attributes.getLength(); j++) {
//				Node na = attributes.item(j);
//				System.out.println(makeIndentString(level) + ">>> " + na.toString());
//			}
//		}
//		
//		NodeList children = n.getChildNodes();
//		for (int i = 0; i < children.getLength(); i++) {
//			Node c = children.item(i);
//			traverse(c, level + 1);
//		}
//	}
	
	static void traverse(Node n, int level) {
		System.out.println(toString(n, level));
		listNodeAttributes(n);
		NodeList children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node c = children.item(i);
			traverse(c, level + 1);
		}
	}
	
	static String toString(Node n, int level) {
		// System.out.println(n.getClass().getCanonicalName());
		// SourceLocator sl = (SourceLocator) n;
		// int lin = sl.getLineNumber();
		// int col = sl.getColumnNumber();
		String nodeStr =
				String.format("Node %s (type=%d, %s): |%s|", // (%d, %d)",
						n.getNodeName(), n.getNodeType(), n.getClass().getSimpleName(),  n.getNodeValue()); //,
				//lin, col);
		return makeIndentString(level) + nodeStr;
	}

	static void listNodeAttributes(Node n) {
		NamedNodeMap attr = n.getAttributes();
		if (attr == null) return;
		int k = attr.getLength();
		for (int i = 0; i < k ; i++) {
			Node a = attr.item(i);
			System.out.println("     a = " + a);
		}
	}
	
	
	static String toString(FlowToken t) {
		TextFlowContext k = t.getTextFlowContext();
		int start = t.getSlice().getStartIndex();
		int end = t.getSlice().getEndIndex();

		return String.format("  FlowToken class=%s: |%s| (%d-%d)",
				t.getClass().getSimpleName(), t.getSlice().extract(), start, end);
//		if (t instanceof SimpleToken) {
//			SimpleToken st = (SimpleToken) t;
//			return String.format("SimpleToken %s", st.getSlice().extract());
//		}
//		else if (t instanceof CommandToken) {
//			CommandToken ct = (CommandToken) t;
//			//ArgumentContainerToken[] act = ct.getArguments();
//			for (ArgumentContainerToken act : ct.getArguments()) {
//				System.out.println("  act = " + act.toString());
//				for (FlowToken ft : act.getContents()) {
//					System.out.println("    ft = " + toString(ft));
//				}
//				
//			}
//			return String.format("CommandToken cmd=%s", ct.getCommand().getTeXName());
//		}
//		else if (t instanceof EnvironmentToken) {
//			EnvironmentToken et = (EnvironmentToken) t;
//			return String.format("EnvironmentToken cmd=%s", et.getEnvironment().getTeXName());
//		}
//		else return t.toString();
			
	}
	

	static private String makeIndentString(int level) {
		char[] charArray = new char[4 * level];
		Arrays.fill(charArray, ' ');
		return new String(charArray);
	}
}

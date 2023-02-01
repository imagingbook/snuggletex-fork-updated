/* $Id: MinimalExample.java 742 2012-05-07 13:09:53Z davemckain $
 *
 * Copyright (c) 2008-2011, The University of Edinburgh.
 * All Rights Reserved
 */
package wilbur;

import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleSession;
import uk.ac.ed.ph.snuggletex.tokens.FlowToken;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
public final class TestWB2 {
	
//	static String inputString = "$$ x+2=3 $$";
	static String inputString = "This is plain text with some $math code1$ and \\($ math code2 \\) in it.";
//	static String inputString = "This is \\emph{emphasized text content} and a \\ref{reFerence}.";
    
    public static void main(String[] args) throws IOException {
        /* Create vanilla SnuggleEngine and new SnuggleSession */
        SnuggleEngine engine = new SnuggleEngine();
        SnuggleSession session = engine.createSession();
        
        /* Parse some very basic Math Mode input */
        SnuggleInput input = new SnuggleInput(inputString);
        session.parseInput(input);
        
        List<FlowToken> tokens = session.getParsedTokens();
        for (FlowToken token : tokens) {
        	System.out.println(token.toString());
        	
        }
        
        
//        NodeList nodes = session.buildDOMSubtree();
//        System.out.println("NodeList length = " + nodes.getLength());
//        for (int i = 0; i < nodes.getLength(); i++) {
//        	Node n = nodes.item(i);
//        	//System.out.println(i + ": " + n.toString());
//        	traverse(n, 0);
//        }
//        
//        /* Convert the results to an XML String, which in this case will
//         * be a single MathML <math>...</math> element. */
//        String xmlString = session.buildXMLString();
//        System.out.println("Input " + input.getString() + " was converted to:\n" + xmlString);
    }
    
	static void traverse(Node n, int level) {
//		System.out.format("%s%s: <%s> id=%d (%s)\n", 
//				makeIndentString(level), 
//				n.getClassName(), 
//				n.stringify(), n.getId(), 
//				(n.getPosition() == null) ? "**NULL**" : n.getPosition().toString());	// wilbur: STRANGE - TextNode has null position!
		String text = n.getTextContent();
		
		
		
		System.out.println(makeIndentString(level) + n.toString() + " " + text);
		NamedNodeMap attributes = n.getAttributes();
		if (attributes != null) {
			for (int j = 0; j < attributes.getLength(); j++) {
				Node na = attributes.item(j);
				System.out.println(makeIndentString(level) + ">>> " + na.toString());
			}
		}
		
		NodeList children = n.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node c = children.item(i);
			traverse(c, level + 1);
		}
	}
	

	static private String makeIndentString(int level) {
		char[] charArray = new char[4 * level];
		Arrays.fill(charArray, ' ');
		return new String(charArray);
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp;

import java.io.*;
import java.util.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.process.*;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 *
 * @author msuman
 */
public class main {

    public static CharSequence[] locationalPrepositions = {
        " in", " on", " at", " by", " near", " nearby", " above", " below", " over",
        " under", " up", " down", " around", " through", " inside", " outside",
        " between", " beside", " beyond", " behind", " within", " beneath",
        " underneath", " among", " along", " against", " where", " here" , " to" };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("\n\n\nSTART\n\n\n"); // print START
        try // device to handle potential errors
        {
            // open file whose path is passed
            // as the first argument of the main method:
            FileInputStream fis = new FileInputStream("input.txt");
            DataInputStream dis = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(dis));
//            BufferedReader br = new BufferedReader(new StringReader("Two hours after lunch we went to a party"));

            // prepare Parser, Tokenizer and Tree printer:
            LexicalizedParser lp = new LexicalizedParser("englishPCFG.ser.gz");
            TokenizerFactory tf = PTBTokenizer.factory(false, new WordTokenFactory());
            TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");

            String sentence; // initialization
            // for each line of the file
            // retrieve it as a string called 'sentence':
            while ((sentence = br.readLine()) != null) {
                A1Result result = new A1Result();
                // print sentence:
                System.out.println("\n***************************************************\n\n\nORIGINAL:\n\n" + sentence);
                // put tokens in a list:
                List tokens = tf.getTokenizer(new StringReader(sentence)).tokenize();
                lp.parse(tokens); // parse the tokens
                Tree t = lp.getBestParse(); // get the best parse tree

//                System.out.println("Tree size is: " + t.size());
                Iterator<Tree> it = t.iterator();
                int i = 0;
                while (it.hasNext()) {
                    Tree child = it.next();
                    //System.out.println("***** " + i + " ***** NN " + child.nodeNumber(t) + " " + child.label());
                    if (child.isPhrasal()) {
                        if (child.label().toString().compareTo("ROOT") == 0) {
                            /* Skip the root, we are not analysing it */
                        } else if (child.label().toString().compareTo("S") == 0) {
                            /* If we are not a part of a VP or NP already, skip? */
                        } else if (child.label().toString().compareTo("NP") == 0) {
                            //System.out.println(child.label().toString() + " *** " + child);
                            List<Tree> leaves = child.getLeaves();
                            Iterator<Tree> it1 = leaves.iterator();
                            //System.out.println(leaves.size());
                            String val = "";
                            int lastNodeNumber = child.nodeNumber(t);
                            while (it1.hasNext()) {
                                Tree t1 = it1.next();
                                val = val.concat(" " + t1.toString());
                                lastNodeNumber = t1.nodeNumber(t);
                            }

                            for (CharSequence s: locationalPrepositions) {
                                if (val.contains(s)) {
                                    result.setWhere(val.substring(val.indexOf(s.toString())));
                                    val = val.substring(0, (val.indexOf(s.toString()) + 1));
                                }
                            }

                            if (result.getWhat().isEmpty()) {
                                result.setWhat(val);
                            }

                            result.setWho(val);

                            if (child.nodeNumber(t) < lastNodeNumber) {
                                Tree mfChild = it.next();
                                while (mfChild.nodeNumber(t) < lastNodeNumber) {
                                    //System.out.println("Skipping " + mfChild.label());
                                    mfChild = it.next();
                                }
                            }
                        } else if (child.label().toString().compareTo("VP") == 0) {
                            Tree local = child.localTree();
                            //System.out.println(local);
                            //System.out.println(child.label().toString() + " *** " + child);
                            List<Tree> leaves = child.getLeaves();
                            Iterator<Tree> it1 = leaves.iterator();
                            //System.out.println(leaves.size());
                            String val = "";
                            int lastNodeNumber = child.nodeNumber(t);
                            while (it1.hasNext()) {
                                Tree t1 = it1.next();
                                val = val.concat(" " + t1.toString());
                                lastNodeNumber = t1.nodeNumber(t);
                            }
                            
                            for (CharSequence s: locationalPrepositions) {
                                if (val.contains(s)) {
                                    result.setWhere(val.substring(val.indexOf(s.toString())));
                                    val = val.substring(0, (val.indexOf(s.toString()) + 1));
                                }
                            }

                            if (result.getWho().isEmpty()) {
                                result.setWho(val);
                            }

                            result.setWhat(val);

                            if (child.nodeNumber(t) < lastNodeNumber) {
                                Tree mfChild = it.next();
                                while (mfChild.nodeNumber(t) < lastNodeNumber) {
                                    //System.out.println("Skipping " + mfChild.label());
                                    mfChild = it.next();
                                }
                            }
                        } //else
                          //System.out.println(child.label().toString() + " *** " + child);
                        //System.out.println(child.label() + " " + child.score());
                    }
                    ++i;
                }

                System.out.println("\nPROCESSED:\n\n" + result + "\n");
                //tp.printTree(t); // print tree
              }
            dis.close(); // close input file
        } catch (Exception e) // catch error if any
        {
            System.err.println("ERROR: " + e.getMessage()); // print error message
        }
        System.out.println("\n\n\nTHE END\n\n\n"); // print THE END
    }
}

/**
 * 
 */
package edu.buffalo.cse.irf14.analysis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author nikhillo
 *
 */
public class StopWordFilter extends TokenFilter {
	private static final Set<String> wordSet = new HashSet<String>(Arrays.asList(new String[] {"a", "an", "and", "are", "as", "at", "be", "but",
		"by", "do", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that",
		"the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with"}));
	
	public StopWordFilter(TokenStream stream) {
		super(stream);
	}

	/* (non-Javadoc)
	 * @see edu.buffalo.cse.irf14.analysis.Analyzer#increment()
	 */
	@Override
	public boolean increment() throws TokenizerException {
		if (stream != null) {
			Token t = getToken();
			StopWordContainer stopWord = new StopWordContainer();
			if (t != null && stopWord.CheckContainer(t.getNormalizedText())) {
				stream.remove();
			}
			
			return stream.hasNext();
		}
		
		return false;

	}

}
class StopWordContainer {
	public  static Map<String, Integer> mp= new HashMap<String, Integer>();
	static {
				mp.put("a",1);
				mp.put("about",1);
				mp.put("above",1);
				mp.put("across",1);
				mp.put("after",1);
				mp.put("afterwards",1);
				mp.put("again",1);
				mp.put("against",1);
				mp.put("all",1);
				mp.put("almost",1);
				mp.put("alone",1);
				mp.put("along",1);
				mp.put("already",1);
				mp.put("also",1);
				mp.put("although",1);
				mp.put("always",1);
				mp.put("among",1);
				mp.put("amongst",1);
				mp.put("amoungst",1);
				mp.put("amount",1);
				mp.put("an",1);
				mp.put("and",1);
				mp.put("another",1);
				mp.put("any",1);
				mp.put("anyhow",1);
				mp.put("anyone",1);
				mp.put("anything",1);
				mp.put("anyway",1);
				mp.put("anywhere",1);
				mp.put("are",1);
				mp.put("around",1);
				mp.put("as",1);
				mp.put("at",1);
				mp.put("back",1);
				mp.put("be",1);
				mp.put("became",1);
				mp.put("because",1);
				mp.put("become",1);
				mp.put("becomes",1);
				mp.put("becoming",1);
				mp.put("been",1);
				mp.put("before",1);
				mp.put("beforehand",1);
				mp.put("behind",1);
				mp.put("being",1);
				mp.put("below",1);
				mp.put("beside",1);
				mp.put("besides",1);
				mp.put("between",1);
				mp.put("beyond",1);
				mp.put("bill",1);
				mp.put("both",1);
				mp.put("bottom",1);
				mp.put("but",1);
				mp.put("by",1);
				mp.put("call",1);
				mp.put("can",1);
				mp.put("cannot",1);
				mp.put("cant",1);
				mp.put("co",1);
				mp.put("con",1);
				mp.put("could",1);
				mp.put("couldnt",1);
				mp.put("cry",1);
				mp.put("describe",1);
				mp.put("detail",1);
				mp.put("do",1);
				mp.put("done",1);
				mp.put("down",1);
				mp.put("due",1);
				mp.put("during",1);
				mp.put("each",1);
				mp.put("eg",1);
				mp.put("eight",1);
				mp.put("either",1);
				mp.put("elevenelse",1);
				mp.put("elsewhere",1);
				mp.put("empty",1);
				mp.put("enough",1);
				mp.put("etc",1);
				mp.put("even",1);
				mp.put("ever",1);
				mp.put("every",1);
				mp.put("everyone",1);
				mp.put("everything",1);
				mp.put("everywhere",1);
				mp.put("except",1);
				mp.put("few",1);
				mp.put("fifteen",1);
				mp.put("fify",1);
				mp.put("fill",1);
				mp.put("find",1);
				mp.put("fire",1);
				mp.put("first",1);
				mp.put("five",1);
				mp.put("for",1);
				mp.put("former",1);
				mp.put("formerly",1);
				mp.put("forty",1);
				mp.put("found",1);
				mp.put("four",1);
				mp.put("from",1);
				mp.put("front",1);
				mp.put("full",1);
				mp.put("further",1);
				mp.put("get",1);
				mp.put("give",1);
				mp.put("go",1);
				mp.put("had",1);
				mp.put("has",1);
				mp.put("hasnt",1);
				mp.put("have",1);
				mp.put("he",1);
				mp.put("hence",1);
				mp.put("her",1);
				mp.put("here",1);
				mp.put("hereafter",1);
				mp.put("hereby",1);
				mp.put("herein",1);
				mp.put("hereupon",1);
				mp.put("hers",1);
				mp.put("herself",1);
				mp.put("him",1);
				mp.put("himself",1);
				mp.put("his",1);
				mp.put("how",1);
				mp.put("however",1);
				mp.put("hundred",1);
				mp.put("ie",1);
				mp.put("if",1);
				mp.put("in",1);
				mp.put("inc",1);
				mp.put("indeed",1);
				mp.put("interest",1);
				mp.put("into",1);
				mp.put("is",1);
				mp.put("it",1);
				mp.put("its",1);
				mp.put("itself",1);
				mp.put("keep",1);
				mp.put("last",1);
				mp.put("latter",1);
				mp.put("latterly",1);
				mp.put("least",1);
				mp.put("less",1);
				mp.put("ltd",1);
				mp.put("made",1);
				mp.put("many",1);
				mp.put("may",1);
				mp.put("me",1);
				mp.put("meanwhile",1);
				mp.put("might",1);
				mp.put("mill",1);
				mp.put("mine",1);
				mp.put("more",1);
				mp.put("moreover",1);
				mp.put("most",1);
				mp.put("mostly",1);
				mp.put("move",1);
				mp.put("much",1);
				mp.put("must",1);
				mp.put("my",1);
				mp.put("myself",1);
				mp.put("name",1);
				mp.put("namely",1);
				mp.put("neither",1);
				mp.put("never",1);
				mp.put("nevertheless",1);
				mp.put("next",1);
				mp.put("nine",1);
				mp.put("no",1);
				mp.put("nobody",1);
				mp.put("none",1);
				mp.put("noone",1);
				mp.put("nor",1);
				mp.put("not",1);
				mp.put("nothing",1);
				mp.put("now",1);
				mp.put("nowhere",1);
				mp.put("of",1);
				mp.put("off",1);
				mp.put("often",1);
				mp.put("on",1);
				mp.put("once",1);
				mp.put("one",1);
				mp.put("only",1);
				mp.put("onto",1);
				mp.put("or",1);
				mp.put("other",1);
				mp.put("others",1);
				mp.put("otherwise",1);
				mp.put("our",1);
				mp.put("ours",1);
				mp.put("ourselves",1);
				mp.put("out",1);
				mp.put("over",1);
				mp.put("ownpart",1);
				mp.put("per",1);
				mp.put("perhaps",1);
				mp.put("please",1);
				mp.put("put",1);
				mp.put("rather",1);
				mp.put("re",1);
				mp.put("same",1);
				mp.put("see",1);
				mp.put("seem",1);
				mp.put("seemed",1);
				mp.put("seeming",1);
				mp.put("seems",1);
				mp.put("serious",1);
				mp.put("several",1);
				mp.put("she",1);
				mp.put("should",1);
				mp.put("show",1);
				mp.put("side",1);
				mp.put("since",1);
				mp.put("sincere",1);
				mp.put("six",1);
				mp.put("sixty",1);
				mp.put("so",1);
				mp.put("some",1);
				mp.put("somehow",1);
				mp.put("someone",1);
				mp.put("something",1);
				mp.put("sometime",1);
				mp.put("sometimes",1);
				mp.put("somewhere",1);
				mp.put("still",1);
				mp.put("such",1);
				mp.put("take",1);
				mp.put("ten",1);
				mp.put("than",1);
				mp.put("that",1);
				mp.put("the",1);
				mp.put("their",1);
				mp.put("them",1);
				mp.put("themselves",1);
				mp.put("then",1);
				mp.put("thence",1);
				mp.put("there",1);
				mp.put("thereafter",1);
				mp.put("thereby",1);
				mp.put("therefore",1);
				mp.put("therein",1);
				mp.put("thereupon",1);
				mp.put("these",1);
				mp.put("they",1);
				mp.put("thickv",1);
				mp.put("thin",1);
				mp.put("third",1);
				mp.put("this",1);
				mp.put("those",1);
				mp.put("though",1);
				mp.put("through",1);
				mp.put("throughout",1);
				mp.put("thus",1);
				mp.put("to",1);
				mp.put("together",1);
				mp.put("too",1);
				mp.put("top",1);
				mp.put("toward",1);
				mp.put("towards",1);
				mp.put("twelve",1);
				mp.put("twenty",1);
				mp.put("two",1);
				mp.put("un",1);
				mp.put("under",1);
				mp.put("until",1);
				mp.put("up",1);
				mp.put("upon",1);
				mp.put("us",1);
				mp.put("very",1);
				mp.put("was",1);
				mp.put("we",1);
				mp.put("well",1);
				mp.put("were",1);
				mp.put("what",1);
				mp.put("whatever",1);
				mp.put("when",1);
				mp.put("whence",1);
				mp.put("whenever",1);
				mp.put("where",1);
				mp.put("whereafter",1);
				mp.put("whereas",1);
				mp.put("whereby",1);
				mp.put("wherein",1);
				mp.put("whereupon",1);
				mp.put("wherever",1);
				mp.put("whether",1);
				mp.put("which",1);
				mp.put("while",1);
				mp.put("whither",1);
				mp.put("who",1);
				mp.put("whoever",1);
				mp.put("whole",1);
				mp.put("whom",1);
				mp.put("whose",1);
				mp.put("why",1);
				mp.put("will",1);
				mp.put("with",1);
				mp.put("within",1);
				mp.put("without",1);
				mp.put("would",1);
				mp.put("yet",1);
				mp.put("you",1);
				mp.put("your",1);
				mp.put("yours",1);
				mp.put("yourself",1);
				mp.put("yourselves",1);
				mp.put("the",1);
	}

	public Boolean CheckContainer(String string){
		if(string!=null)
			return  mp.containsKey(string);
		else 
			return false;
}
}


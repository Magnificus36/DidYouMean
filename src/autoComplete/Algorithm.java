package autoComplete;

import autoComplete.tree.*;

import java.util.*;

/**
 * Contains all algorithm methods for auto complete
 *
 * @author Frans
 */
public final class Algorithm {

    private Algorithm() {
    }

    /**
     * Character used for termination of keywords, to indicate a {@link Leaf} should be made.
     */
    public final static char TERM = '\0';

    /**
     * Recursively searches for the {@link String}-value of the node with the highest weight in {@code ns}(including their subtrees).
     *
     * @param ns The subtrees to search in.
     * @return The {@link String} corresponding to the value of {@link Element#getWord} getWord of the highest weighed element.
     */
    public static String maxNode(Set<Element> ns) {
        String result;

        Element maxN = Collections.max(ns, (o1, o2) -> o1.getWeight() - o2.getWeight());

        if (maxN.getLetter() == TERM) {
            result = maxN.getWord();
        } else {
            result = maxNode(maxN.getChildren());
        }
        return result;
    }

    /**
     * Returns the smallest possible set of elements that do not contain the element corresponding to {@code k}
     * (using {@code n} as starting point)
     *
     * @param n The element to be used as root.
     * @param k The keyword to exclude from the subtree of {@code n}
     * @return The smallest possible {@link Set} containing all elements whose
     * subtrees does not contain the element corresponding to {@code k}
     */
    public static Set<Element> excludeKeyword(Element n, String k) {
        Set<Element> result = new HashSet<>();
        for (Element child : n.getChildren()) {
            if (child.getLetter() == k.charAt(0)) {
                result.addAll(excludeKeyword(child, k.substring(1)));
            } else {
                result.add(child);
            }
        }
        return result;
    }

    /**
     * Searches for the element in the subtree of <code>n</code> corresponding to <code>p</code>.
     * If <code>n</code> is a {@link Root}, {@link Element#getWord getWord} of the result will return {@code p}.
     *
     * @param n The element to start to search in
     * @param p The string to search for in the subtree of (@code n}
     * @return The element that was found, or {@code null} if it doesn't exist
     */
    public static Element searchElement(Element n, String p) {
        if (p.isEmpty()) {
            return n;
        }
        if (n.hasChild(p.charAt(0))) {
            return searchElement(n.getChild(p.charAt(0)), p.substring(1));
        } else {
            return null;
        }
    }

    /**
     * Searches for the <code>c</code> keywords in <code>r</code> with the highest score beginning with <code>p</code>.
     *
     * @param r The root of the tree to search in
     * @param c The amount of keywords to be return
     * @param p The prefix all the resulting keywords must have
     * @return A list with at most <code>c</code> keywords(less than <code>c</code> if no more could be found).
     */
    public static List<String> getTopKeywords(Root r, int c, String p) {
        p = p.toLowerCase();
        List<String> result = new ArrayList<>(c);
        Element n = searchElement(r, p);
        if (n == null) {
            return result;
        }
        Set<Element> searchNodes = new HashSet<>();
        searchNodes.addAll(n.getChildren());
        int i = 0;
        while (i++ < c && !searchNodes.isEmpty()) {
            String keyword = maxNode(searchNodes);
            result.add(keyword);
            for (Element e : searchNodes) {
                String word = e.getWord();
                if (keyword.startsWith(word)) {
                    searchNodes.remove(e);
                    searchNodes.addAll(excludeKeyword(e, keyword.replaceFirst(word, "")));
                    break; //i'm sorry, don't kill me for using break...
                }
            }
        }
        return result;
    }
}

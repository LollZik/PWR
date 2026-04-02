public class Main {
    public static void main(String[] args) {
        TrieDictionary<Integer> test = new TrieDictionary<>(256);

        System.out.println("Insert: ");
        test.insert("a", 1);
        test.insert("ab", 2);
        test.insert("abc", 3);
        test.insert("abcd", 7);

        test.insert("ccc", 0);
        test.insert("cc", 1);

        test.insert("du", 6);
        test.insert("duma", 9);
        System.out.println(test.insert("a", 2));
        System.out.println(test.insert("ab", 1));


        System.out.println("Search: ");

        System.out.println(test.search("abc"));
        System.out.println(test.search("cc"));
        System.out.println(test.search("ccc"));
        System.out.println(test.search("nie"));
        System.out.println(test.search("du"));
        System.out.println(test.search("duma"));


        System.out.println("Remove:");

        System.out.println(test.remove("abcd"));
        System.out.println(test.search("abcd"));
        System.out.println(test.search("abc")+"\n");

        System.out.println(test.remove("ccc"));
        System.out.println(test.search("cc")+"\n");

        System.out.println(test.remove("duma"));
        System.out.println(test.search("du"));

        System.out.println(test.remove("zzz"));

        System.out.println("\nRoot test: ");
        System.out.println(test.insert("",-8));
        System.out.println( test.search(""));

        test.insert("zamek", 1);
        test.insert("zamczysko", 1);
        test.insert("zamarznac", 1);
        test.insert("zamarznalem", 1);
        test.insert("abrakadabra",0);
        System.out.println("Longest path:");
        System.out.println(test.longestEmptyPath());
    }
}

package F28DA_CW1;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Iterator;

public class HashWordMapTest {

	// Add your own tests, for example to test the method hashCode from HashWordMap
	
	// ...

    /** Test 1: test collision handling, double hashing and linear probing */
    @Test
    public void test1() {
        float maxLF = 0.5f;
        HashWordMap h = new HashWordMap(maxLF);
        String word = "abcde";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        h.addPos(word, pos);
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edbcaaa", new WordPosition("test", line, "edbcaaa"));
        assertEquals(5, h.numberOfEntries());
    }

    /** Test 2: add / delete a lot of entries using removeWord */
    @Test
    public void test2() {
        try	{
            float maxLF = 0.5f;
            HashWordMap h = new HashWordMap(maxLF);
            String word;
            int line;
            String file;
            WordPosition pos;
            for (int k = 0; k < 10000; ++k) {
                word = "w" + k;
                line = k + 1;
                file = "f" + k;
                pos = new WordPosition(file, line, word);
                h.addPos(word, pos);
            }
            System.out.println(h.numberOfEntries());
            assertEquals(10000, h.numberOfEntries());
            h.averNumProbes();
            for ( int k = 0; k < 10000; ++k )
            {
                word = "w" + k;
                h.removeWord(word);
            }
            System.out.println(h.numberOfEntries());
            assertEquals(0, h.numberOfEntries());
            for (int k = 0; k < 10000; ++k) {
                word = "w" + k;
                try {
                    h.positions(word);
                    fail();
                } catch (WordException ignored) {
                }
            }
        } catch (WordException e) {
            fail();
        }
    }

    /** Test 3: add / delete a lot of entries using removePos */
    @Test
    public void test3() {
        try	{
            float maxLF = 0.5f;
            HashWordMap h = new HashWordMap(maxLF);
            String word;
            int line;
            String file;
            WordPosition pos;
            for (int k = 0; k < 500; ++k) {
                word = "w" + k;
                line = k + 1;
                file = "f" + k;
                pos = new WordPosition(file, line, word);
                h.addPos(word, pos);
            }
            System.out.println(h.numberOfEntries());
            assertEquals(500, h.numberOfEntries());
            for ( int k = 0; k < 500; ++k )
            {
                word = "w" + k;
                line = k + 1;
                file = "f" + k;
                pos = new WordPosition(file, line, word);
                h.removePos(word,pos);
            }
            System.out.println(h.numberOfEntries());
            assertEquals(0, h.numberOfEntries());
            for (int k = 0; k < 500; ++k) {
                word = "w" + k;
                try {
                    h.positions(word);
                    fail();
                } catch (WordException ignored) {
                }
            }
        } catch (WordException e) {
            fail();
        }
    }

    /** Test 4: test removeWord when collision handling / linear probing is used */
    @Test
    public void test4() {
        float maxLF = 0.5f;
        HashWordMap h = new HashWordMap(maxLF);
        String word = "abcde";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        h.addPos(word, pos);
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edbcaaa", new WordPosition("test", line, "edbcaaa"));
        h.removeWord("abcde");
        h.removeWord("edbcaaa");
        assertEquals(2, h.numberOfEntries());
    }

    /** Test 5: test removePos when collision handling / linear probing is used */
    @Test
    public void test5() {
        float maxLF = 0.5f;
        HashWordMap h = new HashWordMap(maxLF);
        String word = "abcde";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        h.addPos(word, pos);
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edbcaaa", new WordPosition("test", line, "edbcaaa"));
        h.removePos(word, pos);
        assertEquals(4, h.numberOfEntries());
    }

    /** Test 6: test words() */
    @Test
    public void test6() {
        float maxLF = 0.5f;
        HashWordMap h = new HashWordMap(maxLF);
        String word = "abcde";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        h.addPos(word, pos);
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edbcaaa", new WordPosition("test", line, "edbcaaa"));
        h.removePos(word, pos);
        Iterator<String> words = h.words();
        int i = 0;
        while (words.hasNext()) {
            i++;
            words.next();
        }
        assertEquals(3, i);
    }

    /** Test 7: test positions() */
    @Test
    public void test7() {
        float maxLF = 0.5f;
        HashWordMap h = new HashWordMap(maxLF);
        String word = "abcde";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        h.addPos(word, pos);
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edcb", new WordPosition("test", line, "edcb"));
        h.addPos("edbcaaa", new WordPosition("test", line, "edbcaaa"));
        Iterator<IPosition> words = h.positions("edbcaaa");
        int i = 0;
        while (words.hasNext()) {
            i++;
            words.next();
        }
        System.out.println(i);
        assertEquals(1, i);
    }

    /** Test 8: try to delete a nonexistent entry. Should throw an exception. */
    @Test
    public void test8() {
        float maxLF = 0.5f;
        HashWordMap h = new HashWordMap(maxLF);
        String word = "abc";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        WordPosition pos2 = new WordPosition("f2", line, "abcde");
        h.addPos(word, pos);
        try {
            h.removePos("other", pos2);
            fail();
        } catch (WordException e) {
            assertTrue(true);
        }
    }
	
	@Test
	public void signatureTest() {
        try {
            IWordMap map = new HashWordMap(0.5f);
            String word1 = "test1";
            String word2 = "test2";
            IPosition pos1 = new WordPosition("test.txt", 4, word1);
            IPosition pos2 = new WordPosition("test.txt", 5, word2);      
            map.addPos(word1, pos1);
            map.addPos(word2, pos2);
            map.words();
            map.positions(word1);
            map.numberOfEntries();
            map.removePos(word1, pos1);
            map.removeWord(word2);
        } catch (Exception e) {
            fail("Signature of solution does not conform");
        }
	}

}

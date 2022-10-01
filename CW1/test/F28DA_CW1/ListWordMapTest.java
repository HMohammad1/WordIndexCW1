package F28DA_CW1;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Iterator;

public class ListWordMapTest {

	// Add your own tests
	
	// ...

	@Test
	public void signatureTest() {
        try {
            IWordMap map = new ListWordMap();
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
            map.removeWord("test2");
        } catch (Exception e) {
            fail("Signature of solution does not conform");
        }
	}

    @Test
    public void test1() {
        IWordMap h = new ListWordMap();
        String word = "abc";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        assertEquals(1, h.numberOfEntries());
    }

    @Test
    public void test2() {
        IWordMap h = new ListWordMap();
        String word = "abc";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        Iterator<IPosition> possOut;
        try {
            possOut = h.positions(word);
            IPosition posOut = possOut.next();
            assertTrue(posOut.getFileName().equals(file) && posOut.getLine() == line);
        } catch (WordException e) {
            fail();
        }
    }

    @Test
    public void test3() {
        IWordMap h = new ListWordMap();
        String word = "abc";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        try {
            h.positions(word);
        } catch (WordException e) {
            assertTrue(true);
        }
    }

    @Test
    public void test4() {
        IWordMap h = new ListWordMap();
        String word = "abc";
        String file = "f1";
        int line = 2;
        WordPosition pos = new WordPosition(file, line, word);
        h.addPos(word, pos);
        try {
            h.removeWord("other");
            fail();
        } catch (WordException e) {
            assertTrue(true);
        }
    }

    @Test
    public void test5() {
        try {
            IWordMap h = new ListWordMap();
            String word1 = "abc";
            String word2 = "bcd";
            String file = "f1";
            int line = 2;
            WordPosition pos1 = new WordPosition(file, line, word1);
            WordPosition pos2 = new WordPosition(file, line, word2);
            h.addPos(word1, pos1);
            h.addPos(word2, pos2);
            h.removePos(word2, pos2);
            assertEquals(1, h.numberOfEntries());
            Iterator<IPosition> possOut = h.positions(word1);
            assertTrue(possOut.hasNext());
            assertEquals(possOut.next(), pos1);
        } catch (WordException e) {
            fail();
        }
    }

    @Test
    public void test6() {
        IWordMap h = new ListWordMap();
        String word;
        int line;
        String file;
        WordPosition pos;
        for (int k = 0; k < 200; k++) {
            word = "w" + k;
            line = k + 1;
            file = "f" + k;
            pos = new WordPosition(file, line, word);
            h.addPos(word, pos);
        }
        assertEquals(h.numberOfEntries(), 200);
        for (int k = 0; k < 200; ++k) {
            word = "w" + k;
            try {
                Iterator<IPosition> poss = h.positions(word);
                assertTrue(poss.hasNext());
            } catch (WordException e) {
                fail();
            }

        }
    }

    /** Test 7: Delete a lot of entries from the  Map */
    @Test
    public void test7() {
        try	{
            IWordMap h = new ListWordMap();
            String word;
            int line;
            String file;
            WordPosition pos;
            for (int k = 0; k < 20000; ++k) {
                word = "w" + k;
                line = k + 1;
                file = "f" + k;
                pos = new WordPosition(file, line, word);
                h.addPos(word, pos);
            }
            assertEquals(h.numberOfEntries(), 20000);
            for ( int k = 0; k < 20000; ++k )
            {
                word = "w" + k;
                line = k + 1;
                file = "f" + k;
                pos = new WordPosition(file, line, word);
                h.removePos(word, pos);
            }
            assertEquals(h.numberOfEntries(), 0);
            for (int k = 0; k < 20000; ++k) {
                word = "w" + k;
                try {
                    h.positions(word);
                    fail();
                } catch (WordException e) {
                }
            }
        } catch (WordException e) {
            fail();
        }
    }

    /** Test 8: Get iterator over all keys */
    @Test
    public void test8() {
        IWordMap h = new ListWordMap();
        String word;
        int line;
        String file;
        WordPosition pos;
        try {
            for (int k = 0; k < 100; k++) {
                word = "w" + k;
                line = k + 1;
                file = "f" + k;
                pos = new WordPosition(file, line, word);
                h.addPos(word, pos);
            }

            for (int k = 10; k < 30; k++) {
                word = "w" + k;
                line = k + 1;
                file = "f" + k;
                pos = new WordPosition(file, line, word);
                h.removePos(word, pos);
            }
        } catch(WordException e) {
            fail();
        }
        Iterator<String> it = h.words();
        int count = 0;

        while (it.hasNext()) {
            count++;
            it.next();
        }
        assertEquals(h.numberOfEntries(),80);
        assertEquals(count,80);
    }


}

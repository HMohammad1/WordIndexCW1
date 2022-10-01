package F28DA_CW1;

import java.util.*;

public class ListWordMap implements IWordMap{

    private final LinkedList<WordPosition> lWords;

    public ListWordMap() {
        this.lWords = new LinkedList<>();
    }

    private WordPosition findWord(String findWord) {
        for(WordPosition w : this.lWords) {
            if (w.getWord().equals(findWord)) {
                return w;
            }
        }
        return null;
    }

    private WordPosition findWord(String findWord, IPosition position) {
        for(WordPosition w : this.lWords) {
            if (w.getWord().equals(findWord) && w.getLine() == position.getLine()) {
                return w;
            }
        }
        return null;
    }

    @Override
    public void addPos(String word, IPosition pos) {
        this.lWords.add(new WordPosition(pos.getFileName(), pos.getLine(), word));
    }

    @Override
    public void removeWord(String word) throws WordException {
        if(findWord(word) != null) {
            this.lWords.removeIf(findWords -> findWords.getWord().equals(word));
        }else {
            throw  new WordException(word);
        }

    }

    @Override
    public void removePos(String word, IPosition pos) throws WordException {
        if(findWord(word, pos) != null) {
            this.lWords.remove(new WordPosition(pos.getFileName(), pos.getLine(), word));
        }else {
            throw new WordException(word);
        }

    }

    @Override
    public Iterator<String> words() {
        LinkedList<String> iterator = new LinkedList<>();
        for(WordPosition word : this.lWords) {
            iterator.add(word.getWord());
            //System.out.println(word.getWord());
        }
        return iterator.iterator();
    }


    @Override
    public Iterator<IPosition> positions(String word) throws WordException {
        LinkedList<IPosition> iterator = new LinkedList<>();
        IPosition pos;
        for(WordPosition findWord : this.lWords) {
            if(findWord.getWord().equals(word)) {
                pos = new WordPosition(findWord.getFileName(), findWord.getLine(), word);
                iterator.add(pos);
                //System.out.println("File Name: " + findWord.getFileName() + " Line Number: " + findWord.getLine() + " Word: " + findWord.getWord());
            }
        }
        if(iterator.size() == 0) {
            throw new WordException(word);
        }
        return iterator.iterator();
    }

    @Override
    public int numberOfEntries() {
        return this.lWords.size();
    }

}

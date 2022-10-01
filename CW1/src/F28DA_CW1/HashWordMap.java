package F28DA_CW1;

import java.util.*;

public class HashWordMap implements IWordMap, IHashMonitor {

    private final float maxLF;
    private int size = 13;
    private ArrayList<LinkedList<WordPosition>> hashMap = new ArrayList<>(size);
    private int probes = 0;
    private int operations = 0;
    private int totalEntries;
    int collisions = 0;

    public HashWordMap() {
        this.maxLF = 0.5F;
        for (int i = 0; i < size; i++) {
            hashMap.add(null);
        }
    }

    public HashWordMap(float maxLF) {
        this.maxLF = maxLF;
        for (int i = 0; i < size; i++) {
            hashMap.add(null);
        }
    }

    @Override
    public float getMaxLoadFactor() {
        return this.maxLF;
    }

    @Override
    public float getLoadFactor() {
        return (float) totalEntries / hashMap.size();
    }

    @Override
    public float averNumProbes() {
        System.out.println((float) probes / operations);
        return (float) probes / operations;
    }

    @Override
    public int hashCode(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = hash *33+ s.charAt(i);
        }
        if (hash < 1) {
            hash = hash * -1;
        }
        return hash;
    }

    /** get the prime numbers for reSize */
    private static boolean Prime(int value) {
        for (int i = 2; i < value; i++) {
            if (value % i == 0) {
                return false;
            }
        }
        return true;
    }

    /** gets the next prime number needed to reSize the HashMap */
    private int nextPrimeNo() {
        int value = hashMap.size() * 2;
        while (!Prime(value)) {
            value++;
        }
        return value;
    }

    private int getSecondHash(int hash) {
        return 5 - (hash % 5);
    }

    /** make sure the new size is a prime number for least collisions */
    private void reSize(int newSize) {
        LinkedList<WordPosition> addWord;
        int counter = hashMap.size();
        size = newSize;
        ArrayList<LinkedList<WordPosition>> copy = hashMap;
        hashMap = new ArrayList<>(size);

        //make sure the values are null to begin with
        for (int i = 0; i < size; i++) {
            hashMap.add(null);
        }

        for (int i = 0; i < counter; i++) {
            if (copy.get(i) != null) {
                addWord = new LinkedList<>(copy.get(i));
                for (int j = 0; j < addWord.size(); j++) {
                    addPos(copy.get(i).get(j).getWord(), copy.get(i).get(j));
                }

            }
        }
    }

    /** check the hashMap at each stage, only add if the word at a specific hash is either the same word
     * you want to add or if there's none then if null */
    @Override
    public void addPos(String word, IPosition pos) {
        totalEntries++;
        probes++;
        LinkedList<WordPosition> addWord;
        int hash = hashCode(word) % hashMap.size();
        WordPosition wordPosition = new WordPosition(pos.getFileName(), pos.getLine(), word);
        operations++;
        if (hashMap.get(hash) == null) {
            addWord = new LinkedList<>();
            hashMap.set(hash, addWord);
            addWord.add(wordPosition);
        } else if (hashMap.get(hash) != null) {
            if (hashMap.get(hash).get(0).getWord().equals(word)) {
                hashMap.get(hash).add(wordPosition);
            } else {
                operations++;
                int secondHash = getSecondHash(hash);
                if (hashMap.get(secondHash) == null) {
                    collisions++;
                    addWord = new LinkedList<>();
                    addWord.add(wordPosition);
                    hashMap.set(secondHash, addWord);
                } else if (hashMap.get(secondHash) != null) {
                    if (hashMap.get(secondHash).get(0).getWord().equals(word)) {
                        hashMap.get(secondHash).add(wordPosition);
                    } else {
                        for (int j = secondHash; j < hashMap.size(); j += secondHash) {
                            operations++;
                            if (hashMap.get(j) == null) {
                                collisions++;
                                addWord = new LinkedList<>();
                                addWord.add(wordPosition);
                                hashMap.set(j, addWord);
                                break;
                            } else if (hashMap.get(j).get(0).getWord().equals(word)) {
                                hashMap.get(j).add(wordPosition);
                                break;
                            }
                        }
                    }
                }

            }
        }
        float loadFactor = getLoadFactor();
        if (loadFactor >= maxLF) {
            reSize(nextPrimeNo());
        }
        System.out.println(collisions);
    }

    @Override
    public void removeWord(String word) throws WordException {
        probes++;
        int hash = hashCode(word) % hashMap.size();
        operations++;
        if (hashMap.get(hash) == null) {
            boolean isTrue = findWord(hash, word);
            if (!isTrue) {
                throw new WordException(word);
            }
        } else {
            boolean isTrue = firstHash(word, hash);
            if (!isTrue) {
                isTrue = findWord(hash, word);
                if (!isTrue) {
                    throw new WordException(word);
                }
            }
        }
    }

    @Override
    public void removePos(String word, IPosition pos) throws WordException {
        probes++;
        int hash = hashCode(word) % hashMap.size();
        operations++;
        if (hashMap.get(hash) == null) {
            boolean isTrue = findWord(hash, word, pos);
            if (!isTrue) {
                throw new WordException(word);
            }
        } else {
            boolean isTrue = firstHash(word, pos, hash);
            if (!isTrue) {
                isTrue = findWord(hash, word, pos);
                if (!isTrue) {
                    throw new WordException(word);
                }
            }
        }
    }

    /** if fistHash fails or is null then this continues onto the next part */
    private boolean findWord(int hash, String word, IPosition pos) {
        operations++;
        int secondHash = getSecondHash(hash);
        if (hashMap.get(secondHash) == null) {
            return lastHash(word, pos);
        } else {
            boolean isTrue = secondHash(word, pos, secondHash);
            if (!isTrue) {
                return lastHash(word, pos);
            }
        }
        return true;
    }

    private boolean findWord(int hash, String word) {
        operations++;
        int secondHash = getSecondHash(hash);
        if (hashMap.get(secondHash) == null) {
            return lastHash(word);
        } else {
            boolean isTrue = secondHash(word, secondHash);
            if (!isTrue) {
                return lastHash(word);
            }
        }
        return true;
    }

    private boolean firstHash(String word, IPosition pos, int hash) {
        LinkedList<WordPosition> delWord = new LinkedList<>(hashMap.get(hash));
        return removeWordPos(delWord, word, pos, hash);
    }

    private boolean firstHash(String word, int hash) {
        return removeWordPos(word, hash);
    }

    private boolean secondHash(String word, IPosition pos, int secondHash) {
        LinkedList<WordPosition> delWord = new LinkedList<>(hashMap.get(secondHash));
        return removeWordPos(delWord, word, pos, secondHash);
    }

    private boolean secondHash(String word, int secondHash) {
        return removeWordPos(word, secondHash);
    }

    /** using the hash to find the word since first and second hash didn't work */
    private boolean lastHash(String word, IPosition pos) {
        LinkedList<WordPosition> delWord;
        int secondHash = getSecondHash(hashCode(word));
        for (int j = secondHash; j < hashMap.size(); j += secondHash) {
            operations++;
            if (hashMap.get(j) != null) {
                delWord = new LinkedList<>(hashMap.get(j));
                if (removeWordPos(delWord, word, pos, j)) return true;
            }
        }
        return false;
    }

    private boolean lastHash(String word) {
        int secondHash = getSecondHash(hashCode(word));
        for (int j = secondHash; j < hashMap.size(); j += secondHash) {
            operations++;
            if (hashMap.get(j) != null) {
                if (removeWordPos(word, j)) return true;
            }
        }
        return false;
    }

    /** actually removes the word */
    private boolean removeWordPos(String word, int hash) {
        if (hashMap.get(hash).get(0).getWord().equals(word)) {
            hashMap.set(hash, null);
            return true;
        }
        return false;
    }

    private boolean removeWordPos(LinkedList<WordPosition> delWord, String word, IPosition pos, int hash) {
        for (int i = 0; i < delWord.size(); i++) {
            if (delWord.get(i).getWord().equals(word) && delWord.get(i).getLine() == pos.getLine()) {
                delWord.remove(new WordPosition(pos.getFileName(), pos.getLine(), word));
                if (delWord.size() == 0) {
                    hashMap.set(hash, null);
                } else {
                    hashMap.set(hash, delWord);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<String> words() {
        LinkedList<String> iterator = new LinkedList<>();
        int i = 0;
        for (LinkedList<WordPosition> StoreWord : hashMap) {
            if (StoreWord != null) {
                iterator.add(hashMap.get(i).get(0).getWord());
            }
            i++;
        }
        return iterator.iterator();
    }

    @Override
    public Iterator<IPosition> positions(String word) throws WordException {
        probes++;
        LinkedList<IPosition> iterator = new LinkedList<>();
        int hash = hashCode(word) % hashMap.size();
        int secondHash = getSecondHash(hash);
        if (hashMap.get(hash) == null) {
            if (hashMap.get(secondHash) == null) {
                operations++;
                lastHashPos(word, iterator);
            } else if (hashMap.get(secondHash).get(0).getWord().equals(word)) {
                iterator = new LinkedList<>(hashMap.get(secondHash));
                return iterator.iterator();
            } else {
                lastHashPos(word, iterator);
            }
        } else if (hashMap.get(hash).get(0).getWord().equals(word)) {
            operations++;
            iterator = new LinkedList<>(hashMap.get(hash));
            return iterator.iterator();
        } else {
            if (hashMap.get(secondHash) == null) {
                operations++;
                lastHashPos(word, iterator);
            } else if (hashMap.get(secondHash).get(0).getWord().equals(word)) {
                iterator = new LinkedList<>(hashMap.get(secondHash));
                return iterator.iterator();
            } else {
                lastHashPos(word, iterator);
            }
        }
        return iterator.iterator();
    }


    private void lastHashPos(String word, LinkedList<IPosition> iterator) {
        int secondHash = getSecondHash(hashCode(word));
        for (int j = secondHash; j < hashMap.size(); j += secondHash) {
            operations++;
            if (hashMap.get(j) != null) {
                LinkedList<WordPosition> getWord = new LinkedList<>(hashMap.get(j));
                if (hashMap.get(j).get(0).getWord().equals(word)) {
                    for (WordPosition wordPosition : getWord) {
                        IPosition pos = new WordPosition(wordPosition.getFileName(), wordPosition.getLine(), word);
                        iterator.add(pos);
                    }
                }
            }
        }
//        if (iterator.size() == 0) {
//            throw new WordException(word);
//        }
    }

    @Override
    public int numberOfEntries() {
        int counter = 0;
        int entries = 0;
        for (LinkedList<WordPosition> StoreWord : hashMap) {
            if (StoreWord != null) {
                LinkedList<WordPosition> test = new LinkedList<>(hashMap.get(counter));
                entries += test.size();
            }
            counter++;
        }
        return entries;
    }
}

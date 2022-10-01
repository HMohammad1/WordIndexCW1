package F28DA_CW1;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

/** Main class for the Word Index program */
public class WordIndex {

	static final File textFilesFolder = new File("TextFiles_Shakespeare");
	static final FileFilter commandFileFilter = (File file) -> file.getParent()==null;
	static final FilenameFilter txtFilenameFilter = (File dir, String filename) -> filename.endsWith(".txt");



	public static void main(String[] argv) {
		long startTime = System.currentTimeMillis();
		if (argv.length != 1 ) {
			System.err.println("Usage: WordIndex commands.txt");
			System.exit(1);
		}
		try{
			File commandFile = new File(argv[0]);
			if (commandFile.getParent()!=null) {
				System.err.println("Use a command file in current directory");
				System.exit(1);
			}

			// creating a command reader from a file
			WordTxtReader commandReader = new WordTxtReader(commandFile);

			// initialise map
			IWordMap wordPossMap = new HashWordMap();
			// ...
			IPosition pos;
			int totalFiles = 0;
			// reading the content of the command file
			while(commandReader.hasNextWord()) {
				// getting the next command
				String command = commandReader.nextWord().getWord();

				switch (command) {
				case "addall":
					assert(textFilesFolder.isDirectory());
					File[] listOfFiles = textFilesFolder.listFiles(txtFilenameFilter);
					Arrays.sort(listOfFiles);
					totalFiles += listOfFiles.length;
					for (File textFile : listOfFiles) {
						WordTxtReader wordReader = new WordTxtReader(textFile);

						while (wordReader.hasNextWord()) {
							WordPosition wordPos = wordReader.nextWord();
							// adding word to the map
							// ...
							pos = new WordPosition(wordPos.getFileName(), wordPos.getLine(), wordPos.getWord());
							wordPossMap.addPos(wordPos.getWord(), pos);
						}
					}
					System.out.println(wordPossMap.numberOfEntries() + " entries have been indexed from " + listOfFiles.length + " files");
					//wordPossMap.positions("at");
					break;

				case "add":
					File textFile = new File(textFilesFolder, commandReader.nextWord().getWord()+".txt");
					WordTxtReader wordReader = new WordTxtReader(textFile);
					totalFiles += 1;
					int entries = 0;
					while (wordReader.hasNextWord()) {
						WordPosition word = wordReader.nextWord();
						// adding word to the map
						// ...
						pos = new WordPosition(word.getFileName(), word.getLine(), word.getWord());
						entries ++;
						wordPossMap.addPos(word.getWord(), pos);
					}
					System.out.println(entries + " entries have been indexed from file " + textFile.getName());
					System.out.println("Total words added: " + wordPossMap.numberOfEntries());
					break;

				case "search":
					int nb = Integer.parseInt(commandReader.nextWord().getWord());
					String word = commandReader.nextWord().getWord();
					// search for word entry in map
					// ...
					try {
						Set<String> files = uniqueFile(wordPossMap.positions(word));
						Map<String, Integer> test = noOfPositions(files, wordPossMap, word);

						Iterator<IPosition> counter = wordPossMap.positions(word);
						int c = 0;
						while(counter.hasNext()) {
							counter.next();
							c++;
						}

						System.out.println("The word \"" + word + "\"" + " occurs " + c + " times in " + files.size() + " files:");
						for(int i=0; i<nb; i++) {
							// map contains all the positions, so we just need to iterate as many times as specified in commands.txt
							// returning the highest number of words in the file each time
							String file = Collections.max(test.entrySet(), Map.Entry.comparingByValue()).getKey();
							int positions = Collections.max(test.entrySet(), Map.Entry.comparingByValue()).getValue();
							Iterator<IPosition> poss = wordPossMap.positions(word);
							Iterator<IPosition> poss2 = wordPossMap.positions(word);
							System.out.println("\t" + positions + " times in " + file + " on lines:");
							while (poss.hasNext()) {
								if (poss.next().getFileName().equals(file)) {
									System.out.print("\t" + poss2.next().getLine() + ", ");
								}else {
									poss2.next();
								}
							}
							test.remove(file);
							System.out.println();
						}

						//System.out.println("found \"" + word + "\" (" + i + ")");
					} catch (WordException e) {
						System.err.println("not found");
					}
					// ...
					break;

				case "remove":
					File textFileToRemove = new File(textFilesFolder, commandReader.nextWord().getWord()+".txt");
					// remove word-positions
					// ...
					wordReader = new WordTxtReader(textFileToRemove);
					int entriesToRemove = 0;
					while (wordReader.hasNextWord()) {
						WordPosition wordToRemove = wordReader.nextWord();
						// adding word to the map
						// ...
						pos = new WordPosition(wordToRemove.getFileName(), wordToRemove.getLine(), wordToRemove.getWord());
						wordPossMap.removePos(wordToRemove.getWord(), pos);
						entriesToRemove ++;
					}
					System.out.println(entriesToRemove + " entries have been removed from file " + textFileToRemove.getName());
					System.out.println("Total entries now: " + wordPossMap.numberOfEntries());
					break;

				case "overview":
					// print overview
					// ...
					Iterator<String> words = wordPossMap.words();
					int i = 0;
					while (words.hasNext()) {
						i++;
						words.next();
					}
					System.out.println("Overview:" + "\n" + "\t" + "Number of words: " + i + "\n" + "\t" + "Number of positions: " + wordPossMap.numberOfEntries() + "\n" + "\t" +  "Number of files: " + totalFiles);
					break;

				default:
					break;
				}

			}

		}
		catch (IOException e){ // catch exceptions caused by file input/output errors
			System.err.println("Check your file name");
			System.exit(1);
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total time: " + totalTime);
	}

	/** set used to only add unique fine names */
	private static Set<String> uniqueFile(Iterator<IPosition> position) {
		List<String> files = new LinkedList<>();
		while(position.hasNext()) {
			files.add(position.next().getFileName());
		}
		return new LinkedHashSet<>(files);
	}

	/** gets all the positions using the files as the unique parameter */
	private static Map<String, Integer> noOfPositions(Set<String> files, IWordMap wordPossMap, String word) {
		Map<String, Integer> map = new HashMap<>();
		for (String s : files) {
			int j = 0;
			Iterator<IPosition> position = wordPossMap.positions(word);
			while (position.hasNext()) {
				if(position.next().getFileName().equals(s)) {
					j++;
				}
			}
			map.put(s,j);
		}
		return map;
	}

}

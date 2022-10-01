# WordIndex CW1

A simple program that counts the words in different files and adds them to a hash table or a linled list map to see the difference in speed for each implementation.
You can either add all the words from a file, search for a word, add words from multiple files, remove a file that was already added and have an overview of the words added which includes the number of positions and files. 

To use in your IDE locate the 'WordIndex.java' file and run. You will need to add 'commands.txt' as an argument when you run as this is where you enter what you want the program to do. In the commands you put each command on a new line. 

![1](https://user-images.githubusercontent.com/35733550/193412389-682e42ab-1759-4ed7-9c6d-a4cf1595ccd5.png)

Commands include: 

addall: adds to the map the words and their positions for all the text files

"100985 entries have been indexed from 6 files"

overview: prints a summary of the number of indexed words, indexed positions and indexed files.

![1](https://user-images.githubusercontent.com/35733550/193407681-d0de4304-53e2-47e8-932b-9616c8388aaa.png)

search n word: searches for the number of times 'word' appears and sorts them by file name from high to low. The n tells you how many files to show in the output. search 3 from:

![2](https://user-images.githubusercontent.com/35733550/193412700-18812e86-b405-4533-b958-026163b0b8f5.png)

remove filename: removes the words in the file selected. remove 01:

![image](https://user-images.githubusercontent.com/35733550/193412799-c7a63fb0-44a8-478f-a2f8-0454a8872419.png)

add filename: Adds the words from that file to the map. Be sure to add the text file to the TextFiles directory. add 01:

![image](https://user-images.githubusercontent.com/35733550/193412934-a1bd77bc-bd43-4a86-8a13-cc850997ec53.png)


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

Results:

![image](https://user-images.githubusercontent.com/35733550/193413529-6352e47c-bebc-44d1-88d5-eb6ed7d24789.png)

As shown in the diagram above the linked list is faster than the HashMap. I’m not entirely sure why this is the case. One reason I think this could be is the fact that each time I call my resize method I must go through the entire HashMap and set it to null. I do this as it is needed for the add position method since it checks what is inside the HashMap at the very start and when first creating the HashMap null isn’t added by default. Though this is also needed for the other methods as null is used to represent that nothing is in the HashMap at that location. Since null is added linearly this could affect the speed of the HashMap. A fix to this could possibly be changing the array type used to contain the HashMap but I’m not sure if it would solve the issue. 

I have found a way to speed up the HashMap quite significantly by changing line 49 in my code to ‘hash = hash *33+ s.charAt(i);’. I didn’t add this in the code submitted as this happens to break some of my own tests for reasons I have yet to figure out BUT all the provided tests still seem to pass with this change. Thus, the new running times is as follows: 

![image](https://user-images.githubusercontent.com/35733550/193413631-8937f8a5-2cc7-43c4-b6ac-5fb6d373fefd.png)

Even though changing this one line of code sped up HashMap it is still slower than the linked list and this could be solved by maybe changing the hash function to something with even fewer collisions. 

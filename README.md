# SimpleDB

Project are sequential, so they take the code of the previous project and add stuff on it.

Project 1
Basic SimpleDB structure with Heap file access method, and seqScan operator.

Project 2
With indexing we are dealing with the disck for minimizing the IO
I will be able to use tuple and not .dat file as before assignement.
It is a B+ tree because we have pointers between leaf pages.

Implement the B+ Tree:
each page only 4 record, for each page is a sorted structure
1 page = [24,31,49,54]   the key are loaded sorted on the page
when we must add the next key 16 on the page we cannot so we split the page selecting the middle value (31 because 16 is below 31 treshold):
        [31,]
[16,24, , ]     [31,49,54, ]   31 repeted because I need the actual record

I then add the other values in the file example, 76 added easily but when i arrive to value 85 i need to split again:
        [31,54]
[16,24, , ]     [31,49, , ]     [54,76,84]

for simpledb we must refer to the left page if i have same record


Extensible hashing:
Not sorted in the pages when I insert then from the file so I obtain:
[49,24,54,31]
I apply the first hashing function on the last bit that define even or odd:
[16,24,54,76] last bit from the right = 0
[49,31,85, ] last bit from the right = 1
I have to insert 68, now with hashing I split the pages by considering another bit, of the keys in binary format, used for sorting:
[16,24,54,76] last 2 bits = 00
[49,31,85, ] last 2 bits = 01 or 11
[54, , , ] last 2 bits = 10
I go on 15 added easily, when arrive to 40 I have to add new page


Linear Hashing:
Not sorted in the pages when I insert then from the file so I obtain:
[49,24,54,31]
I have 2 additional parameter: bit representation of the point that point to the middle page
[49,24,54,31] p bit 0
Insert 15, same as before
[16,24,54,31] 
[24,54,16,76] p bit 1
[49,31,85, ] 
Adding 68 I will have an overflow page, but I split the pointer page and pointer move to the next:
[16,24,54,31] 
[24,68,16,76]  40->overflow bucket becasue last 2 bit are 0
[49,31,85,15] p 
[54, , , ]
With 40 I split the pointer page, and increase the number of bit to distinguish and i reinitialize the pointer from the beggining
[16,24,54,31] p
[24,68,16,76]-[40]
[49, ,85, ]  
[54, , , ]  
[15,31, , ]

If I have a record how can I determine if I have to use 1 bit or the last 2 bit to distinguish the values:
I use the global variable bit1 the global var is still 1. And then I see if it is after the pointer or not to check if I need more bit or not.
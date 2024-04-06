**Problem 1**

I've designed a multi-threaded simulation where several "servants" concurrently manage a collection of presents, represented by a stack of integers for their unique giftTag values, and sort them into a linked list. At the start, I fill a stack, bagOfPresents, with numbers ranging from 1 to 500,000, each number symbolizing an individual gift. Additionally, I establish a LinkedList to orderly store these gifts.

To model the servants' activities, I crafted the ServantWork class implementing Runnable. Each servant randomly picks one of three tasks: adding a gift from the stack to the linked list in a sorted manner, removing the first gift from the list as a metaphor for writing a thank you note, or searching the list for a gift by its giftTag. To log the actions undertaken by each servant, I use a PrintWriter.

Critical to this simulation is ensuring thread safety, which I achieve through the use of ReentrantLock in each node of the linked list. This precaution prevents the servants from interfering with each other’s work on the shared data structures. I then create four threads to represent the servants, setting them off to work through the stack of presents until none remain. This setup not only demonstrates managing shared resources across multiple threads but also highlights the importance of synchronization to maintain data integrity in a concurrent execution environment.

**Problem 2**


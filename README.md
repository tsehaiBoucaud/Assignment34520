**Problem 1**

I've designed a multi-threaded simulation where several "servants" concurrently manage a collection of presents, represented by a stack of integers for their unique giftTag values, and sort them into a linked list. At the start, I fill a stack, bagOfPresents, with numbers ranging from 1 to 500,000, each number symbolizing an individual gift. Additionally, I establish a LinkedList to orderly store these gifts.

To model the servants' activities, I crafted the ServantWork class implementing Runnable. Each servant randomly picks one of three tasks: adding a gift from the stack to the linked list in a sorted manner, removing the first gift from the list as a metaphor for writing a thank you note, or searching the list for a gift by its giftTag. To log the actions undertaken by each servant, I use a PrintWriter.

Critical to this simulation is ensuring thread safety, which I achieve through the use of ReentrantLock in each node of the linked list. This precaution prevents the servants from interfering with each other’s work on the shared data structures. I then create four threads to represent the servants, setting them off to work through the stack of presents until none remain. This setup not only demonstrates managing shared resources across multiple threads but also highlights the importance of synchronization to maintain data integrity in a concurrent execution environment.

**Problem 2**

This program is a multi-threaded temperature monitoring and report generating system that represents a set of temperature sensors, each recording temperature readings for a sixty-minute timeframe. By utilizing threads, the program simulates simultaneous temperature readings from multiple sensors and uses the collected data to generate a detailed report pinpointing the five warmest and coldest readings as well as the ten-minute period with the broadest temperature difference.

There are two main classes incorporated in the program. One, the Thermometer, stands for a temperature sensor recording 60 readings (one per minute), and two, the ReportCard, containing multiple Thermometer objects, symbolizing a complete temperature report. 

The recordAllTemperatures method in the Thermometer class is thread-safe because it uses std::lock_guard to ensure mutual exclusion while accessing the random temperature generator. This built-in security assures that one single thread can access this piece at any given moment, thus preventing any race conditions from occurring. The generateReportCard method in the ReportCard class consolidates temperatures from all the temperature sensors into one vector that is then sorted. This arrangement allows for a swift calculation of the five lowest and highest temperatures.

The program takes advantage of C++’s Standard Template Library (STL), which offers efficient implementations for widely used data structures and algorithms. The practice of consolidating temperature data from all sensors provides constant time access to any element into a single vector. The use of a std::sort function makes sure that the sorting operation is at most O(n log(n)), where n stands for the total number of temperature readings.

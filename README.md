# Call Center [![Build Status](https://travis-ci.org/pviotti/call-center.svg?branch=master)](https://travis-ci.org/pviotti/call-center)

This project provides a proof-of-concept object-oriented implementation of the entities that
compose a call center according to the following problem statement.

    Imagine you have a call center with three levels of employees: respondent, manager, and director.  
    An incoming telephone call must be  first allocated to a respondent who is free. 
    If the respondent can't handle the call, he or she must escalate the call to a manager. 
    If the manager is not free or not able to handle it, then the call should be escalated to a director. 
    Design the classes and data structures for this problem. 
    Implement a method `dispatchCall()` which assigns a call to the  first available employee.


## Setup and codebase overview

Requirements: Java 8 and Maven.  

To compile: `mvn compile`.  
To run the tests: `mvn test`.  
To generate the javadocs site: `mvn javadoc:javadoc`.  

The call center main logic is implemented in the `CallDispatcher` class, while
the `com.callcenter.employees` package contains the classes modeling the attributes 
and behavior of the various employees of the call center.


## Notes and possible improvements

This is an interesting study project as it presents several crucial aspects
of programming: object-oriented modeling
(e.g. in the way we use class inheritance to model the hierarchy of employees),
and concurrent programming (e.g. as we use a `ConcurrentLinkedQueue`
to share among different threads the queues of not yet serviced calls).  

Several aspects of this implementation are deliberately simplistic or suboptimal for 
large scale deployments, due to the nature and goals of this project.
In the following we hint at possible improvements.  

 * *Actor-based architecture*. A more scalable way of approaching this problem consists in modeling 
 the call center according to the [actor programming paradigm][actors]: 
 each employee is an actor with its own thread constantly checking its message box and handling the messages that it receives.
 While the asynchronous nature of this paradigm could render the code more difficult to understand,
 the lack of shared data structures would ease the burden of coping with side effects due to concurrency.
 An implementation for Java and Scala of this paradigm can be found in the [Akka][akka] library.
 Other languages, such as Erlang, implement this paradigm natively.
 
 * *Databases and message brokers*. Handling a great number of calls would require a more scalable
 way of storing calls' status and the call queues of the call center. 
 This could be accomplished by using a set of distributed workers for the employees and let them communicate 
 through a messaging/queue/pub-sub service such as RabbitMQ, Kafka, etc. 
 Clearly this would trade an improved scalability for an increased 
 complexity of the system and of its possible failure scenarios.


## License

[GPL-3][gpl3].


 [akka]: https://akka.io/
 [actors]: https://en.wikipedia.org/wiki/Actor_model
 [gpl3]: https://www.gnu.org/licenses/gpl-3.0.en.html

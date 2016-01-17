# Number26 Java Code Challenge 
-------------------------------------------
Simple RESTful service based on Jersey.

This service has been tested on **Windows 8.1 64 bits**, with **jdk1.8.0_60** and **Apache Maven 3.3.9.**

## Installation Requirements
-------------------------------------------
Basically
* **jdk1.8.0_60**.
* **Apache maven**.
* **git**.

## Compile
-------------------------------------------
    mvn clean install

## Run
-------------------------------------------
    mvn exec:java

## Test
-------------------------------------------
    mvn install -DskipTests=false
    
    A collection of Postman request can be found in the root folder of the project (transaction-service-request.json.postman_collection)
    
The application is available on port 8080.

## Additional Comments
-------------------------------------------
* External libraries used:
    * exec-maven-plugin and jersey-container-grizzly2-http: This deploys the RESTful service using an embedded Grizzly HTTP server.
    * junit:  A set of unit test has been provided for each resource. 
    * jersey-media-jackon: JSON provider for implicit conversion between POJO and JSON with Jersey.
    
* A HashMap `java.util.HashMap` has been used to store the transactions in memory.

## Asymptotic behaviour 
-------------------------------------------
Consider:

* **n**: number of transactions.

A `java.util.HashMap` has been used to store the request in memory. This class is implemented using hash tables which guarantee an amortized time of **O(1)** for get and put operations. Hence **GET transactionservice/transaction** `TransactionProvider.getTransaction` and **PUT transactionservice/transaction** `TransactionProvider.updateTransaction` will have the same cost but this is in the ideal case when the hash function distributes the objects evenly among the buckets in the hash.

But the performance may worse in the case the hashCode() used is not proper and there are lots of hash collisions until reach **O(n)** in the worst case scenario. To address this issue in **Java 8** there are some improvements to change the worst case performance from **O(n)** to **O(log n)** by using balanced trees instead of linked lists as hash buckets after a certain threshold is reached. 

On the other hand, for **GET transactionservice/sum/{transaction_id}** `TransactionProvider.getTransactionSum` we have to iterate on all the 
linked transaction until reaching one without parent_id. Within each iteration we have to get one element from the hash table. The amortized cost would be **O(n)**. However, as we said before it might degrade to **O(n*n)** in the worst case scenario if there were lots of hash collisions. Slightly better with Java 8 improvements up to **O(log(n)*n)**

Finally, **GET transactionservice/types/{type}** `TransactionProvider.getTransactionIds` iterates on every transaction and compares each String type with the requested 
one. Whenever an element that matches type is found it is inserted into the result list `java.util.ArrayList`. If we can consider String comparison negligible then the cost will depend on the cost inserting in the `java.util.ArrayList` plus iterating on n transactions. `java.util.ArrayList` has an amortized cost of **O(1)** and hence the total amortized cost would be **O(n)** worse case up to **O(n*n)**.

To sum up:

Operation | Amortized cost | Worst case 
------------ | ------------- | ------------ 
GET transaction | O(1) | O(n)
PUT transaction | O(1) | O(n)
GET sum | O(n) | \*O(n*n)
GET types | O(n) | O(n*n)

 \*In Java 8, O(log(n)*n)
 

## Improvements
-------------------------------------------
* Performance of **GET transactionservice/types/{type}** could be improved up to an amortized cost of **O(1)** by adding another Hash table using the transaction type as a key.
* No white box testing.

## For any question please contact me at 
-------------------------------------------
fcojavierob@gmail.com

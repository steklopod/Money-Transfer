# Money Transfer REST API

*Technologies*: `Kotlin`, `Gradle`, `SparkJava`, `H2 Database`, `Exposed`, `JUnit`.  

### Requirements:

1. You can use Java, Scala or `Kotlin`;
2. Keep it simple and to the point (_e.g. no need to implement any authentication_);
3. Assume the API is invoked by multiple systems and services on behalf of end users;
4. You can use frameworks/libraries if you like (_except Spring_), but don't forget about
requirement #2 – keep it simple and avoid heavy frameworks;
5. The datastore should run in-memory for the sake of this test;
6. The final result should be executable as a _standalone program_ (should not require
a pre-installed container/server);
7. Demonstrate with tests that the API works as expected.

## Run:

    ./gradlew build
    java -jar build/libs/MoneyTransfer-all.jar


## Examples:
* See all accounts: [localhost:4567/accounts](http://localhost:4567/accounts)
```json
[
  {
    "id": 1,
    "money": 100
  },
  {
    "id": 2,
    "money": 200
  }
]
```

* See account by id: [localhost:4567/accounts/1](http://localhost:4567/accounts/1)
```json
  {
    "id": 1,
    "money": 100
  }
```    
   
* Create account:
    
        curl -d "money=3" -X POST http://localhost:4567/accounts/create
        
* Transfer money:      

      curl -d "fromId=1&toId=2&amount=33" -X POST http://localhost:4567/transfer
      
      
## Testing:

    ./gradlew test


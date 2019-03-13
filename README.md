# Money Transfer REST API

Technologies used: `Kotlin`, `Gradle`, `SparkJava`, `H2 Database`, `Exposed`, `JUnit`.  


## Run:

    gradle build
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

    gradle test


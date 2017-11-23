# Sample of a distributed system
Application  base elements are listed below:

`- client.java `  client part of the application

`- proxy.java `  an intermediary between client and nodes, who is keepeing a stable communication between them

`- node.java `  an independent element, that has a *payload*

`- main.java` responsible for running the nodes

`- json_schema.json `  a json validation schema

### Transport protocols used
`TCP` for data interchange

`UDP` for node discovery

### Order of running the elements
Its recommended to start the `Proxy` first, then `Main` file, that manages to run the nodes. 
Then lastly we will need to start the client side, and follow the instructions on console.



# WhiteBoard
Distributed Shared White Board

## Introduction
A distributed shared whiteboard which allows multiple users to connect to the shared whiteboard and draw different shapes on the canvas.

## How to run
Run server

```sh
$ java -jar CreateWhiteBoard.jar <ip> <port> username
```

Run client

```sh
$ java -jar JoinWhiteBoard.jar <ip> <port> username
```

## Functionality
1. Users can draw different shapes and enter text with different colours on the whiteboard concurrently
2. Each connection will require the manager to approve
3. People can chat using chat box
4. Manager can empty the whiteboard, open an existing whiteboard, save the current whiteboard and close the whiteboard
5. Manager can kick user out for bad behaviour 

## Interface
The interface is shown below. Should be very straightforward to use.  :)

### Manager 
<img width="617" alt="image" src="https://user-images.githubusercontent.com/88305416/208579645-5abce681-b0bd-42ac-a49c-29c23a54157e.png">

### User
<img width="617" alt="image" src="https://user-images.githubusercontent.com/88305416/208579696-2c98077c-ec4c-4864-a03f-a4f4d4ec32e4.png">

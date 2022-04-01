# Blocky

An interactive, hands-on application for building block ciphers round by round.

### What will the application do?
Cryptography plays a crucial role in securing our digital communications; this application focuses on symmetric block ciphers, where messages are encrypted and decrypted with a shared key between two parties.

Users can create block ciphers by iteratively adding encryption rounds, made up of custom substitution and transposition ciphers (see [Substitution-Permutation Networks](https://en.wikipedia.org/wiki/Substitution%E2%80%93permutation_network)). Moreover, the application allows users to freely encrypt and decrypt data (using their custom cipher), in addition to saving and loading predefined ciphers.

### Who will use it?
Anyone who:
- wants to play around with small-scale ciphers
- learn about the inner workings of standard encryption algorithms
- curious about cryptography in general.


### Why is this project of interest to you?
When I first came across cryptography, I found the whole idea of encryption fascinating – securely transmitting messages meant for only the person on the other side. Though the math and calculations seem daunting at first, even *military-grade* encryption is built up from smaller operations, following principles of confusion and diffusion. With this project, I hope to connect the individual 0's and 1's to the complex algorithms that modern ciphers rely upon.

### User Stories
As a user, I want to be able to:

#### Phase 1
- add custom rounds to an existing cipher
- choose the type of round and relevant mappings
- view the rounds of a cipher
- encrypt data with an existing cipher
- decrypt data with an existing cipher

#### Phase 2
- save all components (rounds, blocksize) of a cipher to a file
- choose between "create" or "load existing" cipher on start

#### Phase 4: Task 2
```
Wed Mar 30 15:10:44 PDT 2022
Encrypted Message

Wed Mar 30 15:10:48 PDT 2022
Decrypted Message

Wed Mar 30 15:10:56 PDT 2022
Added Mix Key Round

Wed Mar 30 15:10:59 PDT 2022
Added Mix Key Round

Wed Mar 30 15:11:03 PDT 2022
Added Substitution Round

Wed Mar 30 15:11:07 PDT 2022
Added Permutation Round

Wed Mar 30 15:11:15 PDT 2022
Encrypted Message

Wed Mar 30 15:11:23 PDT 2022
Decrypted Message
```

#### Phase 4: Task 3

The design consists of 4 distinct sections:
- A model package that stores the different types of rounds and the interfaces they implement
- A persistence package with JsonWriter and JsonReader
- A ui package that associates with the classes above
- Event-EventLog classes that log the actions performed by the user

Overall, I thought that the classes had high cohesion:
- The MixKeyRound/PermutationRound/SubstitutionRound classes are each responsible for a single functionality
- Similarly, JsonReader and JsonWriter are cohesive and focus on loading/saving files
- Event and EventLog also accomplish a single task

Further refractions could include:
- Making Round also implement the Writable interface, so that there's less coupling within the Round classes
- The substitution and permutation operations are quite similar in terms of user input/output; I could reduce duplication by abstracting shared ui actions into separate methods. For example, the `AddPermutationRound` and `AddSubstitutionRound` share similar implementations.
- Additionally, I could reduce duplication by making SubstitutionRound and PermutationRound extend a common abstract class, under the Round interface
- The CipherUI class currently contains multiple inner classes for each button action. Cohesion could be improved by placing these inner classes into separate ui classes, so that each class has a single responsibility.

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
# Gnutella-Network File sharing method
Project:

Gnutella-style peer-to-peer (P2P) system: Each peer should be both a server and a client. As a client, it provides interfaces through which users can issue queries and view search results. As a server, it accepts queries from other peers, checks for matches against its local data set, and responds with corresponding results. In addition, since there's no central indexing server, search is done in a distributed manner. Each peer maintains a list of peers as its neighbor. Whenever a query request comes in, the peer will broadcast the query to all its neighbors in addition to searching its local storage (and responds if necessary).

![mesh topology](https://user-images.githubusercontent.com/15329382/42832996-d31c3330-89a7-11e8-990c-c64ddf2c9d8e.gif)


Implementation:
Project provides a static network implementation of Gnutella. We assume that the structure of the P2P network is static. Declare the neighbor peer details in the config.properties file.

Performance Testing:
Assume star network of 3X3 peers. Find the response time of Peer1 for 1000 requests, 
-When it is the only peer performing the search in the network?
-When there is one more peer performing the search in the network?
-When there are two more peers performing the search in the network?

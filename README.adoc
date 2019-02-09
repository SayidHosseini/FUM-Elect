== FUM Elastic Election System ==
This project consists of multiple microservices that together serve as an elastic (Containerized cluster - swarm) election (electronic voting - i-vote) system.
The master branch includes x86-64 architecture docker-compose file and the arm branch includes arm architecture docker-compose file.

++++
<p align="center">
<img src="https://github.com/SayidHosseini/FUM-Elect/blob/master/ElasticElectionArchitecture.jpg"/>
<br/>
<strong>Figure 1:</strong> The Underlying Architecture </p>
++++


=== Here are the key components and the link to their source: ===
* *https://github.com/SayidHosseini/fumelectElectionPanel[Dynamic Web Panel Service]*
* *https://github.com/SayidHosseini/fumelectAuth[Authentication Service]*
* *https://github.com/ardalanfp/cloudProjectMaster[Master]*
* *https://github.com/ardalanfp/cloudProjectElectionPortal[Election Portal]*
* *https://github.com/ardalanfp/cloudProjectValidator[Fraud Detection]*

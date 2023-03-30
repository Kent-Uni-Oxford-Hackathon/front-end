# Knowledge Token Hackathon - Front End Challenge

## Our Team

### Joseph Kearney 

Joseph is a 3rd year PhD candidate expecting to complete in September 2023. Having graduated with an MsC in Computer Science from the University of Kent he worked as Blockchain researcher at Atlas City designing a novel blockchain system. His PhD has focused on the impact of quantum computation on blockchain technologies with research in the area of [security concerns](https://scholar.google.com/citations?view_op=view_citation&hl=en&user=txAlj-AAAAAJ&authuser=1&citation_for_view=txAlj-AAAAAJ:d1gkVwhDpl0C), the use of quantum devices as [Proof of Work miners](https://scholar.google.com/citations?view_op=view_citation&hl=en&user=txAlj-AAAAAJ&authuser=1&citation_for_view=txAlj-AAAAAJ:9yKSN-GCB0IC) and is currently looking at the energy expenditure of a quantum mining device. 

### Ben Martin

Ben Russell Martin is a software engineer with 10 years+ experience in full stack and a passion for back-end and platform engineering. Professional experience working at the International Telecommunications Company: Sky, he has been able to accelerate his understanding into modern technology and development practices -- TDD (Test Driven Development) and BDD (Behaviour Driven Development) -- which has pushed his projected grade at graduation to 1st Class with Hons.

### Gopinath Gnallingam

Gopinath Gnallingam is a third-year computer science student at the University of Kent, with 5+ years in Software Development and a passion for blockchain technology. Gopinath's academic performance has been consistently impressive, with expectations of graduating with first-class honours.

## What was accomplished 

During the 3 day hackathon we completed the Front End challenge. During this time however we also implemented a majority of the backend functionality. 

### Features

#### Login and Signup 

A login and signup system has been developed. This contains the functionality so that upon account creation an EOA(Externally Owned Account) is created for the user on the Ethereum blockchain to which tokens can be sent. For the sake of demonstration this functionality has been set so that an account created is paired with one of our existing [accounts](https://sepolia.etherscan.io/address/0xd05b7dc35264a651cf0baf51b9f26adcf103c824). 

#### Dashboard

A dashboard has been created to allow users to view their NFT tokens that are associated with their EOA. Links are also provided so that users can view their account and specific tokens on the Block Explorer Etherscan. This is dynamically genrated, so that if another client sends the token the dashboard will automatically update. All tokens generated are immutably stored currently on the Sepolia test net. 

### Technologies Used



## Future Work

## Server Service Application
### Running
- JDK 17 or newer required
```shell
./gradlew bootRun
```
UI can be accessed on http://localhost:8080/

# README

## Notes

### GET /auctionItems
I think pagination should be implemented here, but there was
no example request parameters to control the pagination. Therefore
I went for a full join on the tables to return everything. This is
bad as the app scales.

### TODO
* Elasticsearch for bid history
* Caching layer
* Authentication with Spring Security
* UML diagrams
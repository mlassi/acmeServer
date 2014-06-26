acmeServer
==========
mvn spring-boot:run


Ads
GET: curl http://localhost:8080/ads/1

POST: curl -i -X POST -H "Content-Type:application/json" 
-d '{  "title":"good maid", "description":"good at cleaning, cooking is a plus"} ' http://localhost:8080/ads

DELETE:  curl -i -X DELETE -H "Content-Type:application/json"  http://localhost:8080/ads/3

Publish Ad: curl -i -X POST -H "Content-Type:application/json" -d '{ "id": 5, "publicationName": "Angular JS in Action"} ' http://localhost:8080/ads/14/newspapers

Newspapers


POST: 
tionName":"Java Magazine"} ' http://localhost:8080/newspapers

PUT: curl -i -X PUT -H "Content-Type:application/json" -d '{ "id":3, "publicationName":"Java Magazine 2"} ' http://localhost:8080/newspapers



DELETE:  curl -i -X DELETE -H "Content-Type:application/json"  http://localhost:8080/newspapers/4

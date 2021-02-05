// RESTAURANTS DB TABLE QUESTIONS

//Q.1
cursor=db.restaurants.find({"name":"Caffe Dante"},{"restaurant_id":1});
//Q.2
cursor=db.restaurants.find({"name": /.*Steak.*/},{"restaurant_id":1,"name":1});
//Q.3
cursor=db.restaurants.find({$and:[{$or:[{"cuisine":"Italian"},{"cuisine":"American"}]},{"borough":"Brooklyn"}]},{"name":1});
//Q.4
cursor=db.restaurants.aggregate({"$group":{_id:"$borough",count:{$sum:1}}},{$sort:{count:-1}});
//Q.5
cursor=db.restaurants.aggregate([{$match:{"cuisine":"Chinese","borough":"Manhattan"}},{$unwind:"$grades"},{"$group":{_id:"$name","total":{$sum:"$grades.score"}}},{$sort:{"total":-1}},{$limit:5}]);
//Q.6
cursor=db.restaurants.find({$and:[{"grades.score":{$gt:70}},{"address.coord":{$geoWithin:{$box:[[-74, 40.5],[-73.5, 40.7]]}}}]}).count();

// ZIPS DB TABLE QUESTIONS

//Q.7
cursor=db.zips.find({},{"_id":1,"city":1,"state":1}).sort({pop:-1}).limit(10);
//Q.8
cursor=db.zips.aggregate([{$group:{_id:{state:"$state",city:"$city"},pop:{$sum:"$pop"}}},{$sort:{pop:-1}},{$group:{_id:"$_id.state",bigCity:{$first:"$_id.city"}}}]);
// 9
cursor=db.zips.aggregate([{$group:{_id:{state:"$state",city:"$city"},pop:{$sum:"$pop"}}},{$group:{_id:"$_id.state",avgCityPop:{$avg:"$pop"}}},{$match:{"avgCityPop":{$gt:10000}}}]);
//Q.10
cursor=db.zips.ensureIndex({loc:"2d"}); 
cursor=db.zips.find({loc:{$near:[-70,40]}}).limit(5); 
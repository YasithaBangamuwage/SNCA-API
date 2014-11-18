SNCA-API
========
How to Access SNCA-API Restfull web services

1)To filter bussiness category for a given user details.

http://your_server_location_url/SocialNetworkContentAnalyzerAPI/access/filterCategories/registered_user_id

1)To filter bussiness category for a registered user's search query.

http://your_server_location_url/SocialNetworkContentAnalyzerAPI/access/filterCategories/registered_user_id/search_string

1)To filter bussiness category for a un registered user's search query.

http://your_server_location_url/SocialNetworkContentAnalyzerAPI/access/filterCategories/search_string

How to use AdsInsite SNCA-API services
======================================

1) You have to register users into AdsInsite.
2) Call above web service urls with currect userids and search strings. 

Configurations
=============

1)Add your database name and database user detailsinto DAO pacakge connection class.
2)Run Following sql script in your database.

CREATE TABLE IF NOT EXISTS filtered_feeds 
(
	filtered_id INT AUTO_INCREMENT,
	user_id INT,
	date DATE,
	filtered_feed VARCHaR(1000),
	classified_ads VARCHaR(100),
	
	PRIMARY KEY (filtered_id),
	FOREIGN KEY (user_id) REFERENCES users(user_id)
);




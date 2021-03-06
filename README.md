# product-api 

Note - Please install java 8 and maven 3.x before proceeding with the below steps-

### 1. Building the source code-

a) git clone https://github.com/kalygo/product-api.git
<br>
b) mvn clean package

This generates the artifact product-api-0.0.1-SNAPSHOT.jar in the directory product-api/target/

### 2. Installing a local instance of Cassandra and setting up the keyspaces and tables-

a) Download Apache Cassandra 3.11 from http://cassandra.apache.org/download/
<br>
b) Copy the downloaded file and unzip to local directory
<br>
c) Run {cassandra installation dir}/bin/cassandra - This will bring a local instance of Cassandra node
<br>

To create the product price keyspace and load some sample data-
<br>
d) Run {cassandra installation dir}/bin/cqlsh -f product-api/src/main/resources/cql/product_ks.cql
<br>
e) Run {cassandra installation dir}/bin/cqlsh -f product-api/src/main/resources/cql/sample_data_load.cql
<br>
### 3. Running the micro service

a) Use command -
<br>
java -jar -Dcassandra.contactpoints=localhost -Dcassandra.port=9042 -Dspring.profiles.active=dev target/product-api-0.0.1-SNAPSHOT.jar
<br>
b) The API documentation will be available at http://{hostname}:8081/docs/index.html 
Or Please download https://github.com/kalygo/product-api/blob/master/index.html and open in a web browser


# TUTORIAL KAFKA DEBEZIUM MYSQL (INDONESIAN)

## 0. instal Mysql di docker pakai sample sudah ada datanya & konfigurasi binlog
```
> docker run -it --name mysqldbz -p 3306:3306 -e MYSQL_ROOT_PASSWORD=debezium -e MYSQL_USER=mysqluser -e MYSQL_PASSWORD=mysqlpw debezium/example-mysql:0.5

> docker exec -it mysqldbz /bin/bash

> more /etc/mysql/conf.d/mysql.cnf

log_bin           = mysql-bin
expire_logs_days  = 1
binlog_format     = row
```
> Note: bin log digunakan untuk mengambil change data capture (CDC) dari mysql

## 1. Download kafka & extract
```
> wget https://www.apache.org/dyn/closer.cgi?path=/kafka/2.4.1/kafka_2.12-2.4.1.tgz
> tar -xzf kafka_2.12-2.4.1.tgz
```
## 2. Masuk ke folder kafka & create directory konektor
```
> cd kafka_2.12-2.4.1
> mkdir connect
> cd connect
```

## 3. download debezium connector untuk mysql to kafka
```
> wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-mysql/0.8.3.Final/debezium-connector-mysql-0.8.3.Final-plugin.tar.gz
```

## 4. extract debezium connector
> tar -xzf debezium-connector-mysql-0.8.3.Final-plugin.tar.gz

## 5. copy file distributed.properties jadi debezium.properties di folder config
> cd ../config
> cp connect-distributed.properties debezium.properties

## 6. di file debezium.properties masukkan file path untuk connector
> vim debezium.properties
        plugin.path=$KAFKA_HOME/connect

## 7. start zookeeper, kafka, connector
>cd $KAFKA_HOME
>bin/zookeeper-server-start.sh config/zookeeper.properties
>bin/kafka-server-start.sh config/server.properties
>bin/connect-distributed.sh config/debezium.properties

## 8. daftarin konfigurasi konektor via rest api
POST localhost:8083/connectors
body=
{
  "name": "inventory-connector",
  "config": {
    "connector.class": "io.debezium.connector.mysql.MySqlConnector",
    "tasks.max": "1",
    "database.hostname": "localhost",
    "database.port": "3306",
    "database.user": "debezium",
    "database.password": "dbz",
    "database.server.id": "184054",
    "database.server.name": "dbserver1",
    "database.whitelist": "inventory",
    "database.history.kafka.bootstrap.servers": "localhost:9092",
    "database.history.kafka.topic": "schema-changes.inventory"
  }
}

## 9. coba! ubah data di table inventory.customers 
result:

cek di topik = dbserver1.inventory.customers
```
....
"payload": {
		"before": {
			"id": 1003,
			"first_name": "Edward",
			"last_name": "Walker",
			"email": "ed@walker.com"
		},
		"after": {
			"id": 1003,
			"first_name": "Edward",
			"last_name": "Paijon",
			"email": "ed@walker.com"
		},
....
```
bisa dilihat dari program simple spring boot: github: https://github.com/erfinfeluzy/training-kafka-debezium 
```
mvn spring-boot:run
```

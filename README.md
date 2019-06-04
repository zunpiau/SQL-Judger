## 软件要求
- JDK 11
- Maven 3
- PostgreSQL 11
- yarn 1.15

## 编译
```
# cd ./src/main/resources/web
yarn install
yarn build
```
```
# cd -
mvn clean
mvn package
```

## 运行
数据库表结构在 [schema.sql](https://github.com/zunpiau/SQLJudger/blob/master/schema.sql)
```
java -jar target/sqljudger-0.0.1-SNAPSHOT.jar
```

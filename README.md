# essl-attendance-report-generator
Generates attendance report in Microsoft excel from biometric attendance logs.

## Data
Since `essl-attendance-report-generator` is a small utility to generate an attendance report, it is designed to accept input data as files in json formats defined under `data` directory.

```
$ find src/universal/data/
src/universal/data
src/universal/data/1_attlog.dat
src/universal/data/holidays.json
src/universal/data/requests.json
src/universal/data/employees.json
```

## Config
Also the appilcation can be configured to generate reports for any given month and year via `application.conf`.
```
$ cat src/universal/conf/application.conf
esslattendancereportgenerator {
  month = 04
  year = 2019
}
```

## Dev
```
$ cd essl-attendance-report-generator
$ sbt> universal:stage 

$ cd ./target/universal/stage/logs
$ ../bin/start.sh
$ tail -f stdout.log essl-attendance-report-generator.log
```

## Build Package
```
$ cd essl-attendance-report-generator
$ sbt> universal:packageBin

$ cd ./target/universal/stage/logs
$ ../bin/start.sh
$ tail -f stdout.log essl-attendance-report-generator.log
```

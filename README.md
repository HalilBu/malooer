# Malooer

Malooer is a command-line tool for logging the connectivity state of a mail server. Currently only _SMTP_ is supported.

### Usage:
If you want to log the status to a file, then use the following command:
```
java -jar malooer-<v>.jar -host <HOST> -port <PORT> -user <USER> -pwd <PASSWORD> >> malooer.log
```
This will start Malooer and log the output to _**malooer.log**_.

The default interval for checking the connection to the server is 5 minutes. To change this you have to pass the **-i** parameter with the desired value (in seconds).

## [License](LICENSE)

This project is licensed under the terms of the [MIT license](LICENSE).

# AddressBookReset
Clear the Address Book entries from a Radix Node database

## Building

Create `.jar` file in `build/libs` with the following command

```
./gradlew jar
```

## Usage

1. Stop the Radix node
2. Run the following command

```
java -jar AddressBookReset.jar /path/to/db
```
3. Start the Radix node

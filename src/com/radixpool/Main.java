package com.radixpool;

import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableList;
import com.sleepycat.je.*;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        var dbHome = new File(args[0]);

        var environmentConfig = new EnvironmentConfig();
        environmentConfig.setReadOnly(false);
        environmentConfig.setTransactional(true);

        var environment = new Environment(dbHome, environmentConfig);

        var mapper = new CBORMapper();
        mapper.registerModule(new GuavaModule());
        mapper.registerSubtypes(ImmutableList.class);

        Transaction transaction = null;

        try {
            transaction = environment.beginTransaction(null, new TransactionConfig().setReadUncommitted(true));
            var count = environment.truncateDatabase(transaction, "address_book_entries", true);
            transaction.commit();

            System.out.print("Address Book Entries cleared: ");
            System.out.println(count);

        } catch (DatabaseNotFoundException dnfex) {
            if (transaction != null) {
                transaction.abort();
            }
            System.out.println("Database not found");
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.abort();
            }
            throw new IllegalStateException("while resetting database address book", ex);
        }
        environment.close();
    }
}

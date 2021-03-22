#!/usr/bin/python
import collections
import datetime
import sys

class Transaction(object):
    def __init__(self):
        self.trans_db = collections.defaultdict(list)                # Store the key-value pair that is modified in an active transaction.
        self.trans_log = collections.defaultdict(set)                # Store the keys that are modified by open transactions.
        self.trans_ids = []                                          # The array saves transaction ids to remember the order that transactions were started.

    def begin(self):
        """Start a new transaction
            
        Create a new timestamp as the transaction id and save it to trans_ids.
        """
        transaction_id = '{:%Y-%m-%d %H:%M:%S}'.format(datetime.datetime.now())
        self.trans_ids.append(transaction_id)

    def is_open(self):
        """Check if there is any active transaction"""
        return len(self.trans_ids) != 0

    def is_get_from_transaction(self, key):
        """Check if should get value from transaction table"""
        return key in self.trans_db

    def set(self, key, value):
        """Save key - value pair into primary db.

        Args:
            key: stdin variable name
            value: stdin value value
            trans_id: Current transaction id. 
        """
        trans_id = self.trans_ids[-1]
        self.trans_log[trans_id].add(key)
        self.trans_db[key].append((value, trans_id))

    def get(self, key):
        """Read variable's value from transaction db, print 'NULL' if value is None.

        Args:
            key: stdin variable name
        """
        value = None
        if key in self.trans_db:
            return self.trans_db[key][-1][0] if self.trans_db[key][-1][0] else 'NULL'

    def begin(self):
        """Open a new transaction block
        
        Create a timestamp as the current transaction's transaction id and save it into the trans_ids.
        """
        transaction_id = '{:%Y-%m-%d %H:%M:%S}'.format(datetime.datetime.now())
        self.trans_ids.append(transaction_id)

    def rollback(self):
        """Undo all of the commands issued in the most recent transaction block, and close the block.

        If there is no open transaction, print 'NO TRANSACTION'.
        If there is an open transaction, remove the values that is modified by the current transaction.
        """
        trans_id = self.trans_ids.pop()
        for modified_key in self.trans_log[trans_id]:
            # Go through each of the values, remove if the value is modified by the current transaction.
            while self.trans_db[modified_key] and self.trans_db[modified_key][-1][1] == trans_id:
                self.trans_db[modified_key].pop()

            # if the key has no value, remove the key from trans_db
            if self.trans_db[modified_key] == []:
                del self.trans_db[modified_key]
        del self.trans_log[trans_id]

    def get_commit_names(self):
        """Get a list of variable names that need to set the values to primary db"""
        return self.trans_db.keys()

    def close_transaction(self):
        """Reset transaction db dict, transaction log dict and transaction id array"""
        self.trans_db.clear()
        self.trans_log.clear()
        del self.trans_ids[:]

class Primary(object):
    def __init__(self):
        self.primary_db = {}

    def set(self, key, value):
        """Save key - value pair into primary db.

        Args:
            key: stdin variable name
            value: stdin value value
        """
        self.primary_db[key] = value

    def get(self, key):
        """Read variable's value from primary db, print 'NULL' if value is not found.

        Args:
            key: stdin variable name
        """
        if key in self.primary_db: 
            return self.primary_db[key]
        return 'NULL'

class Store(object):
    def __init__(self):
        self.primary = Primary()                                        # Primary key-value store.
        self.transaction = Transaction()                                # Transaction key-value store.

    def set(self, name, value):
        """Save key - value pair into store system.

        If in an open transaction, save the key-value pair into transaction db.
        If out of a transaction, save the key-value pair into primary db.

        Args:
            name: stdin variable name
            value: stdin value value
        """
        if self.transaction.is_open():
            self.transaction.set(name, value)
        else:
            self.primary.set(name, value)

    def get(self, name):
        """Read variable's value from store system.

        Get the variable's value from transaction db if it exists,
        otherwise get it from primary db.

        Args:
            name: stdin variable name
        """
        if self.transaction.is_get_from_transaction(name):
            return self.transaction.get(name)
        else:
            return self.primary.get(name)

    def unset(self, name):
        """Unset the variable name, making it just like that variable was never set.
        
        If in an open transaction, set the variable value as None in transaction db.
        If out of a transaction, set the variable value as None in primary db.

        Args:
            name: stdin variable name
        """
        if self.transaction.is_open():
            self.transaction.set(name, None)
        else:
            self.primary.set(name, None)

    def numwithvalue(self, value):
        """Return the number of variables that are currently set to value.

        Go through each of the variable in transaction and primary db, count number of
        variables that set to the value.

        Args:
            value: stdin value.
        """
        res = 0
        for key in self.transaction.trans_db:
            if self.get(key) == value:
                res += 1
        for key in self.primary.primary_db:
            if key not in self.transaction.trans_db and self.primary.get(key) == value:
                res += 1
        return res

    def begin(self):
        """Open a new transaction block"""
        self.transaction.begin()

    def rollback(self):
        """Undo all of the commands issued in the most recent transaction block, and close the block."""
        if not self.transaction.is_open():
            print 'NO TRANSACTION'
            return
        self.transaction.rollback()

    def commit(self):
        """Close all open transaction blocks, permanently applying the changes made in them.

        If there is no open transaction, print 'NO TRANSACTION'
        If there is any open transaction, save key-value pairs into primary db and reset transaction record.
        """
        if not self.transaction.is_open():
            print 'NO TRANSACTION'
            return
        commit_keys = self.transaction.get_commit_names()
        for key in commit_keys:
            self.primary.set(key, self.get(key))
        self.transaction.close_transaction()

    def end(self):
        """Exit the program."""
        return 'END'

    def process_command(self, command_args):
        """Process a command line and execute operations on key-value store system"""
        command = command_args[0]
        if command == 'SET':
            self.set(command_args[1], command_args[2])
        elif command == 'GET':
            print self.get(command_args[1])
        elif command == 'UNSET':
            self.unset(command_args[1])
        elif command == 'NUMWITHVALUE':
            print self.numwithvalue(command_args[1])
        elif command == 'END':
            return self.end()
        elif command == 'BEGIN':
            self.begin()
        elif command == 'ROLLBACK':
            self.rollback()
        elif command == 'COMMIT':
            self.commit()
        else:
            pass

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
        transaction_id = '{:%Y-%m-%d %H:%M:%S}'.format(datetime.datetime.now())
        self.trans_ids.append(transaction_id)

    def is_open(self):
        return len(self.trans_ids) != 0

    def is_get_from_transaction(self, key):
        return key in self.trans_db

    def set(self, key, value):
        trans_id = self.trans_ids[-1]
        self.trans_log[trans_id].add(key)
        self.trans_db[key].append((value, trans_id))

    def get(self, key):
        value = None
        if key in self.trans_db:
            return self.trans_db[key][-1][0] if self.trans_db[key][-1][0] else 'NULL'

    def begin(self):
        transaction_id = '{:%Y-%m-%d %H:%M:%S}'.format(datetime.datetime.now())
        self.trans_ids.append(transaction_id)

    def rollback(self):
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
        return self.trans_db.keys()

    def close_transaction(self):
        self.trans_db.clear()
        self.trans_log.clear()
        del self.trans_ids[:]

class Primary(object):
    def __init__(self):
        self.primary_db = {}

    def set(self, key, value):
        self.primary_db[key] = value

    def get(self, key):
        if key in self.primary_db: 
            return self.primary_db[key]
        return 'NULL'

class Store(object):
    def __init__(self):
        self.primary = Primary()                                        # Primary key-value store.
        self.transaction = Transaction()                                # Transaction key-value store.

    def set(self, name, value):
        if self.transaction.is_open():
            self.transaction.set(name, value)
        else:
            self.primary.set(name, value)

    def get(self, name):
        if self.transaction.is_get_from_transaction(name):
            return self.transaction.get(name)
        else:
            return self.primary.get(name)

    def unset(self, name):
        if self.transaction.is_open():
            self.transaction.set(name, None)
        else:
            self.primary.set(name, None)

    def numwithvalue(self, value):
        res = 0
        for key in self.transaction.trans_db:
            if self.get(key) == value:
                res += 1
        for key in self.primary.primary_db:
            if key not in self.transaction.trans_db and self.primary.get(key) == value:
                res += 1
        return res

    def begin(self):
        self.transaction.begin()

    def rollback(self):
        if not self.transaction.is_open():
            print 'NO TRANSACTION'
            return
        self.transaction.rollback()

    def commit(self):
        if not self.transaction.is_open():
            print 'NO TRANSACTION'
            return
        commit_keys = self.transaction.get_commit_names()
        for key in commit_keys:
            self.primary.set(key, self.get(key))
        self.transaction.close_transaction()

    def end(self):
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

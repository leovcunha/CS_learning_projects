# -*- coding: utf-8 -*-
"""
Created on Fri Feb 10 11:24:09 2017

@author: c055867
"""

# -*- coding: utf-8 -*-
import socket
import sys
import utils
import select


class Client(object):

    def __init__(self, name, address, port):
        self.name = name
        self.address = address
        self.port = int(port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.buffer = ""
        try:
            self.socket.connect((self.address, self.port))
            self.send(self.name)
        except:
            print utils.CLIENT_CANNOT_CONNECT.format(self.address, self.port)
            sys.exit()

    def send(self, message):
        if len(message) < utils.MESSAGE_LENGTH:
            message = message.ljust(utils.MESSAGE_LENGTH)
        self.socket.send(message)

    def start(self):
        sys.stdout.write(utils.CLIENT_MESSAGE_PREFIX)
        sys.stdout.flush()
        while True:
            ready_to_read, ready_to_write, in_error = select.select(self.socket, [], [], 0)

            for read_in in ready_to_read:
                if read_in == sys.stdin:
                    message = read_in.readline()
                    message = message.strip('\n')
                    self.send(message)
                    sys.stdout.write(utils.CLIENT_MESSAGE_PREFIX)
                    sys.stdout.flush()
                else:
                    message = read_in.recv(utils.MESSAGE_LENGTH)

                    if message:
                        self.buffer += message
                        if len(self.buffer) >= utils.MESSAGE_LENGTH:
                            sys.stdout.write(utils.CLIENT_WIPE_ME + "\r")
                            sys.stdout.write(self.buffer[: utils.MESSAGE_LENGTH].lstrip() + "\n")
                            sys.stdout.write(utils.CLIENT_MESSAGE_PREFIX)
                            sys.stdout.flush()
                            self.buffer = self.buffer[utils.MESSAGE_LENGTH:]

                    else:
                        sys.stdout.write(utils.CLIENT_WIPE_ME + "\r")
                        sys.stdout.write(utils.CLIENT_SERVER_DISCONNECTED.format(self.address, self.port))
                        sys.stdout.flush()
                        sys.exit()




args = sys.argv
if len(args) != 4:
    print "Please supply a server address and port."
    sys.exit()
client = Client(args[1], args[2], args[3])
client.start()
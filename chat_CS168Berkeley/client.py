# -*- coding: utf-8 -*-
"""
Created on Fri Feb 10 11:24:09 2017

@author: leovcunha
"""

# -*- coding: utf-8 -*-
import socket
import sys
import utils
import select
from client_split_messages import pad_message

class Client(object):

    def __init__(self, name, address, port):
        self.name = name
        self.address = address
        self.port = int(port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.buffer = ""
        self.socket.settimeout(2)
        self.socket_list = []
        try:
            self.socket.connect((self.address, self.port))
        except:
            print utils.CLIENT_CANNOT_CONNECT.format(self.address, self.port)
            sys.exit()

    def start(self):
        sys.stdout.write(utils.CLIENT_MESSAGE_PREFIX)
        sys.stdout.flush()
        self.socket.sendall(pad_message(self.name))
        while True:
            self.socket_list = [sys.stdin, self.socket]

            ready_to_read, ready_to_write, in_error = select.select(self.socket_list, [], [])

            for sock in ready_to_read:
                if sock == self.socket:
                    data = sock.recv(4096)
                    if not data:
                        print "/n "+ utils.CLIENT_SERVER_DISCONNECTED.format(self.address, self.port)
                        sys.exit()
                    else:
                        data = data.rstrip(' ')
                        sys.stdout.write(utils.CLIENT_WIPE_ME)
                        sys.stdout.write("\r" + data)
                        sys.stdout.write(utils.CLIENT_MESSAGE_PREFIX); sys.stdout.flush()   
                else:
                    msg = sys.stdin.readline()
                    self.socket.sendall(pad_message(msg))
                    sys.stdout.write(utils.CLIENT_MESSAGE_PREFIX)
                    sys.stdout.flush()





args = sys.argv
if len(args) != 4:
    print 'Usage : python client.py username hostname port'
    sys.exit()
client = Client(args[1], args[2], args[3])
client.start()
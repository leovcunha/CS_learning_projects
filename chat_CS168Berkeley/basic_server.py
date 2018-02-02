# -*- coding: utf-8 -*-
"""
Created on Wed Feb 08 09:57:50 2017

@author: c055867
"""

import socket
import sys


class BasicServer(object):


    def __init__(self, port):
        self.port = int(port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
#        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket.bind(('', self.port))
        self.socket.listen(5)
#        self.socket.setblocking(0)
           
        
    def start(self):
        (clientsock, addr) = self.socket.accept()
        while 1:
            data = clientsock.recv(1024)
            if not data: break
            print data


if __name__ == '__main__':
    args = sys.argv
    if len(args) != 2:
        print "Please supply a server port."
        sys.exit()
    server = BasicServer(args[1])
    server.start()

#    if not data: break
#    print data
#    server.close()

    

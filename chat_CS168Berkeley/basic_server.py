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
        self.socket = socket.socket()
#        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket.bind(('', self.port))
        self.socket.listen(1)
        
    def start(self):
        sockfd = None
        print "server started" 
        while 1:
                sockfd, addr = self.socket.accept()
                print 'Connected with ' + str(addr)  
                data = sockfd.recv(1024)
                print 'Client: ' + data 

#    def close(self):
#        self.client_socket[0].close()
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

    

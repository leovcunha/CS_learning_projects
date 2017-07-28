# -*- coding: utf-8 -*-
"""
Created on Wed Feb 08 09:57:50 2017

@author: c055867
"""

import socket
import sys
import select
import utils

class Server(object):

    def __init__(self, port):
        self.port = int(port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket.bind(('', self.port))
        self.socket.listen(5)
        self.socket_list[self.socket] = "server"
        
    def start(self):
        while 1:
            
            ready_to_read, ready_to_write, in_error = select.select(self.socket_list, [], [], 0)
            for sock in ready_to_read:
                if sock == self.socket:
                    sockfd, addr = self.socket.accept()
                    self.socket_list.append(sockfd)
                    print 'Connected with ' + str(addr)

                else:
                  
                    try:
                        data = sock.recv(utils.MESSAGE_LENGTH)
                        if data:
                            data= data.strip()
                        else:
                        # remove the socket that's broken    
                            if sock in self.socket_list:
                                del self.socket_list.remove(sock)
                            self.broadcast(sock, sock_list, utils.SERVER_CLIENT_LEFT_CHANNEL.format(name))
                    except: 
                        self.broadcast(sock, sock_list, utils.SERVER_CLIENT_LEFT_CHANNEL.format(name))
                        continue

        
    def broadcast (self, msg):
        for socket in self.socket_list:
        # send the message only to peer
            if socket != server_socket and socket != sock :
                try :
                    socket.send(message)
                except :
                # broken socket connection
                    socket.close()
                # broken socket, remove it
                    if socket in self.socket_list:
                        self.socket_list.remove(socket)
        
#    def close(self):
#        self.client_socket[0].close()
        
args = sys.argv
if len(args) != 2:
    print "Please supply a server port."
    sys.exit()
server = Server(args[1])
print "Chat server started on port " + str(server.port)
server.accept()
print server.receive()

#    if not data: break
#    print data
#    server.close()

    

# -*- coding: utf-8 -*-
"""
Created on Wed Feb 08 09:57:50 2017

@author: leovcunha
"""

import socket
import sys
import select
import utils
from client_split_messages import pad_message

class Server(object):

    def __init__(self, port):
        self.socket_list = []
        self.port = int(port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket.bind(('', self.port))
        self.socket.listen(5)
        self.socket_list.append(self.socket)
        self.channels = {}
        self.sock_name = {}
        
    def start(self):
        message = ""
        while 1:
            
            ready_to_read, ready_to_write, in_error = select.select(self.socket_list, [], [])

            for sock in ready_to_read:
                if sock == self.socket:
                    clientsock, addr = self.socket.accept()
                    self.socket_list.append(clientsock)
                    print 'Connected with ' + str(addr)
                    first_message = True

                else:
                    channelrn = None
                    for channel in self.channels:
                        if sock in channels[channel]:
                            channelrn = channel
                  
                    try:
                        data = sock.recv(4096)
                        if data:
                            message += data
                            if len(message) < utils.MESSAGE_LENGTH:
                                continue

                            if len(message) >= utils.MESSAGE_LENGTH:
                                message = (message)[utils.MESSAGE_LENGTH:]
                                data = (message)[:utils.MESSAGE_LENGTH]
                            data = data.rstrip()
                            # print "received data after stripping: " + str(len(data)) + data
                            args = data.split()
                            if args == []:
                                continue
                            if first_message:
                                self.sockname[sock] = args[0]
                                first_message = False
                                continue
                            if args[0] == '/list':
                                for channel in channels:
                                    sock.sendall(pad_message(channel + "\n"))
                                continue
                            if args[0] == '/create':
                                if len(args) == 1:
                                    sock.sendall(pad_message(utils.SERVER_CREATE_REQUIRES_ARGUMENT + "\n"))
                                    continue
                                if args[1] in channels:
                                    sock.sendall(pad_message(utils.SERVER_CHANNEL_EXISTS.format(args[1]) + "\n"))
                                    continue
                                channels[args[1]] = []
                                for channel in channels:
                                    if sock in channels[channel]:
                                        channels[channel].remove(sock)
                                channels[args[1]].append(sock)
                                continue
                            if args[0] == '/join':
                                if len(args) == 1:
                                    sock.sendall(pad_message(utils.SERVER_JOIN_REQUIRES_ARGUMENT + "\n"))
                                    continue
                                if args[1] not in channels:
                                    sock.sendall(pad_message(utils.SERVER_NO_CHANNEL_EXISTS.format(args[1]) + "\n"))
                                    continue
                                for channel in channels:
                                    if sock in channels[channel]:
                                        channels[channel].remove(sock)
                                channels[args[1]].append(sock)
                                broadcast(server_socket, sock, args[1], utils.SERVER_CLIENT_JOINED_CHANNEL.format(self.sock_name[sock]) + "\n")
                                continue
                            if args[0][0] == '/':
                                sock.sendall(pad_message(utils.SERVER_INVALID_CONTROL_MESSAGE.format(args[0]) + "\n"))
                                continue
                            joinedChannel = False
                            for channel in channels:
                                if sock in channels[channel]:
                                    joinedChannel = True
                            if not joinedChannel:
                                sock.sendall(pad_message(utils.SERVER_CLIENT_NOT_IN_CHANNEL + "\n"))
                                continue

                        # there is something in the socket
                            self.broadcast(self.socket, sock, channelrn, "\r" + '[' + self.sock_name[sock] + '] ' + data + "\n")  
                        else:
                            # remove the socket that's broken    
                            if sock in self.socket_list:
                                self.socket_list.remove(sock)
                            # at this stage, no data means probably the connection has been broken
                            self.broadcast(self.socket, sock, channelrn, utils.SERVER_CLIENT_LEFT_CHANNEL.format(self.sock_name[sock]) + "\n") 

                    except: 
                        self.broadcast(self.socket, sock, channelrn, utils.SERVER_CLIENT_LEFT_CHANNEL.format(self.sock_name[sock]))
                        continue

        
    def broadcast (server_socket, sock, channel, msg):
        if channel is None:
            return
        for socket in self.channels[channel]:
        # send the message only to peer
            if socket != server_socket and socket != sock :
                try :
                    socket.sendall(pad_message(msg))
                except :
                # broken socket connection
                    socket.close()
                # broken socket, remove it
                    if socket in self.socket_list:
                        self.socket_list.remove(socket)
                        self.channels[channel].remove(socket)
        

        
args = sys.argv
if len(args) != 2:
    print "Please supply a server port."
    sys.exit()
server = Server(args[1])
print "Chat server started on port " + str(server.port)
server.start()
    

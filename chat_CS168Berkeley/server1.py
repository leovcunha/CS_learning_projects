# -*- coding: utf-8 -*-
"""
Created on Fri Feb 10 11:24:36 2017

@author: c055867
"""

# -*- coding: utf-8 -*-
import socket
import sys
import select
import utils


class Server(object):
    def __init__(self, port):
        self.port = int(port)
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.socket_dict = {}
        self.channel_socks_dict = {}
        self.socket_buffer_dict = {}

    def bind(self):
        self.server_socket.bind(('', self.port))
        self.server_socket.listen(5)

    def start(self):
        self.bind()
        self.socket_dict[self.server_socket] = "server"
        sock_list = []
        name = ""
        while 1:

            # get the list sockets which are ready to be read through select
            # 4th arg, time_out  = 0 : poll and never block
            ready_to_read, ready_to_write, in_error = select.select(self.socket_dict.keys(), [], [], 0)

            for sock in ready_to_read:
                # a new connection request recieved
                if sock == self.server_socket:
                    sockfd, addr = self.server_socket.accept()
                    self.socket_dict[sockfd] = ""
                    print "Client (%s, %s) connected" % addr
                    # self.broadcast(sockfd, "[%s:%s] entered our chatting room\n" % addr)
                else:
                    try:
                        data = sock.recv(utils.MESSAGE_LENGTH)
                        if data:
                            data = self.buffered_data(sock, data)
                            if not data:
                                continue
                            data = data.strip()

                            if self.socket_dict[sock] == "":
                                self.socket_dict[sock] = data
                            else:
                                sock_list = self.get_channel(sock)
                                name = self.socket_dict[sock]
                                if data[0] == "/":
                                    self.dispatch(data, sock, name)
                                else:
                                    if len(sock_list) == 0:
                                        self.send(sock, utils.SERVER_CLIENT_NOT_IN_CHANNEL)
                                    self.broadcast(sock, sock_list, '[' + name + '] ' + data)
                        else:
                            if sock in self.socket_dict:
                                del self.socket_dict[sock]

                            # at this stage, no data means probably the connection has been broken
                            self.broadcast(sock, sock_list, utils.SERVER_CLIENT_LEFT_CHANNEL.format(name))

                    except:
                        self.broadcast(sock, sock_list, utils.SERVER_CLIENT_LEFT_CHANNEL.format(name))
                        continue

        self.server_socket.close()

    def buffered_data(self, sock, data):
        buffer_d = ""
        if sock in self.socket_buffer_dict:
            buffer_d = self.socket_buffer_dict[sock]

        buffer_d += data
        result = ""
        if len(buffer_d) >= utils.MESSAGE_LENGTH:
            result = buffer_d[:utils.MESSAGE_LENGTH]
            buffer_d = buffer_d[utils.MESSAGE_LENGTH:]

        self.socket_buffer_dict[sock] = buffer_d
        return result


    def get_channel(self, sock):
        for channel, sock_list in self.channel_socks_dict.items():
            if sock in sock_list:
                return sock_list

        return []

    def broadcast(self, sock, sock_list, message):

        for socket in sock_list:
            if socket != self.server_socket and socket != sock:
                try:
                    self.send(socket, message)
                except:
                    # broken socket connection
                    socket.close()
                    # broken socket, remove it
                    if socket in self.socket_dict:
                        del self.socket_dict[sock]

    def dispatch(self, message, sock, name):
        list_str = "list"
        create_str = "create"
        join_str = "join"
        control_message = message
        if control_message[1:] == list_str:
            self.list(sock)
        elif control_message[1:len(create_str) + 1] == create_str:
            commands = control_message.split()
            if len(commands) != 2:
                self.send(sock, utils.SERVER_CREATE_REQUIRES_ARGUMENT)
                return
            self.create(commands[1], sock)
        elif control_message[1:len(join_str) + 1] == join_str:
            commands = control_message.split()
            if len(commands) != 2:
                self.send(sock, utils.SERVER_JOIN_REQUIRES_ARGUMENT)
                return
            self.join(commands[1], sock, name)
        else:
            self.send(sock, utils.SERVER_INVALID_CONTROL_MESSAGE.format(control_message))

    def list(self, sock):
        for channel_name in self.channel_socks_dict.keys():
            self.send(sock, channel_name)

    def create(self, channel, sock):
        if channel in self.channel_socks_dict:
            self.send(sock, utils.SERVER_CHANNEL_EXISTS)
        else:
            self.channel_socks_dict[channel] = [sock]

    def join(self, channel, sock, name):
        if channel in self.channel_socks_dict:
            self.channel_socks_dict[channel].append(sock)
            self.broadcast(sock, self.channel_socks_dict[channel], utils.SERVER_CLIENT_JOINED_CHANNEL.format(name))
        else:
            self.send(sock, utils.SERVER_NO_CHANNEL_EXISTS)

    def send(self, sock, message):
        if len(message) < utils.MESSAGE_LENGTH:
            message = message.ljust(utils.MESSAGE_LENGTH)
        sock.send(message)

args = sys.argv
if len(args) != 2:
    print "Please supply a server port."
    sys.exit()
server = Server(args[1])
server.start()
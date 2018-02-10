# -*- coding: utf-8 -*-
"""
Created on Fri Feb  9 14:57:31 2018

@leovcunha
"""
import re
import symboltable

def parser(inputline):
    """ Encapsulates access to input code. Read an assembly language command, parses it and
    provide convenient access to the command's components. In addition, removes all white spaces
    and comments
    """
    if symboltable.comment_line.match(inputline):
       pass
    if symboltable.empty_line.match(inputline):
       pass
    return strip(inputline)

def strip(line):
    # removes whitespace and comments; returns line without a closing \n
    if len(line) < 1:
        return ''
    else:
        c = line[0]
        if c == "\n" or c == "/":
              return ""
        elif c == " ":
              return strip(line[1:])
        else:
              return c + strip(line[1:])


# -*- coding: utf-8 -*-
"""
Created on Fri Feb  9 15:04:10 2018

@author: leovcunha
"""
import re
import parser

comment_line = re.compile( r'\s*//.*')
empty_line = re.compile( r'\s*')
a_label = re.compile( r'\s*\(\w+\)\s*')
c_inst = re.compile( r'([DAM]+\s*\=.*)|(\s*\-?\!?[AMD10]\s*[+-|&]?\s*[AMD10]?\;J\w\w)')
a_inst = re.compile( r'@.*')
a_num = re.compile( r'@\d+')

def generate_ST(f):
    symbol_counter = 16
    label_rom_address = 0

    for iline in f:
        line = parser.parser(iline)

        if a_label.search(line):
        	# A instruction - label
            label = a_label.search(line).group()
            label = label.replace('(', '')
            label = label.replace(')', '')
            print("storing " + label + " at " + str(label_rom_address))
            symbols[label] = str(label_rom_address)
        if a_inst.search(line) or c_inst.search(line):
        	# A instruction - not label
            label_rom_address += 1
    return symbols


comp_vals = {
      '0': '0101010',
      '1': '0111111',
     '-1': '0111010',
      'D': '0001100',
      'A': '0110000',
      'M': '1110000',
     '!D': '0001101',
     '!A': '0110001',
     '!M': '1110001',
     '-D': '0001111',
     '-A': '0110011',
     '-M': '1110011',
    'D+1': '0011111',
    'A+1': '0110111',
    'M+1': '1110111',
    'D-1': '0001110',
    'A-1': '0110010',
    'M-1': '1110010',
    'D+A': '0000010',
    'D+M': '1000010',
    'D-A': '0010011',
    'D-M': '1010011',
    'A-D': '0000111',
    'M-D': '1000111',
    'D&A': '0000000',
    'D&M': '1000000',
    'D|A': '0010101',
    'D|M': '1010101'
}
dest_vals = {
       '': '000',
      'M': '001',
      'D': '010',
     'MD': '011',
     'DM': '011',
      'A': '100',
     'AM': '101',
     'MA': '101',
     'AD': '110',
     'DA': '110',
    'AMD': '111'
}
jump_vals = {
       '': '000',
    'JGT': '001',
    'JEQ': '010',
    'JGE': '011',
    'JLT': '100',
    'JNE': '101',
    'JLE': '110',
    'JMP': '111'
}
symbols = { # Symbol table
        'R0': '0',
        'R1': '1',
        'R2': '2',
        'R3': '3',
        'R4': '4',
        'R5': '5',
        'R6': '6',
        'R7': '7',
        'R8': '8',
        'R9': '9',
       'R10': '10',
       'R11': '11',
       'R12': '12',
       'R13': '13',
       'R14': '14',
       'R15': '15',
    'SCREEN': '16384',
       'KBD': '24576',
        'SP': '0',
       'LCL': '1',
       'ARG': '2',
      'THIS': '3',
      'THAT': '4'
}


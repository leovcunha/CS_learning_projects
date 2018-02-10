# -*- coding: utf-8 -*-
"""
Created on Fri Feb  9 15:00:42 2018

@author: leovcunha
"""
import re
import symboltable

# regex strings
comment_line = re.compile( r'\s*//.*')
empty_line = re.compile( r'\s*')
a_inst = re.compile( r'@.*')
a_num = re.compile( r'@\d+')
a_symb = re.compile( r'@.*')
a_label = re.compile( r'\s*\(\w+\)\s*')
c_inst = re.compile( r'([DAM]+\s*\=.*)|(\s*\-?\!?[AMD10]\s*[+-|&]?\s*[AMD10]?\;J\w\w)')
c_eq = re.compile( r'[DAM]+\s*\=\s*\-?\!?[AMD10]\s*[+-|&]?\s*[10AMD]?\s*')
c_dest = re.compile( r'[DAM]+\s*(?=\=)')
c_comp = re.compile( r'(?<=\=)\s*\-?\!?[AMD10]\s*[+-|&]?\s*[AMD10]?')
c_jmp = re.compile( r'D?A?M?0?1?\;J\w\w')
c_jmp_comp = re.compile( r'\s*\-?\!?[AMD10]\s*[+-|&]?\s*[AMD10]?(?=\;J)')
c_jmp_jmp = re.compile( r'(?<=\;)J\w\w')

def code(line, ST):
    """Translates Hack assembly into binary code
    """
    if a_inst.search(line):
    	# A instruction
        if a_num.search(line):
            # a instruction with number
            return(get_binary(str(a_num.search(line).group())[1:]))

        else:
        	# A instruction with variable or label
            label = a_inst.search(line).group()
            try:
                a_label_binary = format(int(ST[label[1:]]), '016b')
            except KeyError:
            	# not in symbol table yet
                ST[label[1:]] = str(symbol_counter)
                symbol_counter += 1
                a_label_binary = format(int(ST[label[1:]]), '016b')
            return a_label_binary

    if c_inst.search(line):
    	# C instruction
        if c_eq.search(line):
        	# dest = comp
            dest, comp = '', ''
            if c_dest.search(line):
                dest = str(c_dest.search(line).group())
                dest = ''.join(dest.split())
            if c_comp.search(line):
                comp = str(c_comp.search(line).group())
                comp = ''.join(comp.split())
            if dest and comp:
                c_comp_binary = convert_comp(comp, dest)
                return c_comp_binary
        if c_jmp.search(line):
        	# comp;jmp
            if c_jmp_comp.search(line):
                comp = str(c_jmp_comp.search(line).group())
                comp = ''.join(comp.split())
            if c_jmp_jmp.search(line):
                jump = str(c_jmp_jmp.search(line).group())
                jump = ''.join(jump.split())
            if comp and jump:
                c_jump_binary = convert_jump(comp, jump)
                return c_jump_binary


def convert_comp(arg1, arg2):
    """returns binary code of comp
    """
    return '111'+ symboltable.comp_vals[arg1]  + symboltable.dest_vals[arg2] + '000'

def convert_jump(arg1, arg2):
    """returns binary code of jump
    """
    return '111' + symboltable.comp_vals[arg1] + '000'+ symboltable.jump_vals[arg2]


def get_binary(string):
    """converts string to 16-bit binary
    """
    s_to_int = int(string)
    return format(s_to_int, '016b')
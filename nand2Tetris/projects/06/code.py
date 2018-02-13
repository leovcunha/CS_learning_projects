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
a_symb = re.compile( r'@.*')
a_label = re.compile( r'\s*\(\w+\)\s*')
c_inst = re.compile( r'([DAM]+\s*\=.*)|(\s*\-?\!?[AMD10]\s*[+-|&]?\s*[AMD10]?\;J\w\w)')
c_eq = re.compile( r'[DAM]+\s*\=\s*\-?\!?[AMD10]\s*[+-|&]?\s*[10AMD]?\s*')
c_dest = re.compile( r'[DAM]+\s*(?=\=)')
c_comp = re.compile( r'(?<=\=)\s*\-?\!?[AMD10]\s*[+-|&]?\s*[AMD10]?')
c_jmp = re.compile( r'D?A?M?0?1?\;J\w\w')
c_jmp_comp = re.compile( r'\s*\-?\!?[AMD10]\s*[+-|&]?\s*[AMD10]?(?=\;J)')
c_jmp_jmp = re.compile( r'(?<=\;)J\w\w')
a_inst = re.compile( r'@.*')
a_num = re.compile( r'@\d+')

def code(f, ST, out):
    """Translates Hack assembly into binary code
    """
    symbol_counter = 16
    for line in f:
        if comment_line.match(line):
            pass # skip line
        if empty_line.match(line):
            pass # skip line
        if a_inst.search(line):
        	# A instruction
            if a_num.search(line):
                # a instruction with number
                out.write(get_binary(str(a_num.search(line).group())[1:]) + "\n")
            else:
            	# A instruction with variable or label
                label = a_inst.search(line).group()
                if label[1:] not in ST.keys():
            	# A instruction - not label
            	    ST[label[1:]] = str(symbol_counter)
            	    print("storing " + label[1:] + " at " + str(symbol_counter))
            	    symbol_counter += 1
                a_label_binary = format(int(ST[label[1:]]), '016b')
                out.write(a_label_binary + "\n")

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
                    out.write(c_comp_binary + "\n")
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
                    out.write(c_jump_binary+'\n')


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
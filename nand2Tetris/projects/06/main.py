# -*- coding: utf-8 -*-
"""
Created on Fri Feb  9 15:04:28 2018

@author: c055867
"""

if __name__ == "__main__":
    import sys
    import re
    import parser
    import code
    import symboltable

    if len(sys.argv) > 2:
        print("Only one file is allowed to be inputted at a time.\nProgram exiting.")
        exit()


    in_to_out_file = re.compile( r'.*(?=\.asm)')
    in_file = str(in_to_out_file.match(sys.argv[1]).group())
    out = open(in_file+'.hack', 'w')

    f = open(sys.argv[1])
    ST = symboltable.generate_ST(f)

    for line in f:
        print("parsing line " + str(line))
        out.write(code.code(parser.parser(line), ST) + "/n")
    out.close()




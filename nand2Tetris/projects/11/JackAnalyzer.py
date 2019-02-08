

import os
from JackTokenizer import *
from CompilationEngine import *


class JackAnalyzer(object):

    def __init__(self):
        self.jacktokenizerList = []

    def __str__(self):
        rep = ""
        for tokenizer in self.jacktokenizerList:
            rep += str(tokenizer)
        return rep


    def parseInput(self, userInput):

        if os.path.isfile(userInput) and ".jack" in userInput:
            self.jacktokenizerList.append(JackTokenizer(userInput))

        elif os.path.isdir(userInput):
            for filename in os.listdir(userInput):
                dirname = userInput
                if ".jack" in filename:
                    self.jacktokenizerList.append(JackTokenizer(dirname + filename))

    def run(self):

        for tokenizer in self.jacktokenizerList:

            try:
                file = tokenizer.getFilePath()
                tokenFile = open(file, 'w')
            except FileNotFoundError:
                print("Could not open {} for writing", file)
            else:
                tokenFile.write("<tokens>\n")

                while tokenizer.hasMoreTokens:

                    tokenizer.advance()

                    tokenType = tokenizer.tokenType()
                    token = tokenizer.identifier()

                    if tokenType == KEYWORD:
                        tokenFile.write("<keyword> " + token + " </keyword>\n")
                    elif tokenType == SYMBOL:
                        #handle special cases
                        if token == "<":
                            token = "&lt;"
                        elif token == ">":
                            token = "&gt;"
                        elif token == "&":
                            token = "&amp;"

                        tokenFile.write("<symbol> " + token + " </symbol>\n")
                    elif tokenType == IDENTIFIER:
                        tokenFile.write("<identifier> " + token + " </identifier>\n")
                    elif tokenType == INT_CONST:
                        tokenFile.write("<integerConstant> " + str(token) + " </integerConstant>\n")
                    elif tokenType == STRING_CONST:
                        tokenFile.write("<stringConstant> " + token[1:-1] + " </stringConstant>\n")

                tokenFile.write("</tokens>")
                tokenFile.close()


                tokenFile = open(file, 'r')
                index = file.rfind("T")
                file = file[:index] + file[index+1:]
                compilationFile = open(file, 'w')
                CEngine = CompilationEngine(tokenFile, compilationFile)
                CEngine.run()

if __name__ == "__main__":
    try:
       userInput = argv[1]
    except:
        print("Error. Do python JackAnalyzer.py [source folder or file]")
    else:
        if os.path.isfile(userInput) or os.path.isdir(userInput):
            jackAnalyzer = JackAnalyzer()
            jackAnalyzer.parseInput(userInput)
            jackAnalyzer.run()

            #print our tokenizers
            #print(jackAnalyzer)
        else:
            print("{} is an invalid file/folder name", userInput)

    print("{} compiled succesfully.".format(userInput))
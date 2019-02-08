import os
from KeyValueIndex import *
from Constants import *

class CompilationEngine(object):
    '''creates xml files for later parsing into VM commands'''
    def __init__(self, infile, outfile):
        self.__m_infile = infile
        self.__m_outfile = outfile

        self.__m_translated = []
        self.__m_cursor = 0
        self.__m_curentToken = 0
        self.__m_nextToken = 0


    #get the file into a list
    def fileToTranslated(self):
        count = 0
        for line in self.__m_infile:
            if "tokens" not in line:
                tokens = line.split(" ")
                if "<stringConstant>" not in tokens:
                    self.__m_translated.append(KeyValueIndex(tokens[0][1:-1], tokens[1], count))
                else:
                    self.__m_translated.append(KeyValueIndex(tokens[0][1:-1], ' '.join(tokens[1:-1]), count))
            count += 1


    @property
    def hasMoreTokens(self):
        if self.__m_cursor+1 < len(self.__m_translated):
            return True
        return False

    #get next token
    def advance(self):
        self.__m_curentToken = self.__m_translated[self.__m_cursor]
        if self.__m_cursor+1 < len(self.__m_translated):
            self.__m_nextToken = self.__m_translated[self.__m_cursor+1]
            self.__m_cursor += 1


    #compilation
    def run(self):
        #get tokens
        self.fileToTranslated()
        self.compileClass()
        self.close()

    def compileClass(self):
        self.__m_outfile.write("<class>\n")
        while self.hasMoreTokens:
            if self.__m_curentToken:
                #test
                #print("Current {} Next {}".format(self.__m_curentToken.getKeyValueIndex()[1], self.__m_nextToken.getKeyValueIndex()[1]))
                #end test
                #always starts with a function
                if self.__m_curentToken.getKeyValueIndex()[1] == "function":
                    self.compileSubroutine()
                else:
                    self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
            self.advance()
        self.__m_outfile.write("</class>")

    def compileSubroutine(self):
        self.__m_outfile.write("<subroutineDec>\n")
        compilingParams = True
        compilingBody = False

        while self.hasMoreTokens:
            #if subroutineDec
            if self.__m_curentToken.getKeyValueIndex()[0] == "symbol" and compilingParams:
                #write (
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
                #write params
                self.compileParameterList()
                compilingParams = False
                #write )
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
                #done with Params go to Body
                compilingBody = True
            elif compilingBody:
                self.compileBody()
                break
            else:
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
            self.advance()
        self.__m_outfile.write("</subroutineDec>\n")
        self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])


    #compile body of function
    def compileBody(self):
        self.__m_outfile.write("<subroutineBody>\n")
        while self.hasMoreTokens:
            if self.__m_nextToken.getKeyValueIndex()[1] == "}":
                break
            else:
                #if method/constructor
                if self.__m_curentToken.getKeyValueIndex()[1] in ("method", "constructor"):
                    self.compileSubroutine()
                #if var
                elif self.__m_curentToken.getKeyValueIndex()[1] == "var":
                    self.compileVarDec()
                #if statment*
                elif self.__m_curentToken.getKeyValueIndex()[1] in ("let", "while", "do", "if", "return"):
                    self.compileStatements()
                #class var declaration aka fields
                elif self.__m_curentToken.getKeyValueIndex()[1] == "field":
                    self.compileClassVarDec()
                else:
                    self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
            self.advance()

        self.writeToFile(self.__m_nextToken.getKeyValueIndex()[0], self.__m_nextToken.getKeyValueIndex()[1])
        self.__m_outfile.write("</subroutineBody>\n")

    def compileClassVarDec(self):
        self.__m_outfile.write("<classVarDec>\n")
        self.compileUpToSymbol(";")
        self.__m_outfile.write("</classVarDec>\n")

    def compileExpressionList(self):
        self.__m_outfile.write("<expressionList>\n")
        #loop through expresssions as long as there's an 'op' and not ')'
        if self.__m_nextToken.getKeyValueIndex()[1] != ')':
            self.compileExpression()
        self.__m_outfile.write("</expressionList>\n")

    def compileExpression(self):
        self.__m_outfile.write("<expression>\n")
        self.compileTerm()
        self.__m_outfile.write("</expression>\n")

    def compileTerm(self):
        self.__m_outfile.write("<term>\n")
        while True:
            if self.__m_curentToken.getKeyValueIndex()[1] in (";", "]", ")"):
                break
            elif self.__m_curentToken.getKeyValueIndex()[1] == "(":
                self.compileExpressionList()
                break
            else:
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
            self.advance()
        self.__m_outfile.write("</term>\n")

    def compileStatements(self):
        self.__m_outfile.write("<statements>\n")
        compiling = True
        while compiling:
            #when we finish compiling a statement, and the next element is not in ("let", "if", "while", "do", "return")
            if self.__m_curentToken.getKeyValueIndex()[1] == "let":
                self.compileLet()
            if self.__m_curentToken.getKeyValueIndex()[1] == "do":
                self.compileDo()
            if self.__m_curentToken.getKeyValueIndex()[1] == "return":
                self.compileReturn()
            if self.__m_nextToken.getKeyValueIndex()[1] not in ("let", "if", "while", "do", "return"):
                compiling = False
            self.advance()

        self.__m_outfile.write("</statements>\n")

    def compileLet(self):
        self.__m_outfile.write("<letStatement>\n")
        while True:
            if self.__m_curentToken.getKeyValueIndex()[1] == ";":
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
                break
            elif self.__m_curentToken.getKeyValueIndex()[1] in ("[", "=", "("):
                if self.__m_curentToken.getKeyValueIndex()[1] == "=":
                    self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
                    self.advance()
                #write out "(,["
                self.compileExpression()
                #write out "), ]"
                if self.__m_curentToken.getKeyValueIndex()[1] == ";":
                    continue
            else:
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
            self.advance()
        self.__m_outfile.write("</letStatement>\n")

    def compileDo(self):
        self.__m_outfile.write("<doStatement>\n")
        while True:
            if self.__m_curentToken.getKeyValueIndex()[1] == ";":
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
                break
            elif self.__m_curentToken.getKeyValueIndex()[1] in ("(", "[", "=") :
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
                self.compileExpressionList()
            else:
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
            self.advance()
        self.__m_outfile.write("</doStatement>\n")

    def compileReturn(self):
        self.__m_outfile.write("<returnStatement>\n")
        self.compileUpToSymbol(";")
        self.__m_outfile.write("</returnStatement>\n")

    #compile var dec
    def compileVarDec(self):
        self.__m_outfile.write("<varDec>\n")
        self.compileUpToSymbol(";")
        self.__m_outfile.write("</varDec>\n")

    def compileUpToSymbol(self, symbol):
        while True:
            if self.__m_curentToken.getKeyValueIndex()[1] == symbol:
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
                break;
            else:
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
            self.advance()



    #compile param list
    def compileParameterList(self):
        self.__m_outfile.write("<parameterList>\n")
        compiling = True
        while compiling:
            if self.__m_nextToken.getKeyValueIndex()[0] == "symbol":
                compiling = False
            else:
                self.writeToFile(self.__m_curentToken.getKeyValueIndex()[0], self.__m_curentToken.getKeyValueIndex()[1])
            self.advance()
        self.__m_outfile.write("</parameterList>\n")

    #write straight to file
    def writeToFile(self,  tagName, content):
        self.__m_outfile.write("<{}> {} </{}>\n".format(tagName, content, tagName))

    def close(self):
        self.__m_outfile.close()
